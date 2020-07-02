package br.bh.ivanrodriassis.rest;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

import org.junit.Test;

public class UserXMLTest {

	@Test
	public void devoTrabalharcomXML () {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			
			.rootPath("user")
//			.body("user.name", is("Ana Julia"))	-> sem utilizar o nó raiz
			.body("name", is("Ana Julia"))
			.body("@id", is("3"))
			
			.rootPath("user.filhos")
//			.body("filhos.name.size()", is(2)) -> Sem utilizar o nó raiz
			.body("name.size()", is(2))
			
			.detachRootPath("filhos") // retira o rootPath (necessário informar filhos.)
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			
			.appendRootPath("filhos") // adicona o rootPath (não é informado filhos.)
			.body("name", contains("Zezinho", "Luizinho"))
			.body("name", hasItems("Zezinho", "Luizinho"))
		;
	}
}
