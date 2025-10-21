package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") //libera requisições de qualquer origem e qualquer cabeçalho
public class PostagemController {

	@Autowired
	private PostagemRepository postagemRepository;  //injecao de dependencia
	
	@Autowired
	private TemaRepository temaRepository;
	
	 //Lista todas as postagens
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){  
		return ResponseEntity.ok(postagemRepository.findAll());
		
		//findAll é o equivalente a SELECT * FROM tb_postagens;
		
	}
	
	 //Procura por ID
	@GetMapping("/{id}") //para procurar a variavel no endereço, chamada de variavel de caminho
	public ResponseEntity<Postagem> getById(@PathVariable Long id){ //o parametro pega o id do endereço e joga no método para procurar
		return postagemRepository.findById(id) //nao pode retornar ResponseEntity pq ñ pode retornar valor nulo
			.map(resposta -> ResponseEntity.ok(resposta))
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	//findById é o equivalente a SELECT * FROM tb_postagens WHERE id = ?;
	}
	
	//Procura por título
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		
	}
	
	//Cria postagem
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){ //Valid valida algumas regras q estão na Model; RequestBody pega postagem dentro do Body
		if(temaRepository.existsById(postagem.getTema().getId())) {
		postagem.setId(null); //seta o id como nulo para não criar nenhum ID aqui
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
		}
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema não existe", null );
	}
	
	//Atualiza postagem
	@PutMapping 
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		
		if (postagemRepository.existsById(postagem.getId())) {
			
			if (temaRepository.existsById(postagem.getTema().getId())) {
			
			return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
			}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema não existe", null );

	}
	return ResponseEntity.notFound().build();
	}

	//Deleta postagem
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		
		Optional<Postagem> postagem = postagemRepository.findById(id); //verifica se a postagem existe
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); //se nao encontrar a postagem vai mostrar a msg NOT FOUND
		
		postagemRepository.deleteById(id); //se encontrar vai deletar
		
	}
	
}