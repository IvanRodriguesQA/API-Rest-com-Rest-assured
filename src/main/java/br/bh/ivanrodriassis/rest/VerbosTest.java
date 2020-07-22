package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class VerbosTest {

	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json") // (Cabeçalho) Informação que será enviado um objeto Json
			.body("{ \"name\": \"Jose\", \"age\": 50 }") // Requisição enviando objeto Json
		.when() 
			// Objeto do tipo post para esse recurso "post"
			.post("https://restapi.wcaquino.me/users")
		.then()		
			.log().all()
			.statusCode(201) // Resposta esperada - 201 (criado)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;		
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		
		given()
			.log().all()
			.contentType("application/json") // (Cabeçalho) Informação que será enviado um objeto Json
			.body("{ \"age\": 50 }") // Requisição enviando objeto Json
		.when() 
			// Objeto do tipo post para esse recurso "post"
			.post("https://restapi.wcaquino.me/users")
		.then()		
			.log().all()
			.statusCode(400) // Bad request - falha no registro
			.body("error", is("Name é um atributo obrigatório"))
		;		
	}
	
}





