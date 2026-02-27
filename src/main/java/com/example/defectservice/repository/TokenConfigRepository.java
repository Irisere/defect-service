package com.example.defectservice.repository;

import com.example.defectservice.domain.entity.TokenConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Token配置数据访问层
 */
@Repository
public interface TokenConfigRepository extends JpaRepository<TokenConfig, Integer> {

    /**
     * 根据平台查询有效Token
     */
    TokenConfig findByPlatformAndIsActive(String platform, Boolean isActive);

    TokenConfig findByPlatform(String platform);

    @Modifying
    @Query("UPDATE TokenConfig t SET t.isActive = false WHERE t.platform = :platform AND t.id != :id")
    void deactivateOtherTokens(@Param("platform") String platform, @Param("id") Integer id);
}