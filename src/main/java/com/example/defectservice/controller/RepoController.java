package com.example.defectservice.controller;

import com.example.defectservice.domain.entity.Repo;
import com.example.defectservice.domain.vo.Result;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.service.CollectService;
import com.example.defectservice.service.RepoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repo")
public class RepoController {

    @Autowired
    private RepoService repoService;

    @Autowired
    private CollectService collectService;

    /**
     * 调用 Python 服务采集缺陷 Issue
     */
    @GetMapping("/collect/issue")
    public Result<Map<String, Object>> collectIssue(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam(defaultValue = "open") String state,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String since,
            @RequestParam(required = false) String until,
            @RequestParam("repoId") String repoId) {

        try {
            String result = collectService.collectIssueFromPython(
                    owner, repo, state, platform, since, until, repoId
            );
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> pythonResult = mapper.readValue(result, Map.class);
            return Result.success(pythonResult);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加仓库
     */
    @PostMapping
    public Result<Repo> addRepo(@RequestBody Repo repo) {
        try {
            Repo saved = repoService.saveRepo(repo);
            return Result.success(saved);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据id获取仓库信息
     */
    @GetMapping("/{id}")
    public Result<Repo> getRepo(@PathVariable Integer id) {
        try {
            Repo repo = repoService.getRepoById(id);
            return Result.success(repo);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据项目id查询仓库
     */
    @GetMapping("/project/{projectId}")
    public Result<List<Repo>> getReposByProjectAndPlatform(
            @PathVariable Integer projectId) {
        try {
            List<Repo> list = repoService.getReposByProjectId(projectId);
            return Result.success(list);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据owner和name查询仓库
     */
    @GetMapping("/owner/{owner}/name/{repoName}")
    public Result<Repo> getRepoByOwnerAndName(
            @PathVariable String owner,
            @PathVariable String repoName) {
        try {
            Repo repo = repoService.getRepoByOwnerAndName(owner, repoName);
            return Result.success(repo);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有仓库
     */
    @GetMapping
    public Result<List<Repo>> getAllRepos() {
        List<Repo> list = repoService.getAllRepos();
        return Result.success(list);
    }

    /**
     * 更新仓库信息
     */
    @PutMapping
    public Result<Repo> updateRepo(@RequestBody Repo repo) {
        try {
            Repo updated = repoService.saveRepo(repo);
            return Result.success(updated);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除仓库
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRepo(@PathVariable Integer id) {
        try {
            repoService.deleteRepoById(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}