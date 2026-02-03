package com.example.defectservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * 仓库实体类
 */
@Data
@Entity
@Table(name = "repo")
public class Repo {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目主键
     */
    @Column(name = "poject_id", nullable = false)
    private Integer projectId;

    /**
     * 平台（github/gitlab/gitee）
     */
    @Column(name = "platform", nullable = false, length = 20)
    private String platform;

    /**
     * 拥有者
     */
    @Column(name = "owner", nullable = false, length = 150)
    private String owner;

    /**
     * 仓库名
     */
    @Column(name = "repo_name", nullable = false, length = 250)
    private String repoName;

    /**
     * 最近一次更新时间
     */
    @Column(name = "recent_update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date recentUpdateTime;
}