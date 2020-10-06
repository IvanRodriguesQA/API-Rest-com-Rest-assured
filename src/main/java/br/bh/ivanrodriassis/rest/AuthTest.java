package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

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
	
	@Test
	public void deveObterClima() {
		
		given()
			.log().all()
			.queryParam("q", "Belo Horizonte,BR")
			.queryParam("appid", "d15832ada0504de8d0591809159ee364")
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)	
			.body("name", is("Belo Horizonte"))
			.body("coord.lon", is(-43.94f))
			.body("main.temp", is(27))			
	;			
	}	
	
	@Test
	public void naoDeveAcessarSemSenha() {
		
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;		
	}	
	
	@Test
	public void deveFazerAutenticacaoBasica() {
		
		given()
			.log().all()
		.when() //admin -> usuário / senha -> senha
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;		
	}	
	
	@Test // Outra forma de realizar a autenticação
	public void deveFazerAutenticacaoBasica2() {
		
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when() 
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;		
	}
	
	@Test // Outra forma de realizar a autenticação
	public void deveFazerAutenticacaoBasicaChallenge() {
		
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when() 
			.get("http://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;		
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT () {
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "teste@api.com");
		login.put("senha", "api12345");
		
		// Login na api
		// Receber token
		String token = given() 
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when() 
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");
	;
	
		// Obter as contas
		given() 
			.log().all()	
			.header("Authorization", "JWT " + token) // Enviar um token jwt para a requisição
		.when() 
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
//			.body("nome", hasItem("Conta teste"))
		;
	}	
	
}
