package com.exception;

public class GameAccountInUseException  extends  RuntimeException{
    public GameAccountInUseException(String message){
        super(message);
    }
}
