package com.yandemelo.monitorias.exceptions;

public class MethodArgumentNotValidException extends RuntimeException {
    
    public MethodArgumentNotValidException (String msg){
        super(msg);
    }

}
