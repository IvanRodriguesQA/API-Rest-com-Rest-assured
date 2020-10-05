package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
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
	public void deveFazerUploadArquivo() {
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
	
	@Test
	public void naoDeveFazerUploadArquivoGrande() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/Arquivo.rar"))			
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(9000L)) // Define um tempo máximo de resposta da requisição
			.statusCode(200) 
		;
	}	
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		byte[] image = given()
			.log().all()						
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
//			.log().all()
			.statusCode(200)
			.extract().asByteArray();// Ao acionar ctrl 1 a variável byte é criada
		;
		File imagem = new File("src/main/resources/file.jpg"); // Local em que o arquivo será baixado
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
		System.out.println(imagem.length());
		Assert.assertThat(imagem.length(), lessThan(100000L));
	}	
	
}
