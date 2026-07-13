package com.kleyton.aprendendo.spring.infrastructure.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String mensagem){
        super(mensagem);
    }

    public ResourceNotFoundException(String mensagem, Throwable throwable){
        super(mensagem, throwable);
    }
}
