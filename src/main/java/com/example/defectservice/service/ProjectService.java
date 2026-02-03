package com.example.defectservice.service;

import com.example.defectservice.domain.entity.Project;
import com.example.defectservice.exception.BusinessException;
import com.example.defectservice.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 新增/修改项目
     */
    public Project saveProject(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new BusinessException("项目名称不能为空");
        }
        return projectRepository.save(project);
    }

    public Project updateName(Integer id, Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new BusinessException("项目名称不能为空");
        }
        Project oldProject = projectRepository.findById(id).orElse(null);
        if (oldProject != null) {
            oldProject.setName(project.getName());
            oldProject.setRemark(project.getRemark());
        }else{
            throw new BusinessException("项目id有误，项目id: "+ id);
        }
        return projectRepository.save(oldProject);
    }

    /**
     * 根据ID查询项目
     */
    public Project getProjectById(Integer id) {
        Optional<Project> optional = projectRepository.findById(id);
        return optional.orElseThrow(() -> new BusinessException("未找到ID为" + id + "的项目"));
    }

    /**
     * 根据名称查询项目
     */
    public Project getProjectByName(String name) {
        Project project = projectRepository.findByName(name);
        if (project == null) {
            throw new BusinessException("未找到名称为" + name + "的项目");
        }
        return project;
    }

    /**
     * 查询所有项目
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * 删除项目
     */
    public void deleteProjectById(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new BusinessException("要删除的项目不存在");
        }
        projectRepository.deleteById(id);
    }
}