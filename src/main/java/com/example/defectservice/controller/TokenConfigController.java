package com.example.defectservice.controller;

import com.example.defectservice.domain.entity.TokenConfig;
import com.example.defectservice.domain.vo.Result;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.service.TokenConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/token-config")
public class TokenConfigController {

    @Autowired
    private TokenConfigService tokenConfigService;

    /**
     * 新增Token配置
     * POST /api/token-config
     */
    @PostMapping
    public Result<TokenConfig> addTokenConfig(@RequestBody TokenConfig tokenConfig) {
        try {
            TokenConfig saved = tokenConfigService.saveTokenConfig(tokenConfig);
            return Result.success(saved);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 校验 Token 是否真实可用
     * GET /api/token-config/validate/{id}
     */
    @GetMapping("/validate/{id}")
    public Result<Boolean> validateToken(@PathVariable Integer id) {
        try {
            boolean isValid = tokenConfigService.validateToken(id);
            if (isValid) {
                return Result.success(Boolean.TRUE);
            } else {
                return Result.error("Token 已失效或权限不足");
            }
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询Token配置
     * GET /api/token-config/{id}
     */
    @GetMapping("/{id}")
    public Result<TokenConfig> getTokenConfig(@PathVariable Integer id) {
        try {
            TokenConfig tokenConfig = tokenConfigService.getTokenConfigById(id);
            return Result.success(tokenConfig);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据平台查询有效Token
     * GET /api/token-config/active/{platform}
     */
    @GetMapping("/active/{platform}")
    public Result<TokenConfig> getActiveToken(@PathVariable String platform) {
        try {
            TokenConfig tokenConfig = tokenConfigService.getActiveTokenByPlatform(platform);
            return Result.success(tokenConfig);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询所有Token配置
     * GET /api/token-config
     */
    @GetMapping
    public Result<List<TokenConfig>> getAllTokenConfigs() {
        List<TokenConfig> list = tokenConfigService.getAllTokenConfigs();
        return Result.success(list);
    }

    /**
     * 修改Token配置
     * PUT /api/token-config
     */
    @PutMapping
    public Result<TokenConfig> updateTokenConfig(@RequestBody TokenConfig tokenConfig) {
        try {
            TokenConfig updated = tokenConfigService.saveTokenConfig(tokenConfig);
            return Result.success(updated);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改Token状态（启用/禁用）
     * PATCH /api/token-config/{id}/status?isActive=true
     */
    @PatchMapping("/{id}/status")
    public Result<TokenConfig> updateTokenStatus(@PathVariable Integer id, @RequestParam Boolean isActive) {
        try {
            TokenConfig updated = tokenConfigService.updateTokenStatus(id, isActive);
            return Result.success(updated);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除Token配置
     * DELETE /api/token-config/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTokenConfig(@PathVariable Integer id) {
        try {
            tokenConfigService.deleteTokenConfigById(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}