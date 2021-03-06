package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.http.ContentType;

public class EnvioDadosTest {

	@Test
	public void deveEnviarValorViaQuery() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)		
		;		
	}
	
	@Test
	public void deveEnviarValorViaQueryViaParam() {
		given()
			.log().all()
			.queryParam("format", "xml") // Os par�mentros enviados via query n�o causam erro na requisi��o
			.queryParam("outra", "coisa")
		.when()
			.get("http://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			.contentType(Matchers.containsString("utf-8"))
		;		
	}
	
	@Test
	public void deveEnviarValorViaHeader() {
		given()
			.log().all()
			/* accept -> quando eu quero determinar o que ir� vir na resposta / 
			 * .contentType("application/json") -> n�o determina o que vir� na resposta */
			.accept(ContentType.XML) 
		.when()
			.get("http://restapi.wcaquino.me/v2/users") // Quando n�o � enviado nenhum formato � retornado com um html
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)		
		;		
	}
}
