package com.example.defectservice.repository;

import com.example.defectservice.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 项目数据访问层
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * 根据项目名称查询项目
     */
    Project findByName(String name);
}