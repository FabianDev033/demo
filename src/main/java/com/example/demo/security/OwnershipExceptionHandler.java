package com.example.demo.security;
import org.springframework.http.HttpStatus; import org.springframework.http.ProblemDetail; import org.springframework.web.bind.annotation.ExceptionHandler; import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice public class OwnershipExceptionHandler { @ExceptionHandler(RecursoNoAutorizadoException.class) ProblemDetail forbidden(RecursoNoAutorizadoException e){return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,e.getMessage());} }
