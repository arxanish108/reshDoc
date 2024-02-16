package com.generateToken.generateToken.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
  private T data;
  private int statusCode;
  private String message;

  public ApiResponse(T data) {
    this.data = data;
    this.statusCode = 200;
    this.message = "Successfully registered"; // Default success status code
  }

  public ApiResponse(T data, int statusCode, String message) {
    this.data = data;
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }

}
