package com.wbchao.user.controller;

import com.wbchao.user.bean.User;
import com.wbchao.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Register a new user
     *
     * @param user
     */
    @PostMapping(path = "/user")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    /**
     * Soft delete the user, set active to false
     *
     * @param id
     */
    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    /**
     * Active the user
     *
     * @param id
     */
    @PutMapping(path = "/user/active/{id}")
    public void activeUser(@PathVariable String id) {
        userService.activeUser(id);
    }

    /**
     * Update the exist user
     *
     * @param user
     */
    @PutMapping(path = "/user/{id}")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    /**
     * Find the user by id
     *
     * @param id
     */
    @GetMapping(path = "/user/{id}")
    public User findUserById(@PathVariable String id) {
        return userService.findUserById(id);
    }

    /**
     * Find all users
     *
     */
    @GetMapping(path = "/user")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * Find all active users
     *
     */
    @GetMapping(path = "/user/active")
    public List<User> findActiveUsers(){
        return  userService.findActiveUsers();
    }

    /**
     * Find the users by username
     *
     * @param name
     * @return
     */
    @GetMapping(path = "/user/search")
    public List<User> findUsersByName(@RequestParam String name){
        return userService.findUsersByName(name);
    }
}
