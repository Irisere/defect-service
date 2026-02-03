package com.example.defectservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

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
    @Column(name = "remark", nullable = false, length = 200)
    private String remark;

    /**
     * gitee的token
     */
    @Column(name = "token", nullable = false, length = 200)
    private String token;

    /**
     * 是否有效（1-有效，0-无效）
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}