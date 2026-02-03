package com.example.defectservice.controller;

import com.example.defectservice.domain.vo.Result;
import com.example.defectservice.domain.vo.StatVO;
import com.example.defectservice.repository.ProjectRepository;
import com.example.defectservice.repository.RepoRepository;
import com.example.defectservice.repository.StandardizedDefectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private StandardizedDefectRepository defectRepository;

    /**
     * 1. 获取顶部概览数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Long>> getOverview() {
        Map<String, Long> map = new HashMap<>();
        map.put("totalProjects", projectRepository.count());
        map.put("totalRepos", repoRepository.count());
        map.put("totalDefects", defectRepository.count());
        // 这里可以扩展更多，比如今日新增等
        return Result.success(map);
    }

    /**
     * 2. 获取严重程度分布 (用于饼图)
     */
    @GetMapping("/severity")
    public Result<List<StatVO>> getSeverityStats() {
        List<StatVO> list = defectRepository.countBySeverity();
        // 处理 null 值的 severity 为 "Unknown"
        list.forEach(item -> {
            if (item.getName() == null) item.setName("Unknown");
        });
        return Result.success(list);
    }

    /**
     * 3. 获取近30天缺陷趋势 (用于折线图)
     */
    @GetMapping("/trend")
    public Result<List<StatVO>> getTrendStats() {
        // 由于使用了 nativeQuery 返回 Object[]，需要手动转换
        List<Object[]> rawList = defectRepository.findTrendLastNDays(30);
        List<StatVO> result = rawList.stream()
                .map(obj -> new StatVO((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
        return Result.success(result);
    }

    /**
     * 4. 获取缺陷最多的仓库 Top 5 (用于柱状图)
     */
    @GetMapping("/top-repos")
    public Result<List<StatVO>> getTopRepos() {
        // 使用 PageRequest 限制只取前5名
        List<StatVO> list = defectRepository.findTopRepoDefects(PageRequest.of(0, 5));
        return Result.success(list);
    }
}