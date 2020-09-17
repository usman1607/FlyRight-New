package com.flyright.flyright.repository;

import com.flyright.flyright.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Integer> {
    Optional<Role> findByName(String name);
}
