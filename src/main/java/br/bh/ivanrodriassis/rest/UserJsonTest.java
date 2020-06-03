package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.*;

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
//	Outros formas de verificar		
	@Test	
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		// path (direciona para json ou xml)
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s","id"));
		
		// jsonpath (direciona diretamente para o json)
		JsonPath jpath = new JsonPath(response.asString()); // response.asString pega o conteúdo da mensagem
		Assert.assertEquals(1, jpath.getInt("id"));
		
		// from -> metódo estátido do JsonPath
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
}
