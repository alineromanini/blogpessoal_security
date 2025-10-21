package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //Diz ao JPA/Hibernate que a classe Tema é uma entidade, ou seja, será mapeada para uma tabela no banco de dados
@Table(name = "tb_temas")

public class Tema {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY) //auto incremento
	private Long id;  //Hibernate gera o valor; Long corresponde bem ao BIGINT no banco
	
	@Column(length = 200)
	@NotBlank(message = "O atributo descrição é obrigatório!")
	@Size(min = 10, max = 300, message = "O atributo descrição deve conter no mínimo 10 e no máximo 200 caracteres")
	
	private String descricao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) //carregamento de dados preguiçoso
	@JsonIgnoreProperties(value = "tema", allowSetters = true) //allowSetters permite apenas a escrita e ignora a leitura (métodos get)
	private List<Postagem> postagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	
	
	
}
