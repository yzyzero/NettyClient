package com.xyd.resource.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyd.resource.model.Terminal;

public interface TerminalDao extends JpaRepository<Terminal, Integer>{
	
	@Query("from Terminal where id=?1")
	List<Terminal> findById(String Id);
}
