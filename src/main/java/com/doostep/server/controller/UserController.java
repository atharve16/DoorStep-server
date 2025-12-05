package com.doostep.server.controller;

import com.doostep.server.model.UserEntity;
import com.doostep.server.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<?> allUser(){
        List<UserEntity> lst = service.getAllUsers();
        if(lst.isEmpty()){
            return new ResponseEntity<>("no user found",HttpStatus.OK);
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable String id){
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @PostMapping("/newUser")
    public ResponseEntity<String> createUser(@RequestBody UserEntity user){
        String rsp = service.createNew(user);
        return new ResponseEntity<>(rsp, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity loginUser, HttpSession session) {
        UserEntity user = service.getByEmail(loginUser.getEmail());

        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserEntity user) {
        UserEntity fetchUser = service.getById(user.getId());
        if(fetchUser != null){
            fetchUser.setEmail(user.getName().isEmpty() ? fetchUser.getEmail() : user.getEmail());
            fetchUser.setName(user.getName().isEmpty() ? fetchUser.getName() : user.getName());
            fetchUser.setPassword(user.getPassword().isEmpty() ? fetchUser.getPassword() : user.getPassword());
        }else{
            return ResponseEntity.status(404).body("User Not Found");
        }
        service.update(fetchUser);
        return ResponseEntity.status(201).body("User Details Updated Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable String id){
        if(service.getById(id) != null){
            if(service.remove(id) == true){
                return ResponseEntity.ok("User Account Deleted Successfully !");
            }else{
                return new ResponseEntity<>("Try Again Later !", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
    }

}
