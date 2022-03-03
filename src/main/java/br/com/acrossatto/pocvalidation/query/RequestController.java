package br.com.acrossatto.pocvalidation.query;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acrossatto.pocvalidation.domain.Request;
import br.com.acrossatto.pocvalidation.service.command.RequestCommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Api(value = "/requests", tags = "[Requisição]", description = "Operação responsável por consultar dados de uma requisição")
@RequestMapping("/requests")
public class RequestController {
	
	private RequestCommandService requestCommandService;
	
	@ApiOperation(value = "Inclui uma requisição", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Requisição salva com sucesso"),
			@ApiResponse(code = 400, message = "Requisição Inválida"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 500, message = "Erro desconhecido") })
	@PostMapping
	public ResponseEntity<Void> authorizations(@RequestBody Request request) {
		requestCommandService.save(request);
		return ResponseEntity.noContent().build();
	}

}