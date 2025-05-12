package com.perucontrols.techdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;

    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(true, "Operación exitosa", data, null);
    }

    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data, null);
    }

    public static <T> ApiResponseDto<T> error(String error) {
        return new ApiResponseDto<>(false, null, null, error);
    }

    public static <T> ApiResponseDto<T> error(String message, String error) {
        return new ApiResponseDto<>(false, message, null, error);
    }
}
