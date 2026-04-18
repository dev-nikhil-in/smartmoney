package com.nivtron.smartmoney.exception;

import com.nivtron.smartmoney.dto.BaseResponse;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<BaseResponse> handleBadRequest(BadRequestException ex) {
    return ResponseEntity.status(400).body(BaseResponseUtil.error(400, ex.getMessage(), null));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<BaseResponse> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(400).body(BaseResponseUtil.error(400, ex.getMessage(), null));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<BaseResponse> handleUnauthorized(UnauthorizedException ex) {
    return ResponseEntity.status(401).body(BaseResponseUtil.error(401, ex.getMessage(), null));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<BaseResponse> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(403)
        .body(
            BaseResponseUtil.error(403, "You don't have permission to access this resource", null));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<BaseResponse> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(404).body(BaseResponseUtil.error(404, ex.getMessage(), null));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<BaseResponse> handleNoSuchElement(NoSuchElementException ex) {
    return ResponseEntity.status(404).body(BaseResponseUtil.error(404, "Resource not found", null));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .toList();
    return ResponseEntity.status(400)
        .body(BaseResponseUtil.error(400, "Validation failed", errors));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<BaseResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(413)
        .body(BaseResponseUtil.error(413, "File size exceeds maximum allowed limit", null));
  }

  //    @ExceptionHandler(FileUploadException.class)
  //    public ResponseEntity<BaseResponse> handleFileUpload(FileUploadException ex) {
  //        log.error("File upload error: ", ex);
  //        return ResponseEntity.status(500)
  //                .body(BaseResponseUtil.error(500, "Failed to upload file. Please try again.",
  // null));
  //    }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse> handleGeneric(Exception ex) {
    log.error("Unexpected error: ", ex);
    return ResponseEntity.status(500)
        .body(BaseResponseUtil.error(500, ex.getMessage(), List.of(ex.getClass().getName())));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<BaseResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    return ResponseEntity.status(400)
        .body(BaseResponseUtil.error(400, "Invalid request format. Please check your input", null));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<BaseResponse> handleConstraintViolation(ConstraintViolationException ex) {
    List<String> errors =
        ex.getConstraintViolations().stream()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .toList();
    return ResponseEntity.status(400)
        .body(BaseResponseUtil.error(400, "Validation failed", errors));
  }
}
