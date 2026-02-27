package com.example.defectservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Token配置实体类
 */
@Data
@Entity
@Table(name = "token_config")
public class TokenConfig {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 平台（github/gitlab/gitee）
     */
    @Column(name = "platform", nullable = false, length = 20)
    private String platform;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * token
     */
    @Column(name = "token", nullable = false, length = 200)
    private String token;

    /**
     * 是否激活使用（1-是，0-否）
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * 可用状态：0-失效，1-有效，2-未知
     */
    @Column(name = "is_usable", nullable = false)
    private Integer isUsable = 2; // 默认设为“未知”

    /**
     * 创建时间
     */
    @CreationTimestamp // 只要是新增操作，Hibernate 自动填入当前时间
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}