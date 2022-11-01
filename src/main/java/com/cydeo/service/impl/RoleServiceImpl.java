package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //Controller call me and requesting all RoleDTO, so it can show in the drop-down in the UI
        //I need to make a call to DB and get all the roles from table
        //Go to repository and find a service which gives me the roles from DB
        //How to call any service here? - DI
        //convert Role to RoleDto by using modelmapper (add dependency in pom.xml)
        return roleRepository.findAll();
    }

    @Override
    public RoleDTO findById(Long id) {
        return null;
    }
}