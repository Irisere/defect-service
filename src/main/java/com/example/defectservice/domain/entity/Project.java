package com.example.defectservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 项目实体类
 */
@Data
@Entity
@Table(name = "project")
public class Project {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目名称
     */
    @Column(name = "name", nullable = false, length = 80)
    private String name;

    /**
     * 备注
     */
    @Column(name = "remark", nullable = false, length = 255)
    private String remark;
}