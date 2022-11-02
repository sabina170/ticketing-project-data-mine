package com.cydeo.repository;

import com.cydeo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // not mandatory to put
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByDescription(String description);

}