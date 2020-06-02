package br.bh.ivanrodriassis.rest;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		
//		Envio de uma requisi��o Get / A resposta do m�todo colocado na vari�vel local		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		
//		Verificar se a resposta � Ola Mundo!
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		
//		Verificar se o staus code retornado � o 200
		Assert.assertTrue(response.statusCode() == 200);
		
		Assert.assertTrue("O status deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(201, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
}