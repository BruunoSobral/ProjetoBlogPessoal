package org.generation.blogpessoal.controller;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //organizar a ordem das execuções
public class UsuarioControllerTest {
	
	@Autowired
	private UsuarioService usuarioService;
	
	//Somente noteste de controller, pois usamos o padrão rest que utiliza os métodos Http
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar apenas um usuário")
	public void deveCadastrarUmUsuario() {
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Ana Maria", "cozinheira@email.com", "papagaio", "https://i.imgur.com/FETvs2O.jpg"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
		
		}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Claudemir", "claudim@email.com", "123456", "https://i.imgur.com/FETvs2O.jpg"));
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Claudemir", "claudim@email.com", "123456", "https://i.imgur.com/FETvs2O.jpg"));
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Alterar um usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(
				0L, "Louro Jose", "papagaio@email.com", "anamariabraga", "https://i.imgur.com/FETvs2O.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
				"DJ Arlindo Lindo", "comar@email.com", "agrtocomar", "https://i.imgur.com/FETvs28.jpg");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean testeSenha = encoder.matches(usuarioUpdate.getSenha(), resposta.getBody().getSenha());
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
		assertEquals(true, testeSenha);
		assertEquals(usuarioUpdate.getFoto(), resposta.getBody().getFoto());
	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Rodi Nei", "rodi@nei.com", "123456", "https://i.imgur.com/FETvs2O.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Claudemir", "claudim@email.com", "123456", "https://i.imgur.com/FETvs2O.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	}
