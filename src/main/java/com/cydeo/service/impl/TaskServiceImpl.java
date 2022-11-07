package com.cydeo.service.impl;


import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Override
    public TaskDTO getById(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if(task.isPresent()){
            return taskMapper.convertToDto(task.get());
        }
        return null;
    }


    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream()
                .map(task->taskMapper.convertToDto(task))
                .collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        taskRepository.save(task);

    }


    @Override
    public void update(TaskDTO dto) {
        
            //Find current task in DB:
            Optional<Task> task = taskRepository.findById(dto.getId());

            //Map update  taskDto to entity object:
            Task convertedTask = taskMapper.convertToEntity(dto);

          //set task status and assigned date to the converted object:
            if(task.isPresent()){
                convertedTask.setTaskStatus(task.get().getTaskStatus());
                convertedTask.setAssignedDate((task.get().getAssignedDate()));
            }

            //save the updated task in the DB:
            taskRepository.save(convertedTask);

        }


    @Override
    public void delete(Long id) {
        //go to DB and get that task with id:
       Optional<Task> foundTask = taskRepository.findById(id);

       //change the isDeleted field to true,
        //save the object in the db
       if(foundTask.isPresent()){
           foundTask.get().setIsDeleted(true);
          taskRepository.save(foundTask.get());
       }

    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);

    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDto) {
        //**fixing bug:convert dto to entity:
        Project project = projectMapper.convertToEntity(projectDto);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(task-> delete(task.getId()));
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        //capture the employee with the security:
        UserDTO loggedInUserDTO = userService.findByUserName("john@employee.com");
        //convert userDto to user entity:
        User user= userMapper.convertToEntity(loggedInUserDTO);
        //go to DB find all the tasks belong to that employee and base on the status of the task:
        List<Task> tasks =taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, user);
        //Map all the tasks belongs to that employee: convert each task to DTO:
        return tasks.stream().map(task-> taskMapper.convertToDto(task)).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        //capture the employee with the security:
        UserDTO loggedInUserDTO = userService.findByUserName("john@employee.com");
        //convert userDto to user entity:
        User user= userMapper.convertToEntity(loggedInUserDTO);
        //go to DB find all the tasks belong to that employee and base on the status of the task:
        List<Task> tasks =taskRepository.findAllByTaskStatusAndAssignedEmployee(status, user);
        //Map all the tasks belongs to that employee: convert each task to DTO:
        return tasks.stream().map(task-> taskMapper.convertToDto(task)).collect(Collectors.toList());
    }
}
