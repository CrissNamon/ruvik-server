package ru.kpekepsalt.ruvik.dto;

public class Response<V> {

    private String message;
    private V response;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, V response)
    {
        this.message = message;
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public V getResponse() {
        return response;
    }
}
