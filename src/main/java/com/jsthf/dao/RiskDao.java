package com.jsthf.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jsthf.model.Risk;

public interface RiskDao extends JpaRepository<Risk, Long>{
	
	 @Query(nativeQuery = true, value="SELECT * FROM risk c WHERE c.user = :Name OR c.user = 'adm'")
	    List<Risk> findAllRisksAddedByUserAndAdmin(@Param("Name") String Name);

	 @Query(nativeQuery = true, value="SELECT * FROM risk c WHERE c.user = :Name")
	    List<Risk> findAllRisksAddedByUser(@Param("Name") String Name);

}
