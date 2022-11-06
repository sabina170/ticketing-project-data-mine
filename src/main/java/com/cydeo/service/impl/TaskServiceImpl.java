package com.cydeo.service.impl;


import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
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

            //Spring Boot is creating primary key, since  1st set id that is coming from ui:
            //Find current project in DB:
            Optional<Task> task = taskRepository.findById(dto.getId());

            //Map update projectDto to entity object:
            Task convertedTask = taskMapper.convertToEntity(dto);

          //set status and assigned date to the converted object:
            if(task.isPresent()){
                convertedTask.setTaskStatus(task.get().getTaskStatus());
                convertedTask.setAssignedDate((task.get().getAssignedDate()));
            }

            //save the updated project in the DB:
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
}
