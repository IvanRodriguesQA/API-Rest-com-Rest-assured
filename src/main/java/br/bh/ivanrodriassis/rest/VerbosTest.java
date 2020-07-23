package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import io.restassured.http.ContentType;

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
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML) // Na dúvida do que enviar pode ser enviado um enum 
//			.contentType("application/xml") // (Cabeçalho) Informação que será enviado um objeto XML
			.body("<user><name>Jose</name><age>50</age></user>") // Requisição enviando objeto XML
		.when() 
			// Objeto do tipo post para esse recurso "post"
			.post("https://restapi.wcaquino.me/usersXML")
		.then()		
			.log().all()
			.statusCode(201) // Resposta esperada - 201 (criado)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		;		
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json") // (Cabeçalho) Informação que será enviado um objeto Json
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80 }") // Requisição enviando objeto Json
		.when() 
			// Usuário que será alterado
			.put("https://restapi.wcaquino.me/users/1")
		.then()		
			.log().all()
			.statusCode(200) // Resposta esperada - 200 (sucesso na operação)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;		
	}

	/* Formato XML do que será enviado no post
	<user> <name>Jose</name> <age>50</age> </user>
	*/

	// http://restapi.wcaquino.me/users/1
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json") // (Cabeçalho) Informação que será enviado um objeto Json
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80 }") // Requisição enviando objeto Json
		.when() 
			// Usuário que será alterado
			// URL Parametrizável - Com parâmetros na URL
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()		
			.log().all()
			.statusCode(200) // Resposta esperada - 200 (sucesso na operação)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;		
	}
	
	@Test
	public void deveCustomizarURLParte2() {
		given()
			.log().all()
			.contentType("application/json") // (Cabeçalho) Informação que será enviado um objeto Json
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80 }") // Requisição enviando objeto Json
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when() 
			// URL Parametrizável - Com parâmetros no given
			.put("https://restapi.wcaquino.me/{entidade}/{userId}") 
		.then()		
			.log().all()
			.statusCode(200) // Resposta esperada - 200 (sucesso na operação)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;		
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
			
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
			
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))			
		;
	}
}



