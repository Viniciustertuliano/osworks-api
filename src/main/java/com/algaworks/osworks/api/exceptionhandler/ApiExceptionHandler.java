package com.algaworks.osworks.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.osworks.domain.exception.NegocioException;

@ControllerAdvice																				//Marcação para identificar como clsse do spring
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{						//superclasse
	
	@Autowired
	private MessageSource messageSource;														//Faz a injeção da mensagem
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {		// Retorna o statos e as mensagem corretas em caso de e-mail repetido.
		var status = HttpStatus.BAD_REQUEST;
		
		var problema = new Problema();
		problema.setStatus(status.value());														//envia Status
		problema.setTitulo(ex.getMessage());													//envia mensagem do erro
		problema.setDataHora(LocalDateTime.now());												//envia data e hora do erro.
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);		//Retorn doso os dados a super classe
		
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		var campos = new ArrayList<Problema.Campo>();
		
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new Problema.Campo(nome, mensagem));
		} 
		
		var problema = new Problema();															//recebe a classe problema
		problema.setStatus(status.value());														//envia Status
		problema.setTitulo("um ou mais campos estão invalidos" 
				+ "faça p preenchimento correto e tente novamente.");							//envia a mensagem de erro 
		problema.setDataHora(LocalDateTime.now());												//envia data e hora do erro
		problema.setCampos(campos);																//envia o campo que apresenta erro.

		return super.handleExceptionInternal(ex, problema, headers, status, request);			//Retorn doso os dados a super classe
	}
	
}
