package com.wbchao.user.controller;

import com.wbchao.user.bean.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import io.swagger.annotations.Api;

@RestController
@Api("User-Service API")
public class UserController {

    private static final String EUREKA_SERVICE_HTTP_URL = "http://User-SERVICE/user";

    @Autowired
    RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    /**
     * Register a new user
     *
     * @param user
     */
    @PostMapping(path = "/user")
    public void addUser(@RequestBody User user) {
        restTemplate.postForLocation(EUREKA_SERVICE_HTTP_URL, user);
    }

    /**
     * Soft delete the user, set active to false
     *
     * @param id
     */
    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable String id) {
        restTemplate.delete(EUREKA_SERVICE_HTTP_URL + "/" + id);
    }

    /**
     * Active the user
     *
     * @param id
     */
    @PutMapping(path = "/user/active/{id}")
    public void activeUser(@PathVariable String id) {
        restTemplate.put(EUREKA_SERVICE_HTTP_URL + "/active/" + id, null);
    }

    /**
     * Update the exist user
     *
     * @param user
     */
    @PutMapping(path = "/user/{id}")
    public void updateUser(@RequestBody User user) {
        restTemplate.put(EUREKA_SERVICE_HTTP_URL + "/" + user.getId(), user);

    }

    /**
     * Find the user by id
     *
     * @param id
     */
    @GetMapping(path = "/user/{id}")
    public User findUserById(@PathVariable String id) {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL + "/" + id, User.class);
    }

    /**
     * Find all users
     */
    @GetMapping(path = "/user")
    public List<User> findAllUsers() {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL, List.class);
    }

    /**
     * Find all active users
     */
    @GetMapping(path = "/user/active")
    public List<User> findActiveUsers() {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL + "/active", List.class);
    }

    /**
     * Find the users by username
     *
     * @param name
     * @return
     */
    @GetMapping(path = "/user/search")
    public List<User> findUsersByName(@RequestParam String name) {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL + "/search", List.class);
    }
}
