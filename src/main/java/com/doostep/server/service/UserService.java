package com.doostep.server.service;

import com.doostep.server.model.UserEntity;
import com.doostep.server.model.UserPrincipal;
import com.doostep.server.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    public String createNew(UserEntity user){
        Optional<UserEntity> finder = repo.findByEmail(user.getEmail());
        if(finder.isEmpty()){
            repo.save(user);
            return "New User Created !";
        }else{
            return "User Already Exist !";
        }
    }

    public void update(UserEntity user){
        repo.save(user);
    }

    public List<UserEntity> getAllUsers(){
        return repo.findAll();
    }

    public UserEntity getById(String id){
        return repo.findById(id).orElseThrow(() ->  new RuntimeException("User not Found !"));
    }

    public UserEntity getByEmail(String email){
        return repo.findByEmail(email).orElseThrow(() ->  new RuntimeException("User not Found !"));
    }

    public Boolean remove(String id){
        repo.deleteById(id);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = repo.findByEmail(email);
        if (!user.isPresent()){
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserPrincipal(user);
    }
}
