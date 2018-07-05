package com.xyd.dao;

import com.xyd.model.Terminal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TerminalDao extends JpaRepository<Terminal, Integer>{
	
	@Query("from Terminal where id=?1")
	List<Terminal> findById(String Id);
}
