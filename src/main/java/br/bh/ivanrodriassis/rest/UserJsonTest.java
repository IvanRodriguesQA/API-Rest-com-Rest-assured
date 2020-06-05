package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;



public class UserJsonTest {
	
	@Test	
	public void deveVerificarPrimeiroNivel() {
		given() 
		.when() 
			.get("http://restapi.wcaquino.me/users/1")
		.then() 
			.statusCode(200)		
			.body("id", Matchers.is(1))
			.body("name", Matchers.containsString("Silva"))
			.body("age", Matchers.greaterThan(18))
		;
	}	
//	Outras formas de verificar		
	@Test	
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		// path (direciona para json ou xml)
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s","id"));
		
		// jsonpath (direciona diretamente para o json)
		JsonPath jpath = new JsonPath(response.asString()); // response.asString pega o conteúdo da mensagem
		Assert.assertEquals(1, jpath.getInt("id"));
		
		// from -> metódo estático do JsonPath
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(2))
			.body("name", Matchers.containsString("Joaquina"))
			.body("endereco.rua", Matchers.is("Rua dos bobos"))
			.body("endereco.numero", Matchers.is(0))			
		;				
	}
	
	@Test
	public void deveVerificarLista() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(3))
			.body("name", Matchers.containsString("Júlia"))
			.body("filhos", Matchers.hasSize(2)) // Verifica o tamanho da lista
			.body("filhos[0].name", Matchers.is("Zezinho"))
			.body("filhos[1].name", Matchers.is("Luizinho"))
			.body("filhos.name", Matchers.hasItem("Zezinho")) // Verifica se a lista contém o elemento Zezeinho
			.body("filhos.name", Matchers.hasItems("Zezinho","Luizinho"))
		;						
	}
	
	@Test
	public void deveRetornarErroUsuarioInexistente() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", Matchers.is(("Usuário inexistente")))								
		;	
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3)) // "$" indica que está sendo procurado na raiz
			.body("", Matchers.hasSize(3))  // ""  funciona também sem o "$"
			.body("name", Matchers.hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", Matchers.is(25))
			.body("filhos.name", Matchers.hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", Matchers.contains(1234.5678f, 2500, null)) // 1234.5678f -> f indica que o valor é float
		;	
	}
		
}
