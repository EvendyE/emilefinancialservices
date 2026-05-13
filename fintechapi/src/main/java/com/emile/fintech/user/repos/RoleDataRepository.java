package com.emile.fintech.user.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emile.fintech.user.model.Role;

public interface RoleDataRepository extends JpaRepository<Role, Long> {

}
