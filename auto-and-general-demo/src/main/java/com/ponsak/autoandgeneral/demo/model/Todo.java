package com.ponsak.autoandgeneral.demo.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.annotation.PersistenceConstructor;


@Entity
public class Todo {

	public Todo() {
		
	}
	
    public Todo(Long id, String text, boolean isCompleted, String createdAt) {
		this.id = id;
		this.text = text;
		this.isCompleted = isCompleted;
		this.createdAt = createdAt;
	}
    
	@Id
    @GeneratedValue
    private Long id;
    
	private String text;
    private boolean isCompleted;
    private String createdAt;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}
