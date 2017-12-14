package com.jsthf.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jsthf.model.User;

public interface UserService {

	List<User> findAll();

	User findById(Long id);

	void save(User user);

    boolean existsByName(String name);
    
    int idByName(String name);

	void delete(User user);

}
