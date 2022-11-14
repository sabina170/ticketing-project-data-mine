package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userRepository.findAll(Sort.by("firstName")).stream()
                    .map(user->userMapper.convertToDto(user)).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDto(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));

    }

    @Override
    public void deleteByUserName(String username) {

        userRepository.deleteByUserName(username);
    }

    @Override
    public UserDTO update(UserDTO user) {
        //Spring Boot is creating primary key, since  1st set id that is coming from ui:
        //Find current user:
        User user1 = userRepository.findByUserName(user.getUserName());

        //Map update userDto to entity object:
        User convertedUser = userMapper.convertToEntity(user);

        //set id to the converted object:
        convertedUser.setId(user1.getId());

        //save the updated user in the DB:
        userRepository.save(convertedUser);

        return findByUserName(user.getUserName()); // return user;

    }

    @Override
    public void delete(String username) {
        //go to DB and get that user with userName:
        User user = userRepository.findByUserName(username);
        //check if the user cannot be deleted:
        if(checkIfUserCanBeDeleted(user)) {
            //change the isDeleted field to true:
            user.setIsDeleted(true);
            //save the object in the db:
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        return userRepository.findByRoleDescriptionIgnoreCase(role).stream()
                .map(user-> userMapper.convertToDto(user))
                .collect(Collectors.toList());
    }


    //**fixing bug: we create this method, that we use before delete() method
    private boolean checkIfUserCanBeDeleted(User user) {
        //go to DB and check if the user is manager or employee:
        switch(user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
                return taskDTOList.size() == 0;
            default:
                return true;
        }

    }
}