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
			.contentType("application/json") // (Cabe�alho) Informa��o que ser� enviado um objeto Json
			.body("{ \"name\": \"Jose\", \"age\": 50 }") // Requisi��o enviando objeto Json
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
	
	/* Serializa��o -> Transformar um objeto em formato de byte, texto em 
	 * algo que possa ser armazenado. */ 
	
	/* Deserializa��o -> o processo de pegar um conjunto de bytes e texto e voltar
	 * para um objeto. */
	
	// Serializa��o MAP - (Necess�rio inlcuir a depend�ncia "gson" no arquivo pom)
	
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
			.contentType("application/json") // (Cabe�alho) Informa��o que ser� enviado um objeto Json
			.body("{ \"age\": 50 }") // Requisi��o enviando objeto Json
		.when() 
			// Objeto do tipo post para esse recurso "post"
			.post("https://restapi.wcaquino.me/users")
		.then()		
			.log().all()
			.statusCode(400) // Bad request - falha no registro
			.body("error", is("Name � um atributo obrigat�rio"))
		;		
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML) // Na d�vida do que enviar pode ser enviado um enum 
//			.contentType("application/xml") // (Cabe�alho) Informa��o que ser� enviado um objeto XML
			.body("<user><name>Jose</name><age>50</age></user>") // Requisi��o enviando objeto XML
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
			.contentType("application/json") // (Cabe�alho) Informa��o que ser� enviado um objeto Json
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80 }") // Requisi��o enviando objeto Json
		.when() 
			// Usu�rio que ser� alterado
			.put("https://restapi.wcaquino.me/users/1")
		.then()		
			.log().all()
			.statusCode(200) // Resposta esperada - 200 (sucesso na opera��o)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;		
	}

	/* Formato XML do que ser� enviado no post
	<user> <name>Jose</name> <age>50</age> </user>
	*/

	// http://restapi.wcaquino.me/users/1
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json") // (Cabe�alho) Informa��o que ser� enviado um objeto Json
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80 }") // Requisi��o enviando objeto Json
		.when() 
			// Usu�rio que ser� alterado
			// URL Parametriz�vel - Com par�metros na URL
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()		
			.log().all()
			.statusCode(200) // Resposta esperada - 200 (sucesso na opera��o)
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
			.contentType("application/json") // (Cabe�alho) Informa��o que ser� enviado um objeto Json
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80 }") // Requisi��o enviando objeto Json
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when() 
			// URL Parametriz�vel - Com par�metros no given
			.put("https://restapi.wcaquino.me/{entidade}/{userId}") 
		.then()		
			.log().all()
			.statusCode(200) // Resposta esperada - 200 (sucesso na opera��o)
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



