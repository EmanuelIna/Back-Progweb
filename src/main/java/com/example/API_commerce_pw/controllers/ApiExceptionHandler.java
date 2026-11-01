package com.example.API_commerce_pw.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e) {
  return ResponseEntity.badRequest().body(Map.of("erro","Dados inválidos","detalhes",e.getBindingResult().getFieldErrors().stream().map(f->f.getField()+": "+f.getDefaultMessage()).collect(java.util.stream.Collectors.joining("; "))));
 }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<Map<String,String>> conflict() { return ResponseEntity.status(409).body(Map.of("erro","Conflito nos dados cadastrados")); }
}
