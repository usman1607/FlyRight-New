package com.flyright.flyright.repository;

import com.flyright.flyright.model.Role;
import com.flyright.flyright.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUsername(String username);
    List<Role> findAllByAccountLockedFalse();
    boolean   existsByUsername(String username);
}
