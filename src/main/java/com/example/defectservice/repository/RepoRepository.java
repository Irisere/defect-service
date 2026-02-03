package com.example.defectservice.repository;

import com.example.defectservice.domain.entity.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 仓库数据访问层
 */
@Repository
public interface RepoRepository extends JpaRepository<Repo, Integer> {

    /**
     * 根据项目ID查询仓库
     */
    List<Repo> findByProjectId(Integer projectId);

    /**
     * 根据拥有者和仓库名查询仓库
     */
    Repo findByOwnerAndRepoName(String owner, String repoName);
}
