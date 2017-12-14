package com.jsthf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsthf.dao.UserDao;
import com.jsthf.model.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(Long id) {
        return userDao.getOne(id);
    }

    @Override
    public void save(User user){
        userDao.save(user);
    }

    @Override
    public boolean existsByName(String name){    
    		return userDao.existsByName(name);
    }

    @Override
    public int idByName(String name){    
    		return userDao.idByName(name);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

}
