package com.doostep.server.controller;

import com.doostep.server.model.UserEntity;
import com.doostep.server.model.UserResponseDTO;
import com.doostep.server.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> createUser(@RequestBody UserEntity user){
        Optional<UserEntity> finder = service.getByEmailOptional(user.getEmail());

        if(finder.isEmpty()){
            UserEntity savedUser = service.save(user);

            // Return user object without password
            UserResponseDTO response = new UserResponseDTO(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(409).body("User Already Exist!");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity loginUser) {
        UserEntity user = service.getByEmail(loginUser.getEmail());
        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            UserResponseDTO response = new UserResponseDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );
            return ResponseEntity.ok(response);
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
