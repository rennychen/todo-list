package com.github.renny.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;

    public JwtInterceptor(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return false;
        }

        String token = authHeader.substring(7);

        try{
            Claims claims = jwtUtils.validateAndParseToken(token);
            String userId = jwtUtils.getUserIdFromClaims(claims);
            request.setAttribute("currentUserId",userId);
            return true;
        } catch (JwtException e){
            sendUnauthorizationResponse(response,e.getMessage());
            return false;
        }
    }

    private void sendUnauthorizationResponse(HttpServletResponse response,String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format("{\"status\":false,\"message\":\"%s\",\"data\":null}",message);
        response.getWriter().write(json);
    }
}
