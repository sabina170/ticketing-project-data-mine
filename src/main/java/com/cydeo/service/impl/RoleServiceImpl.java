package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //Controller call this method and requesting all RoleDTO, so it can show in the drop-down in the UI
        //I need to make a call to DB and get all the roles from table:
            // go to repository and find a service(method) which gives me the roles from DB
        //How to call any service here? - DI
        //Convert Role to RoleDto by using modelmapper (before create RoleMapper with the method that i created thatt will make this converting) - it is 3rd party library, it is not coming with the springboot, that's why we should add dependency in pom.xml)

        List<Role> roleList = roleRepository.findAll();
        //return roleList.stream().map(roleMapper::convertToDto).collect(Collectors.toList());

        return roleList.stream().map(role -> mapperUtil.convert(role, new RoleDTO())).collect(Collectors.toList());
        //return roleList.stream().map(role -> mapperUtil.convert(role, RoleDTO.class)).collect(Collectors.toList());
    }


    @Override
    public RoleDTO findById(Long id) {
        return roleMapper.convertToDto(roleRepository.findById(id).get());
    }
}