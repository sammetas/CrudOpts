package com.crud.CrudOpts.security;

import com.crud.CrudOpts.Model.UserNameAndPasswordModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
private  AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        System.out.println("JWTAuth constructor");
        this.authenticationManager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("Method :: attemptAuthentication");
         try {
             UserNameAndPasswordModel userNameAndPasswordModel = new ObjectMapper().readValue(request.getInputStream(), UserNameAndPasswordModel.class);

             Authentication authentication =
                     new UsernamePasswordAuthenticationToken(userNameAndPasswordModel.getUsername(), userNameAndPasswordModel.getPassword());

             Authentication   authenticate=   authenticationManager.authenticate(authentication);
             return authenticate;


         }catch (IOException e){
             throw  new RuntimeException(e);
         }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("Method::successfulAuthentication");


        String token=Jwts.builder()
                         .setSubject(authResult.getName())
                         .claim("authorities",authResult.getAuthorities())
                         .setIssuedAt(new Date())
                         .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(2)))
                         .signWith(Keys.hmacShaKeyFor("abcedefghijkllmmewewewewe8389e89r89e8r9e89rddfdf".getBytes()))
                         .compact();

        response.addHeader("Authorization","Bear "+token);
        System.out.println("token  "+token);


    }
}
