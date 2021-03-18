package com.crud.CrudOpts.service;

import com.crud.CrudOpts.Model.JWTResponse;
import com.crud.CrudOpts.Model.UserNameAndPasswordModel;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2")
public class TestServiceForAuth {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  MyUserService myUserService;


  @PostMapping(value = "/authenticate")

  public ResponseEntity getJWTToke(@RequestBody UserNameAndPasswordModel userNameAndPasswordModel) throws Exception {
      System.out.println("inside..."+userNameAndPasswordModel.getPassword());
      try{
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(userNameAndPasswordModel.getUsername(),
                      userNameAndPasswordModel.getPassword()));

          final UserDetails userDetails=
                  myUserService.loadUserByUsername(userNameAndPasswordModel.getUsername());
          System.out.println(userNameAndPasswordModel.getPassword()+"  "+userNameAndPasswordModel.getUsername());

          return ResponseEntity.ok(new JWTResponse(getJWTToken(userDetails)));


      }catch (Exception e)
      {
          throw  new Exception(e);
      }



  }


  private String  getJWTToken(UserDetails userDetails){
Map<String,Object> map=new HashMap<>();
      String token= Jwts.builder()
              .setSubject(userDetails.getUsername())
              .setClaims(map)
              .setIssuedAt(new Date())
              .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(2)))
              .signWith(Keys.hmacShaKeyFor("abcedefghijkllmmewewewewe8389e89r89e8r9e89rddfdf".getBytes()))
              .compact();
      System.out.println(token);
      return  token;
  }

}
