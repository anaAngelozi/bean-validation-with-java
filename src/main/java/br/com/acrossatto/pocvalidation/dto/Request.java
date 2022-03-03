package br.com.acrossatto.pocvalidation.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Request implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long requestId;
	
	private Long transactionId;
	
	private Long accountId;
	
    @Getter
    @CPF(message = "O campo 'cpf' está inválido")
	private final String cpf;
	
	private Long channelId;

}