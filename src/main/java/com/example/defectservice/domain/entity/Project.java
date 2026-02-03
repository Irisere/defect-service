package com.example.defectservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 项目实体类
 */
@Data
@Entity
@Table(name = "poject") // 若数据库表名是拼写错误，建议改为project并同步数据库
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