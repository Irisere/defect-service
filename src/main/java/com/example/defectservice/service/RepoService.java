package com.example.defectservice.service;

import com.example.defectservice.domain.entity.Repo;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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