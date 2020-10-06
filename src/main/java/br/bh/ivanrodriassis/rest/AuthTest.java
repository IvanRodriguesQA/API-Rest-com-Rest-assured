package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

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
}
