package com.ponsak.autoandgeneral.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ponsak.autoandgeneral.demo.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
