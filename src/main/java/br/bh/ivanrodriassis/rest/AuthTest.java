package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

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
		.when() //admin -> usu�rio / senha -> senha
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;		
	}	
	
	@Test // Outra forma de realizar a autentica��o
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
	
	@Test // Outra forma de realizar a autentica��o
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
			.header("Authorization", "JWT " + token) // Enviar um token jwt para a requisi��o
		.when() 
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
//			.body("nome", hasItem("Conta teste"))
		;
	}	
	
	@Test
	public void deveAcessarAplicacaoWeb() {
		
		//login
		String cookie = given()
			.log().all()
			.formParam("email", "teste@api.com")
			.formParam("senha", "api12345")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("https://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie");
		;
		// Retorna o 2� registro ap�s o igual
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie);
				
		//obter conta
		
		String body = given()
			.log().all()			
			.cookie("connect.sid", cookie)
		.when()
			.get("https://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", is("Conta teste"))
			.extract().body().asString();
		;			
		
		System.out.println("-----------------");
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
}
