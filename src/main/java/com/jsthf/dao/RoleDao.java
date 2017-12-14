package com.jsthf.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jsthf.model.Role;


public interface RoleDao extends JpaRepository<Role, Long>{
	Role findByRole(String role);
}

