package com.example.defectservice.domain.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
        uniqueConstraints = @UniqueConstraint(columnNames = {"repo_id", "issue_id"})
)
public class StandardizedDefect {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    private Integer id;

    /**
     * 仓库主键
     */
    @Column(name = "repo_id", nullable = false)
    @ExcelIgnore
    private Integer repoId;

    /**
     * 平台侧缺陷ID（如GitHub的issue编号）
     */
    @Column(name = "issue_id", nullable = false, length = 50)
    @ExcelProperty("外部IssueID")
    private String issueId;

    /**
     * 缺陷标题
     */
    @Column(name = "title", nullable = false, length = 500)
    @ExcelProperty("标题")
    private String title;

    /**
     * 缺陷描述（支持长文本）
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @ExcelProperty("描述")
    private String description;

    /**
     * 版本信息
     */
    @Column(name = "version", length = 20)
    @ExcelProperty("版本")
    private String version;

    /**
     * 复现步骤
     */
    @Column(name = "steps_to_reproduce", length = 500)
    @ExcelProperty("复现步骤")
    private String stepsToReproduce;

    /**
     * 严重程度
     */
    @Column(name = "severity", length = 20)
    @ExcelProperty("严重程度")
    private String severity;

    /**
     * issue的url
     */
    @Column(name = "url", nullable = false, length = 500)
    @ExcelProperty("链接地址")
    private String url;

    /**
     * 缺陷报告发布时间
     */
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ExcelProperty("创建时间")
    private Date createdAt;

    /**
     * 缺陷报告结构化记录时间
     */
    @Column(name = "record_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ExcelIgnore
    private Date recordAt;
}