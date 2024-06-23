package com.tuskers.backend.jwt.repository;

import com.tuskers.backend.jwt.dto.UserList;
import com.tuskers.backend.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT new com.tuskers.backend.jwt.dto.UserList(u.id, u.username, u.name)"
            + "FROM User u WHERE u.role = 'ADMIN'", nativeQuery = true)
    List<UserList> findAllAdmins();
}
