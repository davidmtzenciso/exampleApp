package com.example.app.model.security;

import javax.persistence.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Entity
@Table(name = "authority", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}, name="AUTHORITY_UNIQUE_NAME"))
public class Authority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "bigint unsigned")
	private Integer id;

	@Column(length=20)
	private String name;
	
	public Authority(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
