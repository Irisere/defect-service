package com.example.defectservice.repository;

import com.example.defectservice.domain.entity.StandardizedDefect;
import com.example.defectservice.domain.vo.StatVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 结构化缺陷报告数据访问层
 */
@Repository
public interface StandardizedDefectRepository extends JpaRepository<StandardizedDefect, Integer> {

    /**
     * 根据仓库ID查询缺陷列表
     */
    List<StandardizedDefect> findByRepoId(Integer repoId);

    /**
     * 根据仓库ID和issueID查询缺陷（联合唯一索引保证唯一）
     */
    StandardizedDefect findByRepoIdAndIssueId(Integer repoId, String issueId);

    //统计相关
    // 1. 按严重程度统计
    @Query("SELECT new com.example.defectservice.domain.vo.StatVO(d.severity, COUNT(d)) " +
            "FROM StandardizedDefect d GROUP BY d.severity")
    List<StatVO> countBySeverity();

    // 2. 按日期统计最近 N 天的趋势 (MySQL语法示例，如果是H2或PG需调整日期函数)
    // 注意：nativeQuery = true 使用原生SQL
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m-%d') as name, COUNT(*) as value " +
            "FROM standardized_defect " +
            "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL :days DAY) " +
            "GROUP BY name ORDER BY name ASC", nativeQuery = true)
    List<Object[]> findTrendLastNDays(@Param("days") int days);

    // 3. 统计Top N 缺陷最多的仓库
    @Query("SELECT new com.example.defectservice.domain.vo.StatVO(r.repoName, COUNT(d)) " +
            "FROM StandardizedDefect d JOIN Repo r ON d.repoId = r.id " +
            "GROUP BY r.repoName ORDER BY COUNT(d) DESC")
    List<StatVO> findTopRepoDefects(Pageable pageable);
}
