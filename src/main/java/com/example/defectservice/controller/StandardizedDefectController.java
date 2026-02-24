package com.example.defectservice.controller;

import com.example.defectservice.domain.entity.StandardizedDefect;
import com.example.defectservice.domain.vo.Result;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.service.StandardizedDefectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    /**
     * 导出缺陷报告
     */
    @GetMapping("/repo/{repoId}/export")
    public void exportDefects(@PathVariable Integer repoId, HttpServletResponse response) {
        try {
            // 1. 获取数据
            List<StandardizedDefect> list = defectService.getDefectsByRepoId(repoId);

            // 2. 构造动态文件名
            String rawFileName = defectService.generateExportFileName(repoId);
            String encodedFileName = URLEncoder.encode(rawFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            // 3. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 注意：这里把文件名传给前端，前端可以通过 content-disposition 获取，或者前端自己构造
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            // 暴露 Header 给前端 JavaScript 访问
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            // 4. 写入 Excel
            EasyExcel.write(response.getOutputStream(), StandardizedDefect.class)
                    .sheet("缺陷列表")
                    .doWrite(list);
        } catch (Exception e) {
            log.error("导出失败", e);
            // 注意：导出流已经开启的情况下不能返回 Result.error，这里抛出异常由全局捕获处理
        }
    }

}