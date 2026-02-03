package com.example.defectservice.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatVO {
    private String name;  // 统计项名称（如 "Critical", "2023-10-01"）
    private Long value;   // 统计数值
}