package br.com.acrossatto.pocvalidation.service.query;

import org.springframework.stereotype.Service;

import br.com.acrossatto.pocvalidation.repository.query.RequestQueryRepository;
import br.com.acrossatto.pocvalidation.validations.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("RequestQueryService")
public class RequestQueryService {

	private RequestQueryRepository repository;

	public void findExistentRequestById(Long requestId) {
		repository.findById(requestId).ifPresent(auth -> {
			throw Message.EXISTENT_REQUEST_IN_BASE.asBusinessException();
		});
	}

}