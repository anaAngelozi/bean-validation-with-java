package br.com.acrossatto.pocvalidation.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.acrossatto.pocvalidation.dto.Request;
import br.com.acrossatto.pocvalidation.exception.BusinessException;
import br.com.acrossatto.pocvalidation.service.query.RequestQueryService;

@Component
public class RequestValidator implements ConstraintValidator<RequestValid, Request> {

	@Autowired
	private RequestQueryService service;

	@Override
	public boolean isValid(Request value, ConstraintValidatorContext context) {

		try {
		
			service.findExistentRequestById(value.getRequestId());
			
		} catch (BusinessException e) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
			return false;
		}
		return true;
	}

}