package ru.kpekepsalt.ruvik.dto;

public class ResponseDto<V> {

    private String message;
    private V response;

    public ResponseDto(String message) {
        this.message = message;
    }

    public ResponseDto(String message, V response)
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
