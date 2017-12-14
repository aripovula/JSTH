package com.jsthf.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jsthf.model.FCExItem;


public interface FCExItemDao extends JpaRepository<FCExItem, Long>{
	
	 @Query(nativeQuery = true, value="SELECT * FROM fcex_item c WHERE c.user = :Name OR c.user = 'adm'")
	    List<FCExItem> findAllCardsAddedByUserAndAdmin(@Param("Name") String Name);

	 @Query(nativeQuery = true, value="SELECT * FROM fcex_item c WHERE c.user = :Name")
	    List<FCExItem> findAllCardsAddedByUser(@Param("Name") String Name);
}

