package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;



public class SchemaTest {

	@Test
	public void deveValidarEsquemaXLM() {
		
		given()
			.log().all()			
		.when()
			.get("http://restapi.wcaquino.me/usersXML")			
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;
		
	}	
		
	/* Uma exce��o ser� lan�ada devido o arquivo/schema est� inv�lido */
		@Test(expected=SAXParseException.class) 
		public void n�oDeveValidarEsquemaXLMInvalido() {
			
			given()
				.log().all()			
			.when()
				.get("http://restapi.wcaquino.me/invalidusersXML")			
			.then()
				.log().all()
				.statusCode(200)
				.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
			;
			
		}	
		
		@Test
		public void deveValidarEsquemaJson() {
			
			given()
				.log().all()			
			.when()
				.get("http://restapi.wcaquino.me/users")			
			.then()
				.log().all()
				.statusCode(200)
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
			;
			
		}			
	
}
