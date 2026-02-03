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
     * 根据名称获取项目
     */
    @GetMapping("/name/{name}")
    public Result<Project> getProjectByName(@PathVariable String name) {
        try {
            Project project = projectService.getProjectByName(name);
            return Result.success(project);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
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