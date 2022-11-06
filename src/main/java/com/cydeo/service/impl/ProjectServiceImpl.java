package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
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
        //it is for project list table in ui, we should set the status before saving, because in create project form we in ui we dont have status field, but in list of project table we have status field.
        // After saving new project it needs to be come to that project list with the OPEN status.
        // otherwise we will get ean error "property or field "value" cannot be found on null
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);

    }

    @Override
    public void update(ProjectDTO dto) {
        //Spring Boot is creating primary key, since  1st set id that is coming from ui:
        //Find current project in DB:
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        //Map update projectDto to entity object:
        Project convertedProject = projectMapper.convertToEntity(dto);

        //set id to the converted object:
        convertedProject.setId(project.getId());
        //set status to the converted object:
        convertedProject.setProjectStatus(project.getProjectStatus());

        //save the updated project in the DB:
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

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        UserDTO currentUSerDTO = userService.findByUserName("harold@manager.com");
        User user= userMapper.convertToEntity(currentUSerDTO);
        List<Project> list =projectRepository.findAllByAssignedManager(user);
        return list.stream().map(project->{
                                    ProjectDTO obj = projectMapper.convertToDto(project);
                                    obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
                                    obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
                                    return obj;
                                    
        }).collect(Collectors.toList());

    }
}
