package com.belkin.finch_backend.api;

import com.belkin.finch_backend.api.dto.ExceptionResponse;
import com.belkin.finch_backend.exception.MyRestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (MyRestException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getStatus(), e.getMessage(), request.getServletPath());
            response.setStatus(e.getStatus().value());
            response.getWriter().write(jsonify(exceptionResponse));
            response.addHeader("Content-Type", "application/json");
        }
        catch (AuthenticationException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getServletPath());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(jsonify(exceptionResponse));
            response.addHeader("Content-Type", "application/json");
        }
        catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getServletPath());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(jsonify(exceptionResponse));
            response.addHeader("Content-Type", "application/json");
        }
    }

    private String jsonify(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
