package com.jsthf.service;

import java.util.List;

import com.jsthf.model.Risk;

public interface RiskService {

	List<Risk> findAll();

	Risk findById(Long id);

	void save(Risk risk);

	void delete(Risk risk);

	List<Risk> findAllRisksAddedByUserAndAdmin(String Name);
	
	List<Risk> findAllRisksAddedByUser(String Name);
}
