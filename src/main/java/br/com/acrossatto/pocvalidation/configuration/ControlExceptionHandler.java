package br.com.acrossatto.pocvalidation.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.acrossatto.pocvalidation.exception.BusinessException;
import br.com.acrossatto.pocvalidation.exception.BusinessException.BusinessExceptionBuilder;
import br.com.acrossatto.pocvalidation.exception.ExceptionResolver;
import br.com.acrossatto.pocvalidation.validations.Message;
import javassist.NotFoundException;

@ControllerAdvice
public class ControlExceptionHandler {

	public static final String X_TRACEID = "X-traceid";
	public static final String CONSTRAINT_VALIDATION_FAILED = "Constraint validation failed";

	@ExceptionHandler(value = { BusinessException.class})
	protected ResponseEntity<Object> handleConflict(BusinessException ex, WebRequest request) {
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	@ExceptionHandler({ Throwable.class })
	public ResponseEntity<Object> handleException(Throwable eThrowable) {

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
				.message(Optional.ofNullable(eThrowable.getMessage()).orElse(eThrowable.toString()))
				.description(ExceptionResolver.getRootException(eThrowable))
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();
		
		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exMethod,
																   WebRequest request) {

		String error = exMethod.getName() + " should be " + exMethod.getRequiredType().getName();

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.BAD_REQUEST)
				.message(CONSTRAINT_VALIDATION_FAILED)
				.description(error)
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	/**
	 *
	 * @param exMethod
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exMethod, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : exMethod.getConstraintViolations()) {
			errors.add(violation.getMessage());
		}

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.BAD_REQUEST)
				.message(CONSTRAINT_VALIDATION_FAILED)
				.description(errors.toString())
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	/**
	 *
	 * @param exMethod
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationError(MethodArgumentNotValidException exMethod) {

		BindingResult bindingResult = exMethod.getBindingResult();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		List<String> fieldErrorDtos = fieldErrors.stream()
				.map(f -> f.getField().concat(":").concat(f.getDefaultMessage())).map(String::new)
				.collect(Collectors.toList());

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.BAD_REQUEST)
				.message(CONSTRAINT_VALIDATION_FAILED)
				.description(fieldErrorDtos.toString())
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	/**
	 *
	 * @param exMethod
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> validationError(HttpMessageNotReadableException exMethod) {

		Throwable mostSpecificCause = exMethod.getMostSpecificCause();
		BusinessExceptionBuilder exBuilder = BusinessException.builder()
			.httpStatusCode(HttpStatus.BAD_REQUEST)
			.message(CONSTRAINT_VALIDATION_FAILED)
			.description(mostSpecificCause.getMessage());
		
		if(mostSpecificCause instanceof InvalidFormatException){
			List<String> fields = new ArrayList<>();
			for (Reference r : ((InvalidFormatException) mostSpecificCause).getPath()) {
				fields.add(r.getFieldName());
			}
			exBuilder.description(Message.INVALID_FORMAT_EXCEPTION.getDescription() + String.join(",", fields))
				.httpStatusCode(Message.INVALID_FORMAT_EXCEPTION.getStatus());
		}

		if(mostSpecificCause instanceof JsonParseException){
			exBuilder.description(Message.JSON_PARSE_EXCEPTION.getDescription())
				.httpStatusCode(Message.JSON_PARSE_EXCEPTION.getStatus());
		} 

		BusinessException ex = exBuilder.build();
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}
	
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<Object> handleException(MissingServletRequestParameterException e) {

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.BAD_REQUEST)
				.message(Optional.ofNullable(e.getMessage()).orElse(e.toString()))
				.description(ExceptionResolver.getRootException(e))
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}
	
	@ExceptionHandler({ NotFoundException.class })
	public ResponseEntity<Object> handleException(NotFoundException e) {

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.NOT_FOUND)
				.message(Optional.ofNullable(e.getMessage()).orElse(e.toString()))
				.description(ExceptionResolver.getRootException(e))
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();
		
		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException  e) {

		BusinessException ex = BusinessException.builder()
				.httpStatusCode(HttpStatus.METHOD_NOT_ALLOWED)
				.message(Optional.ofNullable(e.getMessage()).orElse(e.toString()))
				.description(ExceptionResolver.getRootException(e))
				.build();
		HttpHeaders responseHeaders = new HttpHeaders();

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

}