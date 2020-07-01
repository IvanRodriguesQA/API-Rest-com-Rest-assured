package br.bh.ivanrodriassis.rest;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;



public class UserJsonTest {
	
	@Test	
	public void deveVerificarPrimeiroNivel() {
		given() 
		.when() 
			.get("http://restapi.wcaquino.me/users/1")
		.then() 
			.statusCode(200)		
			.body("id", Matchers.is(1))
			.body("name", Matchers.containsString("Silva"))
			.body("age", Matchers.greaterThan(18))
		;
	}	
//	Outras formas de verificar		
	@Test	
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		// path (direciona para json ou xml)
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s","id"));
		
		// jsonpath (direciona diretamente para o json)
		JsonPath jpath = new JsonPath(response.asString()); // response.asString pega o conteúdo da mensagem
		Assert.assertEquals(1, jpath.getInt("id"));
		
		// from -> metódo estático do JsonPath
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(2))
			.body("name", Matchers.containsString("Joaquina"))
			.body("endereco.rua", Matchers.is("Rua dos bobos"))
			.body("endereco.numero", Matchers.is(0))			
		;				
	}
	
	@Test
	public void deveVerificarLista() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(3))
			.body("name", Matchers.containsString("Júlia"))
			.body("filhos", Matchers.hasSize(2)) // Verifica o tamanho da lista
			.body("filhos[0].name", Matchers.is("Zezinho"))
			.body("filhos[1].name", Matchers.is("Luizinho"))
			.body("filhos.name", Matchers.hasItem("Zezinho")) // Verifica se a lista contém o elemento Zezinho
			.body("filhos.name", Matchers.hasItems("Zezinho","Luizinho"))
		;						
	}
	
	@Test
	public void deveRetornarErroUsuarioInexistente() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", Matchers.is(("Usuário inexistente")))								
		;	
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3)) // "$" indica que está sendo procurado na raiz
			.body("", Matchers.hasSize(3))  // ""  funciona também sem o "$"
			.body("name", Matchers.hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", Matchers.is(25))
			.body("filhos.name", Matchers.hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", Matchers.contains(1234.5678f, 2500, null)) // 1234.5678f -> f indica que o valor é float
		;	
	}
	
	@Test
	public void devoFazerVerificacoesAvancadas() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3))
			
/*			Método que itera em cima das idades "findAll"
            Objeto "it" instância atual da idade
			Irá percorrer todas as idades que referencia a idade atual do objeto it  
			Usuário com idade até 25 anos */
			
			.body("age.findAll{it <= 25}.size()", Matchers.is(2))  // size() verifica o tamanho da coleção
			
			/* Quantidade de usuários com mais de 20 anos e até 25 */
			.body("age.findAll{it <= 25 && it > 20}.size()", Matchers.is(1))
			
			/* Nome do usuário com mais de 20 anos e até 25  */
			/* findAll -> verifica a partir do início da raiz */
			
			.body("findAll{it.age <= 25 && it.age > 20}.name", Matchers.hasItem("Maria Joaquina"))  
			
			/* Retorna o primeiro usuário da lista com idade até 25 */
			.body("findAll{it.age <= 25}[0].name", Matchers.is("Maria Joaquina"))
			
			/* Retorna o último usuário da lista com idade até 25 */
			.body("findAll{it.age <= 25}[-1].name", Matchers.is("Ana Júlia")) // -1 começa a navegar do último registro da lista
			
			/* Retorna apena um elemento, o primeiro usuário da lista com idade até 25 */
			.body("find{it.age <= 25}.name", Matchers.is("Maria Joaquina"))
			
			/* Todos os elementos cujo nome contém o "n" */
			.body("findAll{it.name.contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Júlia"))
			
			/* Todos os elementos que possuem mais de 10 caracteres */
			.body("findAll{it.name.length() > 10}.name", Matchers.hasItems("João da Silva", "Maria Joaquina"))
			
			/* Alterar as letras do nome para maiúscula e retorna o nome */
			.body("name.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))
			
			/* Retorna o nome de usuário que começa com Maria convertido para maiúsculo e retorna o nome encontrado */
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", 
					Matchers.hasItem("MARIA JOAQUINA"))
			
			/* Retorna o nome de usuário que começa com Maria convertido para maiúsculo e retorna o nome encontrado */
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", 
					Matchers.allOf(Matchers.arrayContaining("MARIA JOAQUINA"), Matchers.arrayWithSize((1))))
		
			/* Múltiplica a idade por 2 e retorna a idade múltiplicada */
			.body("age.collect{it*2}", Matchers.hasItems(60, 50, 40))
			
			/* Retorna o maior id da coleção */
			.body("id.max()", Matchers.is(3))
			
			/* Retorna o menor salário da coleção */
			.body("salary.min()", Matchers.is(1234.5678f))
			
			/* Retorna a soma do salário da coleção */
			.body("salary.findAll{it != null}.sum()", Matchers.is(Matchers.closeTo(3734.5678f, 0.001)))
			
			/* Outra forma - Retorna a soma do salário da coleção */
			.body("salary.findAll{it != null}.sum()", Matchers.allOf(Matchers.greaterThan(3000d),
					Matchers.lessThan(5000d)))			
		;		
	}
	
	@Test
	public void devoUnirJsonPathComJava() {
		ArrayList <String> names = 
			given()
			.when()
				.get("http://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.extract().path("name.findAll{it.startsWith('Maria')}");			
			;	
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("mAriA Joaquina"));
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}
	
}
