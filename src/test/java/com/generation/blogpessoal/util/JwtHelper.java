package com.generation.blogpessoal.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.UsuarioLogin;

public class JwtHelper {

	private JwtHelper() {} //Método Construtor vazio e privado para impedir instanciação

	public static String obterToken(TestRestTemplate testRestTemplate, String usuario, String senha) { /*Recebe uma requisição HTTP, um usuário e uma senha.
		Serve para */
		
		UsuarioLogin usuarioLogin = TestBuilder.criarUsuarioLogin(usuario, senha);
		
		//Criando a requisição HTTP 
		HttpEntity<UsuarioLogin> requisicao = new HttpEntity<UsuarioLogin>(usuarioLogin);
		
		//Enviar a requisição
		ResponseEntity<UsuarioLogin> resposta = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, requisicao, UsuarioLogin.class);
		
		UsuarioLogin corpoResposta = resposta.getBody();
		
		if(corpoResposta != null && corpoResposta.getToken() != null) {
			return corpoResposta.getToken();
		}
		
		throw new RuntimeException("Falha no login: " + usuario);
	}
	
	public static <T> HttpEntity<T> criarRequisicaoComToken(T corpo, String token){ //T = type pra definir o tipo do método que precisar Ex: tema, postagem, usuário
		HttpHeaders cabecalho = new HttpHeaders();
		String tokenLimpo = token.startsWith("Bearer ") ? token.substring(7): token; //pra pegar depois do sétimo elemento, ou seja, pular a palavra Bearer
		cabecalho.setBearerAuth(tokenLimpo);
		return new HttpEntity<T>(corpo, cabecalho);
	
	}
	
	public static HttpEntity<Void> criarRequisicaoComToken(String token){ //a requisição (void) nao está retornando nada, só o método
		return criarRequisicaoComToken(null, token);
	}
}

