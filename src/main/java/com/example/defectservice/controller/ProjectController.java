package com.example.defectservice.controller;

import com.example.defectservice.domain.entity.Project;
import com.example.defectservice.domain.vo.Result;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 新增项目
     */
    @PostMapping
    public Result<Project> addProject(@RequestBody Project project) {
        try {
            Project saved = projectService.saveProject(project);
            return Result.success(saved);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据id获取项目
     */
    @GetMapping("/id/{id}")
    public Result<Project> getProject(@PathVariable Integer id) {
        try {
            Project project = projectService.getProjectById(id);
            return Result.success(project);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 模糊搜索项目列表
     */
    @GetMapping("/search")
    public Result<List<Project>> searchProjects(@RequestParam(required = false) String name) {
        try {
            // 如果 name 为空，通常返回所有项目或空列表
            List<Project> projects = projectService.searchProjectsByName(name);
            return Result.success(projects);
        } catch (Exception e) {
            return Result.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有项目list
     */
    @GetMapping("/list")
    public Result<List<Project>> getAllProjects() {
        List<Project> list = projectService.getAllProjects();
        return Result.success(list);
    }

    /**
     * 更新项目
     */
    @PutMapping("/{id}")
    public Result<Project> updateProject(@PathVariable Integer id, @RequestBody Project project) {
        try {
            Project newProject = projectService.updateName(id,project);
            return Result.success(newProject);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除项目
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteProject(@PathVariable Integer id) {
        try {
            projectService.deleteProjectById(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}