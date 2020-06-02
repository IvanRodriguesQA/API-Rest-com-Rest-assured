package br.bh.ivanrodriassis.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

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
		
//		Imprime o corpo do conte�do da mensagem de resposta / verificar se a resposta � Ola Mundo!
		System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		
//		Imprime o status code / == verificar se o staus code retornado � o 200
		System.out.println(response.statusCode() == 200);
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
}
