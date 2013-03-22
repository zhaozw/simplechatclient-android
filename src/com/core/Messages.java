package com.core;

public class Messages {
    private static Messages instance = new Messages();
    public static synchronized Messages getInstance() {return instance; }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
    
    public void showMessage(String channel, String data)
    {
    }
    
    public void showMessageAll(String data)
    {
    }
}
