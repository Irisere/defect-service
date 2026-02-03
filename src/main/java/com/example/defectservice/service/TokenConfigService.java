package com.example.defectservice.service;

import com.example.defectservice.domain.entity.TokenConfig;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.repository.TokenConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TokenConfigService {

    @Autowired
    private TokenConfigRepository tokenConfigRepository;

    /**
     * 新增/修改Token配置
     */
    public TokenConfig saveTokenConfig(TokenConfig tokenConfig) {
        // 业务校验：平台和Token不能为空
        if (tokenConfig.getPlatform() == null || tokenConfig.getPlatform().trim().isEmpty()) {
            throw new BusinessException("平台名称不能为空");
        }
        if (tokenConfig.getToken() == null || tokenConfig.getToken().trim().isEmpty()) {
            throw new BusinessException("Token值不能为空");
        }
        return tokenConfigRepository.save(tokenConfig);
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
     * 启用/禁用Token
     */
    public TokenConfig updateTokenStatus(Integer id, Boolean isActive) {
        TokenConfig tokenConfig = getTokenConfigById(id);
        tokenConfig.setIsActive(isActive);
        return tokenConfigRepository.save(tokenConfig);
    }
}