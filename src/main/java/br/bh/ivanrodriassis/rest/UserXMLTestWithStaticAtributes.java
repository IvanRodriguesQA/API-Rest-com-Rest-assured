package br.bh.ivanrodriassis.rest;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static io.restassured.RestAssured.*;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.internal.path.xml.NodeImpl;



public class UserXMLTestWithStaticAtributes {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 443; // http -> porta 80
//		RestAssured.basePath = "/v2";
	}
	
	@Test
	public void devoTrabalharcomXML () {
		
		given()
			.log().all()
		.when()
			.get("/usersXML/3")
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
			.get("usersXML")
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
	
	@Test
	public void devoFazerPesquisasAvancadasComXMLEJava () {
		ArrayList<NodeImpl> nomes = given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}")			
		;			

		Assert.assertEquals(2, nomes.size());		
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXPath () {
		given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("/users/user[@id='1']"))
			.body(hasXPath("//user[@id='1']"))
			
			// /.. -> sobe um nível  - A partir do filho ter o nome da mãe
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
			
			// A partir da mãe ter o nome do filho
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos",
					allOf(containsString("Zezinho"), containsString("Luizinho")))) // following-sibling -> permite navegar entre os irmãos
			
			// Pega o nome do primeiro registro
			.body(hasXPath("/users/user/name", is("João da Silva")))
			.body(hasXPath("//name", is("João da Silva")))
			
			// Pega o nome do segundo registro
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
			
			//Pega a idade do terceiro registro da lista
			.body(hasXPath("/users/user[3]/age", is("20")))	
			
			// Pega o último registro que existe na lista
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
			
			// Pega pessoas que contém a letra "n" no nome
			.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
			
			// Pega o usuário que a idade seja menor que 24
			.body(hasXPath("user[age < 24]/name", is("Ana Julia")))
			
			// Pega o usuário que tenha mais que 20 e menor que 30 anos 
			.body(hasXPath("user[age > 20 and age < 30]/name", is("Maria Joaquina")))
			
			.body(hasXPath("user[age > 20][age < 30]/name", is("Maria Joaquina")))
		;					
	}
	
}



