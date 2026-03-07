package com.example.defectservice.repository;

import com.example.defectservice.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目数据访问层
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    // 自动生成 SQL: WHERE name LIKE %?1%
    List<Project> findByNameContaining(String name);
}