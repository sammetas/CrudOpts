package com.crud.CrudOpts.service;

import com.crud.CrudOpts.Model.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/users")
public class CrudService {

    private List<User> USERS= new ArrayList<>();

    public CrudService() {
        User defaultUser= new User();
        defaultUser.setId(suserId.addAndGet(1));
        defaultUser.setName("Default User");
        defaultUser.setEmail("default@email.com");
        this.USERS.add(defaultUser);
    }

    private static  AtomicInteger suserId= new AtomicInteger(0);


     @GetMapping("/")
     public List<User> getAllUser(){
         System.out.println("Inside GET ");
         return USERS;
     }

    @GetMapping(value = "/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable ("userId") String id) throws UserPrincipalNotFoundException {
        System.out.println("GET METHOD::"+id);
        if(id!=null) {
            Integer userId = Integer.parseInt(id);

            return USERS.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst().
                            orElseThrow(() -> new IllegalStateException(userId.toString()+" not found"));
        }

        return new User();
    }

    @PostMapping("/")
    public String createUser(@RequestBody User user){
        System.out.println("POST METHOD::"+user.getName());
        if(user.getName()!=null) {
            user.setId(suserId.addAndGet(1));
            USERS.add(user);
            return  "User Created with "+suserId.toString();
        }else{
            return  "User Name is Mandatory to create User";
        }
    }

    @PutMapping(path = {"/{userId}"})
    public String updateUser(@PathVariable("userId") String userId , @RequestBody User newUser){
        System.out.println("PUT METHOD::"+userId);
         Integer intId=Integer.parseInt(userId);

          if(USERS.stream().anyMatch(u->u.getId().equals(intId))){
              USERS.removeIf(u->u.getId().equals(intId));

              newUser.setId(suserId.addAndGet(1));
              USERS.add(newUser);
          }else{
              return  "User with id "+suserId.toString()+" Created";
          }
        return  "User with id "+userId+" Updated";

    }


    @DeleteMapping(path = {"/{userId}"})
    public String updateUser(@PathVariable("userId") String userId ){
        System.out.println("DELETE method:::"+userId);
        Integer intId=Integer.parseInt(userId);

        if(USERS.stream().anyMatch(u->u.getId().equals(intId))){
            USERS.removeIf(u->u.getId().equals(intId));
            return  "User with id "+userId+" Removed";
           // USERS.add(newUser);
        }else{
            return  "User with id "+userId+" not found";
        }


    }








}
