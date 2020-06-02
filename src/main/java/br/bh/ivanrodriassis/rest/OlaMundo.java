package br.bh.ivanrodriassis.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class OlaMundo {

	public static void main(String[] args) {
		
/*
 * http://www.restapi.wcaquino.me/ola
 * http -> protocolo
 * www.restapi.wcaquino.me -> endere�o da api
 * ola -> recurso que deseja acessar do servidor
 */		
		
//		Envio de uma requisi��o Get / A resposta do m�todo colocado na vari�vel local		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
//		Imprime o corpo do conte�do da mensagem de resposta
		System.out.println(response.getBody().asString());		
	}
}
