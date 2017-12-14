package com.jsthf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsthf.dao.FCExItemDao;
import com.jsthf.model.FCExItem;

@Service
@Transactional
public class FCExItemServiceImpl implements FCExItemService {
    @Autowired
    private FCExItemDao fcExItemDao;

    @Override
    public List<FCExItem> findAll() {
        return fcExItemDao.findAll();
    }

    @Override
    public FCExItem findById(Long id) {
        return fcExItemDao.getOne(id);
    }

    @Override
    public void save(FCExItem fcExItem){
        fcExItemDao.save(fcExItem);
    }

    @Override
    public void delete(FCExItem fcExItem) {
        fcExItemDao.delete(fcExItem);
    }
    
    @Override
    public List<FCExItem> findAllCardsAddedByUserAndAdmin(String Name){
    		return fcExItemDao.findAllCardsAddedByUserAndAdmin(Name);
    }

    @Override
    public List<FCExItem> findAllCardsAddedByUser(String Name){
    		return fcExItemDao.findAllCardsAddedByUser(Name);
    }

}
