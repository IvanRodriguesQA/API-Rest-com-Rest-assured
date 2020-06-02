package br.bh.ivanrodriassis.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class OlaMundo {

	public static void main(String[] args) {
		
/*
 * http://www.restapi.wcaquino.me/ola
 * http -> protocolo
 * www.restapi.wcaquino.me -> endereço da api
 * ola -> recurso que deseja acessar do servidor
 */		
		
//		Envio de uma requisição Get / A resposta do método colocado na variável local		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
//		Imprime o corpo do conteúdo da mensagem de resposta
		System.out.println(response.getBody().asString());		
	}
}
