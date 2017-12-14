package com.jsthf.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jsthf.model.User;


public interface UserDao extends JpaRepository<User, Long>{
	
	
	 @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM User c WHERE c.username = :Name")
	    boolean existsByName(@Param("Name") String Name);
	 
	 @Query("SELECT id FROM User c WHERE c.username = :Name")
	    int idByName(@Param("Name") String Name);

}

