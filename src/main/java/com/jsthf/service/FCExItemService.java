package com.jsthf.service;

import java.util.List;

import com.jsthf.model.FCExItem;

public interface FCExItemService {

	List<FCExItem> findAll();

	FCExItem findById(Long id);

	void save(FCExItem fcExItem);

	void delete(FCExItem fcExItem);

	List<FCExItem> findAllCardsAddedByUserAndAdmin(String Name);
	
	List<FCExItem> findAllCardsAddedByUser(String Name);
}
