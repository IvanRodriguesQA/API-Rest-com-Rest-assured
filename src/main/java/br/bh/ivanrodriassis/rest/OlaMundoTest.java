package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
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
			.statusCode(200);		
	}	
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(128d, Matchers.greaterThan(120d));
		Assert.assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
//		Assert.assertThat(impares, Matchers.hasSize(5));

//		import static io.restassured.RestAssured.*;
//		Com importação estática (import static org.hamcrest.Matchers.*;)
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, containsInAnyOrder(1,3,9,5,7));
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1,9));
		
// 		Matchers alinhados
		assertThat("Maria", is(not("João")));
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));		
	}
	
	@Test
	public void devoValidarBody() {
		given() 
		.when() 
			.get("http://restapi.wcaquino.me/ola")
		.then() 
			.statusCode(200)		
			.body(is("Ola Mundo!")) // Valida o corpo da mensagem
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
		
}
