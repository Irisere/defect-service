package com.example.defectservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

/**
 * 结构化后的缺陷报告实体类
 * 注意：为source_defect表添加(repo_id, issue_id)联合唯一索引（此处映射到standardized_defect，若有source_defect表需单独创建实体）
 */
@Data
@Entity
@Table(
        name = "standardized_defect",
        // 为source_defect表添加联合唯一索引，若该实体对应source_defect，需修改表名并添加索引
        uniqueConstraints = @UniqueConstraint(columnNames = {"repo_id", "issue_id"})
)
public class StandardizedDefect {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 仓库主键
     */
    @Column(name = "repo_id", nullable = false)
    private Integer repoId;

    /**
     * 平台侧缺陷ID（如GitHub的issue编号）
     */
    @Column(name = "issue_id", nullable = false, length = 50)
    private String issueId;

    /**
     * 缺陷标题
     */
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    /**
     * 缺陷描述（支持长文本）
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * 版本信息
     */
    @Column(name = "version", length = 20)
    private String version;

    /**
     * 复现步骤
     */
    @Column(name = "steps_to_reproduce", length = 500)
    private String stepsToReproduce;

    /**
     * 严重程度
     */
    @Column(name = "severity", length = 20)
    private String severity;

    /**
     * issue的url
     */
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    /**
     * 缺陷报告发布时间
     */
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * 缺陷报告结构化记录时间
     */
    @Column(name = "record_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordAt;
}