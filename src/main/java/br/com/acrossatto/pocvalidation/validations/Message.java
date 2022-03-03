package br.com.acrossatto.pocvalidation.validations;

import org.springframework.http.HttpStatus;

import br.com.acrossatto.pocvalidation.exception.BusinessException;

public enum Message {
	
	NOT_FOUND("Não encontrado(a)", HttpStatus.NOT_FOUND),
	INTEGRITY_RESTRICTION("Restrição de integridade, chave-mãe não localizada",HttpStatus.BAD_REQUEST),
	REQUEST_EXISTED("Solicitação já foi registrada",HttpStatus.BAD_REQUEST),
	REQUEST_NOT_FOUND("Solicitação não encontrada", HttpStatus.BAD_REQUEST),
	OPERATION_NOT_AVAILABLE("Operação recebida do rabbit não implementada", HttpStatus.INTERNAL_SERVER_ERROR),
	EXISTENT_REQUEST_IN_BASE("Essa requisição já existe!", HttpStatus.BAD_REQUEST),
	JSON_PARSE_EXCEPTION("Constraint validation failed", "Verifique o corpo da requisição, ouve um erro na conversão de dados!", HttpStatus.BAD_REQUEST),
	INVALID_FORMAT_EXCEPTION("Constraint validation failed", "Verifique o corpo da requisição, ouve um erro no(s) campo(s): ", HttpStatus.BAD_REQUEST);

	private String value;
	private String description;
	private HttpStatus statusCode;

	private Message(String value, String description, HttpStatus statusCode) {
		this.value = value;
		this.description = description;
		this.statusCode = statusCode;
	}

	private Message(String value, HttpStatus statusCode) {
		this.value = value;
		this.statusCode = statusCode;
	}
	
	private Message(String value) {
		this.value = value;
	}

	public String getMessage() {
		return this.value;
	}

	
	public HttpStatus getStatus() {
		return this.statusCode;
	}

	public String getDescription() {
		return description;
	}

	public BusinessException asBusinessException() {
		return BusinessException.builder()
				.httpStatusCode(this.getStatus())
				.code(String.valueOf(this.getStatus().value()))
				.message(this.getMessage())
				.description(this.getDescription()).build();
	}

}