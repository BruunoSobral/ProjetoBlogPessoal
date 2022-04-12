package org.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start () {
		usuarioRepository.save(new Usuario(0L, "DJ Arlindo Feio", "semar@email.com", "perdioar", "https://i.imgur.com/FETvs2O.jpg"));
		usuarioRepository.save(new Usuario(0L, "DJ Cleiton Rasta","cleitinho@pedra.com","cabecadegelo","https://i.imgur.com/FETvs2O.jpg"));
		usuarioRepository.save(new Usuario(0L, "Ana Aninha", "anao@email.com", "aindasouana", "https://i.imgur.com/FETvs2O.jpg"));
		usuarioRepository.save(new Usuario(0L, "Anitta", "rebolomesmo@email.com", "sorebolo", "https://i.imgur.com/FETvs2O.jpg"));
		usuarioRepository.save(new Usuario(0L, "Louro Jose", "papagaio@email.com", "anamariabraga", "https://i.imgur.com/FETvs2O.jpg"));
	}
	
	@Test
	@DisplayName("Retorna apenas um usuário")
	public void deveRetornarUmUsuario() {
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("papagaio@email.com");
		assertTrue(usuario.get().getUsuario().equals("papagaio@email.com"));
	}
	
	@Test
	@DisplayName("Retorna dois usuários")
	public void deveRetornarDoisUsuarios() {
		
		//findAllByNomeContainingIgnoreCase
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("DJ");
		assertEquals(2, listaDeUsuarios.size());
		
		assertTrue(listaDeUsuarios.get(0).getNome().equals("DJ Arlindo Feio"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("DJ Cleiton Rasta"));
	}

	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}

}
