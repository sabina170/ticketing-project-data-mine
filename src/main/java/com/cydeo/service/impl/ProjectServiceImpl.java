package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return projectMapper.convertToDto(projectRepository.findByProjectCode(code));
    }

    @Override
    public List<ProjectDTO> listAllProjects() {

        return projectRepository.findAll().stream()
                .map(project->projectMapper.convertToDto(project))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);

    }

    @Override
    public void update(ProjectDTO dto) {
        //Spring Boot is creating primary key, since  1st set id that is coming from ui:
        //Find current user:
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        //Map update userDto to entity object:
        Project convertedProject = projectMapper.convertToEntity(dto);

        //set id to the converted object:
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());

        //save the updated user in the DB:
        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {
        //go to DB and get that project with projectCode:
        Project project = projectRepository.findByProjectCode(code);
        //change the isDeleted field to true:
        project.setIsDeleted(true);
        //save the object in the db:
        projectRepository.save(project);

    }

    @Override
    public void complete(String projectCode) {
        //go to DB and get that project with projectCode:
        Project project = projectRepository.findByProjectCode(projectCode);
        //change the isDeleted field to true:
        project.setProjectStatus(Status.COMPLETE);
        //save the object in the db:
        projectRepository.save(project);

    }
}
