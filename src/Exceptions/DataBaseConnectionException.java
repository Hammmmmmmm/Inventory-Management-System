package Exceptions;

import java.sql.SQLException;

public class DataBaseConnectionException extends Exception{
    public DataBaseConnectionException(String message) {super(message);}
    public DataBaseConnectionException(String message, Exception ex) {super(message, ex);}
}