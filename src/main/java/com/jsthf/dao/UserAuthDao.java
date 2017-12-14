package com.jsthf.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jsthf.model.UserAuth;


public interface UserAuthDao extends JpaRepository<UserAuth, Long>{
	UserAuth findByEmail(String email);	
}

