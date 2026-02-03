package com.example.defectservice.controller;

import com.example.defectservice.domain.entity.StandardizedDefect;
import com.example.defectservice.domain.vo.Result;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.service.StandardizedDefectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/defect")
public class StandardizedDefectController {

    @Autowired
    private StandardizedDefectService defectService;

//    @PostMapping
//    public Result<StandardizedDefect> addDefect(@RequestBody StandardizedDefect defect) {
//        try {
//            StandardizedDefect saved = defectService.saveDefect(defect);
//            return Result.success(saved);
//        } catch (BusinessException e) {
//            return Result.error(e.getMessage());
//        }
//    }


    /**
     * 根据id查询报告信息
     */
    @GetMapping("/{id}")
    public Result<StandardizedDefect> getDefect(@PathVariable Integer id) {
        try {
            StandardizedDefect defect = defectService.getDefectById(id);
            return Result.success(defect);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据仓库查询缺陷报告
     */
    @GetMapping("/repo/{repoId}")
    public Result<List<StandardizedDefect>> getDefectsByRepoId(@PathVariable Integer repoId) {
        try {
            List<StandardizedDefect> list = defectService.getDefectsByRepoId(repoId);
            return Result.success(list);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

//    @GetMapping("/repo/{repoId}/issue/{issueId}")
//    public Result<StandardizedDefect> getDefectByRepoAndIssue(
//            @PathVariable Integer repoId,
//            @PathVariable String issueId) {
//        try {
//            StandardizedDefect defect = defectService.getDefectByRepoIdAndIssueId(repoId, issueId);
//            return Result.success(defect);
//        } catch (BusinessException e) {
//            return Result.error(e.getMessage());
//        }
//    }

//    /**
//     * 获取所有缺陷报告
//     */
//    @GetMapping
//    public Result<List<StandardizedDefect>> getAllDefects() {
//        List<StandardizedDefect> list = defectService.getAllDefects();
//        return Result.success(list);
//    }

    /**
     * 更新缺陷报告（可微调内容）
     */
    @PutMapping
    public Result<StandardizedDefect> updateDefect(@RequestBody StandardizedDefect defect) {
        try {
            StandardizedDefect updated = defectService.saveDefect(defect);
            return Result.success(updated);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除缺陷报告
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDefect(@PathVariable Integer id) {
        try {
            defectService.deleteDefectById(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}