package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
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
	
	/* Serialização -> Transformar um objeto em formato de byte, texto em 
	 * algo que possa ser armazenado. */ 
	
	/* Deserialização -> o processo de pegar um conjunto de bytes e texto e voltar
	 * para um objeto. */
	
	// Serialização MAP - (Necessário inlcuir a dependência "gson" no arquivo pom)
	
	@Test
	public void deveSalvarUsuarioUsandoMap() {
		
// Map -> como se fosse uma lista mais ele armazena pares
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
				
		given()
			.log().all()
			.contentType("application/json") 
			.body(params)
		.when() 
			.post("https://restapi.wcaquino.me/users")
		.then()		
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25))
		;		
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		
		User user = new User("Usuario via objeto", 35);
			
		given()
			.log().all()
			.contentType("application/json") 
			.body(user)
		.when() 
			.post("https://restapi.wcaquino.me/users")
		.then()		
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))
		;		
	}
		
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario() {
		
		User user = new User("Usuario deserializado", 35);
			
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json") 
			.body(user)
		.when() 
			.post("https://restapi.wcaquino.me/users")
		.then()		
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
		
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



