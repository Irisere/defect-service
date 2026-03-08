package com.example.defectservice.service;

import com.example.defectservice.domain.entity.Repo;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Optional;

@Service
public class RepoService {

    @Autowired
    private RepoRepository repoRepository;

    /**
     * 新增/修改仓库
     */
    public Repo saveRepo(Repo repo) {
        // 核心校验
        if (repo.getProjectId() == null) {
            throw new BusinessException("项目ID不能为空");
        }
        if (repo.getPlatform() == null || repo.getPlatform().trim().isEmpty()) {
            throw new BusinessException("平台不能为空");
        }
        if (repo.getOwner() == null || repo.getOwner().trim().isEmpty()) {
            throw new BusinessException("仓库拥有者不能为空");
        }
        if (repo.getRepoName() == null || repo.getRepoName().trim().isEmpty()) {
            throw new BusinessException("仓库名称不能为空");
        }
        return repoRepository.save(repo);
    }

    /**
     * 根据url导入仓库
     */
    public Repo parseUrlAndCreate(String url, Integer projectId) {
        // 正则表达式匹配：提取平台、拥有者、仓库名
        // 支持格式：https://github.com/owner/repo 或 https://gitee.com/owner/repo.git
        String regex = "https?://(github|gitee|gitlab)\\.com/([^/]+)/([^/.]+)(\\.git)?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            Repo repo = new Repo();
            repo.setPlatform(matcher.group(1).toLowerCase());
            repo.setOwner(matcher.group(2));
            repo.setRepoName(matcher.group(3));
            repo.setProjectId(projectId);
            return saveRepo(repo); // 复用原有的保存逻辑和校验
        } else {
            throw new BusinessException("无法识别该 URL 格式，目前仅支持 GitHub/Gitee/GitLab 仓库链接");
        }
    }

    public boolean checkRepoExists(String platform, String owner, String repoName) {
        String targetUrl = "";
        // 构造不同平台的公开访问地址
        if ("github".equalsIgnoreCase(platform)) {
            targetUrl = String.format("https://github.com/%s/%s", owner, repoName);
        } else if ("gitee".equalsIgnoreCase(platform)) {
            targetUrl = String.format("https://gitee.com/%s/%s", owner, repoName);
        } else if ("gitlab".equalsIgnoreCase(platform)) {
            targetUrl = String.format("https://gitlab.com/%s/%s", owner, repoName);
        }

        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 模拟浏览器头部，防止某些平台屏蔽爬虫
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            // 200 表示存在且公开可访问
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据ID查询仓库
     */
    public Repo getRepoById(Integer id) {
        Optional<Repo> optional = repoRepository.findById(id);
        return optional.orElseThrow(() -> new BusinessException("未找到ID为" + id + "的仓库"));
    }

    /**
     * 根据项目ID和平台查询仓库列表
     */
    public List<Repo> getReposByProjectId(Integer projectId) {
        List<Repo> list = repoRepository.findByProjectId(projectId);
        if (list.isEmpty()) {
            throw new BusinessException("未找到项目ID为" + projectId + "的仓库");
        }
        return list;
    }

    /**
     * 根据拥有者和仓库名查询仓库
     */
    public Repo getRepoByOwnerAndName(String owner, String repoName) {
        Repo repo = repoRepository.findByOwnerAndRepoName(owner, repoName);
        if (repo == null) {
            throw new BusinessException("未找到拥有者为" + owner + "、名称为" + repoName + "的仓库");
        }
        return repo;
    }

    /**
     * 查询所有仓库
     */
    public List<Repo> getAllRepos() {
        return repoRepository.findAll();
    }

    /**
     * 删除仓库
     */
    public void deleteRepoById(Integer id) {
        if (!repoRepository.existsById(id)) {
            throw new BusinessException("要删除的仓库不存在");
        }
        repoRepository.deleteById(id);
    }
}