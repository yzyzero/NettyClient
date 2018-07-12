package com.xyd.resource.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="ad_clients")
public class Terminal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5402070720306360620L;

	@Id
	@Column(name="id", length=12)
	private String id;
	
	private String source;
	
	private String targets;
	
	private Boolean startup;

	private String host;

	private Integer port;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getStartup() {
		return startup;
	}

	public void setStartup(Boolean startup) {
		this.startup = startup;
	}
	
}
