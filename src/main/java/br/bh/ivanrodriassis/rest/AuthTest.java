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
	
	//d15832ada0504de8d0591809159ee364
	
	
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
	
}
