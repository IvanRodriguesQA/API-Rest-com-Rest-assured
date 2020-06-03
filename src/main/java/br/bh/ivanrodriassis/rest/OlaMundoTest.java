package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.*;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		
//		Envio de uma requisição Get / A resposta do método colocado na variável local		
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		
//		Verificar se a resposta é Ola Mundo!
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		
//		Verificar se o staus code retornado é o 200
		Assert.assertTrue(response.statusCode() == 200);
		
		Assert.assertTrue("O status deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrosFormasRestAssured() {
//		Envio de uma requisição Get / A resposta do método colocado na variável local		
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		given() // Pré-condição -> formas de envio da requisição
		.when() // Ação que será testada
			.get("http://restapi.wcaquino.me/ola")
		.then() // Verificações do resultado
//			.assertThat();
			.statusCode(201);
			
		
	}
	
	
	
}
