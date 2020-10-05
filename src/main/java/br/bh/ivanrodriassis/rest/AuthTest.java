package br.bh.ivanrodriassis.rest;

import static org.hamcrest.Matchers.is;

import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AuthTest {

	@Test
	public void deveAcessarSWAPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;		
		
	} 
}
