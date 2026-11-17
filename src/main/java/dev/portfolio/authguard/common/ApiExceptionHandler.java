package dev.portfolio.authguard.common;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(DuplicateEmailException.class)
 ResponseEntity<ProblemDetail> duplicate(DuplicateEmailException e) {
  return ResponseEntity.status(HttpStatus.CONFLICT)
   .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,e.getMessage()));
 }
 @ExceptionHandler(BadCredentialsException.class)
 ResponseEntity<ProblemDetail> badCredentials(BadCredentialsException e) {
  return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
   .body(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,"Credenciais inválidas"));
 }
 @ExceptionHandler(DataIntegrityViolationException.class)
 ResponseEntity<ProblemDetail> dataConflict(DataIntegrityViolationException e) {
  return ResponseEntity.status(HttpStatus.CONFLICT)
   .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,"Conflito de dados"));
 }
 @ExceptionHandler(NoSuchElementException.class)
 ResponseEntity<ProblemDetail> missing(NoSuchElementException e) {
  return ResponseEntity.status(HttpStatus.NOT_FOUND)
   .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,e.getMessage()));
 }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException e) {
  ProblemDetail p=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Campos inválidos");
  Map<String,String> fields=new LinkedHashMap<>();
  e.getBindingResult().getFieldErrors().forEach(err->fields.put(err.getField(),err.getDefaultMessage()));
  p.setProperty("errors",fields);
  return ResponseEntity.badRequest().body(p);
 }
}
