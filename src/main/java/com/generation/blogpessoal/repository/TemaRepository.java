package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long>{ //interface herda de JpaRepsitory que irá fornecer métodos CRUD sem precisar escrever SQL
	
	public List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
	

}
