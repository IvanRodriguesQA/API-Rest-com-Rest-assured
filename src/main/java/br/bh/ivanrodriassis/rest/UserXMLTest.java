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
	
	@Test
	public void devoFazerPesquisasAvancadasComXML () {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1","2","3"))
			.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name",
					hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger() * 2}", hasItems(40, 50, 60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString()."
					+ "toUpperCase()}", is("MARIA JOAQUINA"))			
		;			
		
	}
	
}



