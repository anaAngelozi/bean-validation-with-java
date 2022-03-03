package br.com.acrossatto.pocvalidation.service.command;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.acrossatto.pocvalidation.domain.Request;
import br.com.acrossatto.pocvalidation.repository.command.RequestCommandRepository;
import br.com.acrossatto.pocvalidation.validations.RequestValid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@AllArgsConstructor
@Service("RequestCommandService")
public class RequestCommandService {

	private RequestCommandRepository requestCommandRepository;
	
	public void save(@Valid @RequestValid Request request) {
				
		Request requestEntity = null;
		
		requestCommandRepository.save(requestEntity);

		log.info("method=save request={}", request.getRequestId());
	}
	
}