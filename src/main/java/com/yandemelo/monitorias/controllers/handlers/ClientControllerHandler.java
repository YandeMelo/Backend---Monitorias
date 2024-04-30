package com.yandemelo.monitorias.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.yandemelo.monitorias.dto.errors.CustomError;
import com.yandemelo.monitorias.dto.errors.ValidationError;
import com.yandemelo.monitorias.exceptions.AlunoCandidatado;
import com.yandemelo.monitorias.exceptions.AlunoNaoCandidatado;
import com.yandemelo.monitorias.exceptions.CursosDiferentes;
import com.yandemelo.monitorias.exceptions.InvalidFileException;
import com.yandemelo.monitorias.exceptions.MonitoriaExistenteException;
import com.yandemelo.monitorias.exceptions.MonitoriaProfessorDiferente;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ClientControllerHandler {
    
    @ExceptionHandler(MonitoriaExistenteException.class)
    public ResponseEntity<CustomError> customName(MonitoriaExistenteException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados Inválidos.", request.getRequestURI());
        for (FieldError f: e.getBindingResult().getFieldErrors()){
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<CustomError> invalidFileException(InvalidFileException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AlunoCandidatado.class)
    public ResponseEntity<CustomError> alunoCandidatado(AlunoCandidatado e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ValidationError err = new ValidationError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AlunoNaoCandidatado.class)
    public ResponseEntity<CustomError> alunoNaoCandidatado(AlunoNaoCandidatado e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    
    @ExceptionHandler(CursosDiferentes.class)
    public ResponseEntity<CustomError> cursosDiferentes(CursosDiferentes e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(MonitoriaProfessorDiferente.class)
    public ResponseEntity<CustomError> monitoriaProfessorDiferente(MonitoriaProfessorDiferente e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
