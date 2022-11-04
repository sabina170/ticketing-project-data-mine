package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;

        this.userMapper = userMapper;
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
        //change the isDeleted field to true:
        user.setIsDeleted(true);
        //save the object in the db:
        userRepository.save(user);

    }
}