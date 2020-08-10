package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.io.File;

import org.hamcrest.Matchers;
import org.junit.Test;


public class FileTest {

	@Test
	public void deveObrigarEnvioArquivo() {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) // Deveria ser 400
			.body("error", is("Arquivo não enviado"))			
		;
	}
	
	@Test
	public void deveObrigarUploadArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))			
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200) 
			.body("name", is("users.pdf"))			
		;
	}	
	
}
