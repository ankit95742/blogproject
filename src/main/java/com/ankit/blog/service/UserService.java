package com.ankit.blog.service;

import com.ankit.blog.entity.Role;
import com.ankit.blog.entity.User;
import com.ankit.blog.repository.RoleRepository;
import com.ankit.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName("AUTHOR");
        user.setRoles(new HashSet<>(List.of(userRole)));
        userRepository.save(user);
    }

    public String getNameOfUser(String userName){
        return userRepository.getNameByEmail(userName);
    }

    public User getUserByUserName(String username){
        return userRepository.findByEmail(username);
    }

    public Integer getIdOfUserByUserName(String userName){
        return userRepository.getIdByUserName(userName);
    }
}
