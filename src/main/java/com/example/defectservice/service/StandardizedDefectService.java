package com.example.defectservice.service;

import com.example.defectservice.domain.entity.StandardizedDefect;
import com.example.defectservice.domain.entity.Repo;
import com.example.defectservice.domain.entity.Project;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.repository.StandardizedDefectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class StandardizedDefectService {

    @Autowired
    private StandardizedDefectRepository defectRepository;

    @Autowired
    private com.example.defectservice.repository.RepoRepository repoRepository;

    @Autowired
    private com.example.defectservice.repository.ProjectRepository projectRepository;

    /**
     * 新增/修改缺陷报告
     */
    public StandardizedDefect saveDefect(StandardizedDefect defect) {
        // 核心非空校验
        if (defect.getRepoId() == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        if (defect.getIssueId() == null || defect.getIssueId().trim().isEmpty()) {
            throw new BusinessException("平台缺陷ID不能为空");
        }
        if (defect.getTitle() == null || defect.getTitle().trim().isEmpty()) {
            throw new BusinessException("缺陷标题不能为空");
        }
        if (defect.getDescription() == null || defect.getDescription().trim().isEmpty()) {
            throw new BusinessException("缺陷描述不能为空");
        }
        if (defect.getUrl() == null || defect.getUrl().trim().isEmpty()) {
            throw new BusinessException("Issue URL不能为空");
        }
        if (defect.getCreatedAt() == null) {
            throw new BusinessException("缺陷发布时间不能为空");
        }
        if (defect.getRecordAt() == null) {
            throw new BusinessException("记录时间不能为空");
        }
        return defectRepository.save(defect);
    }

    /**
     * 根据ID查询缺陷报告
     */
    public StandardizedDefect getDefectById(Integer id) {
        Optional<StandardizedDefect> optional = defectRepository.findById(id);
        return optional.orElseThrow(() -> new BusinessException("未找到ID为" + id + "的缺陷报告"));
    }

    /**
     * 根据仓库ID查询缺陷列表
     */
    public List<StandardizedDefect> getDefectsByRepoId(Integer repoId) {
        List<StandardizedDefect> list = defectRepository.findByRepoId(repoId);
        if (list.isEmpty()) {
            throw new BusinessException("未找到仓库ID为" + repoId + "的缺陷报告");
        }
        return list;
    }

    /**
     * 根据仓库ID和IssueID查询缺陷（唯一）
     */
    public StandardizedDefect getDefectByRepoIdAndIssueId(Integer repoId, String issueId) {
        StandardizedDefect defect = defectRepository.findByRepoIdAndIssueId(repoId, issueId);
        if (defect == null) {
            throw new BusinessException("未找到仓库ID为" + repoId + "、IssueID为" + issueId + "的缺陷报告");
        }
        return defect;
    }

    /**
     * 查询所有缺陷报告
     */
    public List<StandardizedDefect> getAllDefects() {
        return defectRepository.findAll();
    }

    /**
     * 删除缺陷报告
     */
    public void deleteDefectById(Integer id) {
        if (!defectRepository.existsById(id)) {
            throw new BusinessException("要删除的缺陷报告不存在");
        }
        defectRepository.deleteById(id);
    }

    /**
     * 导出缺陷报告时生成标题
     */
    public String generateExportFileName(Integer repoId) {
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new BusinessException("未找到仓库信息"));
        Project project = projectRepository.findById(repo.getProjectId())
                .orElseThrow(() -> new BusinessException("未找到项目信息"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String timeStr = sdf.format(new Date());

        // 格式：缺陷报告_项目标题_仓库平台类型_导出时间
        return String.format("缺陷报告_%s_%s_%s",
                project.getName(),
                repo.getPlatform(),
                timeStr);
    }
}