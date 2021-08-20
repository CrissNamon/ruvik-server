package ru.kpekepsalt.ruvik.dto;

public class ErrorResponseDto<V> extends ResponseDto<V> {

    public ErrorResponseDto(String message) {
        super(message);
    }
}
