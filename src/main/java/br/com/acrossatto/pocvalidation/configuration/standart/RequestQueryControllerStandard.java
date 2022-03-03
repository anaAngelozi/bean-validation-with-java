package br.com.acrossatto.pocvalidation.configuration.standart;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Requisição encontrada com sucesso"),
		@ApiResponse(code = 400, message = "Requisição Inválida"),
		@ApiResponse(code = 404, message = "Requisição não encontrada"),
		@ApiResponse(code = 500, message = "Erro desconhecido") })
@ResponseStatus(HttpStatus.OK)
public @interface RequestQueryControllerStandard {
}