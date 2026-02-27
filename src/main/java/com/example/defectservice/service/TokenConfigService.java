package com.example.defectservice.service;

import com.example.defectservice.domain.entity.TokenConfig;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.repository.TokenConfigRepository;
import com.example.defectservice.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

@Service
public class TokenConfigService {

    @Autowired
    private TokenConfigRepository tokenConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 新增Token配置
     */
    public TokenConfig saveTokenConfig(TokenConfig tokenConfig) {
        // 2. 加密 Token
        try {
            String encryptedToken = EncryptionUtils.encrypt(tokenConfig.getToken());
            tokenConfig.setToken(encryptedToken);
            return tokenConfigRepository.save(tokenConfig);
        } catch (Exception e) {
            throw new BusinessException("Token 加密失败");
        }
    }

    /**
     * 根据ID查询Token配置
     */
    public TokenConfig getTokenConfigById(Integer id) {
        Optional<TokenConfig> optional = tokenConfigRepository.findById(id);
        return optional.orElseThrow(() -> new BusinessException("未找到ID为" + id + "的Token配置"));
    }

    /**
     * 根据平台查询有效Token
     */
    public TokenConfig getActiveTokenByPlatform(String platform) {
        TokenConfig tokenConfig = tokenConfigRepository.findByPlatformAndIsActive(platform, true);
        if (tokenConfig == null) {
            throw new BusinessException("未找到" + platform + "平台的有效Token");
        }
        return tokenConfig;
    }

    /**
     * 查询所有Token配置
     */
    public List<TokenConfig> getAllTokenConfigs() {
        return tokenConfigRepository.findAll();
    }

    /**
     * 根据ID删除Token配置
     */
    public void deleteTokenConfigById(Integer id) {
        if (!tokenConfigRepository.existsById(id)) {
            throw new BusinessException("要删除的Token配置不存在");
        }
        tokenConfigRepository.deleteById(id);
    }

    /**
     * 启用/禁用Token，并确保同平台互斥
     */
    @Transactional // 涉及多表操作，务必开启事务
    public TokenConfig updateTokenStatus(Integer id, Boolean isActive) {
        TokenConfig tokenConfig = getTokenConfigById(id);

        // 如果是要启用该 Token
        if (Boolean.TRUE.equals(isActive)) {
            // 将该平台下所有其他的 Token 全部设为不活跃
            tokenConfigRepository.deactivateOtherTokens(tokenConfig.getPlatform(), id);
        }

        tokenConfig.setIsActive(isActive);
        return tokenConfigRepository.save(tokenConfig);
    }

    /**
     * 校验指定 ID 的 Token 是否真实有效，并更新数据库状态 (0-失效, 1-有效, 2-未知)
     */
    public boolean validateToken(Integer id) {
        TokenConfig config = getTokenConfigById(id);
        String plainToken;

        try {
            plainToken = EncryptionUtils.decrypt(config.getToken());
        } catch (Exception e) {
            // 解密失败直接标记为失效
            config.setIsUsable(0);//更新数据库状态
            tokenConfigRepository.save(config);
            System.out.println("Token 解密失败，已标记为失效");
            throw new BusinessException("Token 解密失败，已标记为失效");
        }

        int status;
        boolean result;
        String platform = config.getPlatform().toLowerCase();

        try {
            result = switch (platform) {
                case "github" -> checkGithubToken(plainToken);
                case "gitee" -> checkGiteeToken(plainToken);
                case "gitlab" -> checkGitlabToken(plainToken);
                default -> throw new BusinessException("不支持的平台");
            };
            // 校验通过设为 1，不通过设为 0
            status = result ? 1 : 0;
        } catch (Exception e) {
            // 如果是网络超时等未知错误，可以保持为 2 (未知)，或者设为 0
            // 这里建议：如果是授权错误(401)设为0，网络错误设为2
            status = 0;
            result = false;
        }

        config.setIsUsable(status);//更新数据库状态
        tokenConfigRepository.save(config);

        return result;
    }

    private boolean checkGithubToken(String token) {
        // GitHub 验证接口：获取当前授权用户信息
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.github.com/user", HttpMethod.GET, entity, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    private boolean checkGiteeToken(String token) {
        // Gitee 验证接口
        String url = "https://gitee.com/api/v5/user?access_token=" + token;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    private boolean checkGitlabToken(String token) {
        // GitLab 验证接口
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://gitlab.com/api/v4/user", HttpMethod.GET, entity, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
}