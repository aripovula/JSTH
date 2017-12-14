package com.jsthf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsthf.dao.RiskDao;
import com.jsthf.model.Risk;

@Service
@Transactional
public class RiskServiceImpl implements RiskService{
	    @Autowired
	    private RiskDao riskDao;

	    @Override
	    public List<Risk> findAll() {
	        return riskDao.findAll();
	    }

	    @Override
	    public Risk findById(Long id) {
	        return riskDao.getOne(id);
	    }

	    @Override
	    public void save(Risk risk) {
	        riskDao.save(risk);
	    }

	    @Override
	    public void delete(Risk risk) {
	        riskDao.delete(risk);
	    }
	    
	    @Override
	    public List<Risk> findAllRisksAddedByUserAndAdmin(String Name){
	    		return riskDao.findAllRisksAddedByUserAndAdmin(Name);
	    }

	    @Override
	    public List<Risk> findAllRisksAddedByUser(String Name){
	    		return riskDao.findAllRisksAddedByUser(Name);
	    }

	}

