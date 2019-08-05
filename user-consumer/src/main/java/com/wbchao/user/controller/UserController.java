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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
    @ApiOperation(value = "RegisterUser", notes = "Register a new user with username, age and email")
    @ApiImplicitParam(name = "user", value = "User entity include username, age and email", dataType = "User")
    @PostMapping(path = "/user")
    public void addUser(@RequestBody User user) {
        restTemplate.postForLocation(EUREKA_SERVICE_HTTP_URL, user);
    }

    /**
     * Soft delete the user, set active to false
     *
     * @param id
     */
    @ApiOperation(value = "DeleteUser", notes = "Delete a the user with id")
    @ApiImplicitParam(name = "id", value = "The user id", dataType = "String", required = true, paramType = "path")
    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable String id) {
        restTemplate.delete(EUREKA_SERVICE_HTTP_URL + "/" + id);
    }

    /**
     * Active the user
     *
     * @param id
     */
    @ApiOperation(value = "ActiveUser", notes = "Active the deleted user with id")
    @ApiImplicitParam(name = "id", value = "The user id", dataType = "String", required = true, paramType = "path")
    @PutMapping(path = "/user/active/{id}")
    public void activeUser(@PathVariable String id) {
        restTemplate.put(EUREKA_SERVICE_HTTP_URL + "/active/" + id, null);
    }

    /**
     * Update the exist user
     *
     * @param user
     */
    @ApiOperation(value = "UpdateUser", notes = "Update the user with id")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "User id", required = true, dataType = "String", paramType = "path"), @ApiImplicitParam(
                    name = "user", value = "User entity", required = true, dataType = "User")})
    @PutMapping(path = "/user/{id}")
    public void updateUser(@RequestBody User user) {
        restTemplate.put(EUREKA_SERVICE_HTTP_URL + "/" + user.getId(), user);

    }

    /**
     * Find the user by id
     *
     * @param id
     */
    @ApiOperation(value = "FindUser", notes = "Find the single user with id")
    @ApiImplicitParam(name = "id", value = "User id", required = true, dataType = "String", paramType = "path")
    @GetMapping(path = "/user/{id}")
    public User findUserById(@PathVariable String id) {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL + "/" + id, User.class);
    }

    /**
     * Find all users
     */
    @ApiOperation(value = "FindAllUser", notes = "Find all users")
    @GetMapping(path = "/user")
    public List<User> findAllUsers() {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL, List.class);
    }

    /**
     * Find all active users
     */
    @ApiOperation(value = "FindActiveUser", notes = "Find the active users")
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
    @ApiOperation(value = "SearchUser", notes = "Search the users with username keyword")
    @ApiImplicitParam(name = "keyword", value = "Username key word", required = true, dataType = "String", paramType = "param")
    @GetMapping(path = "/user/search")
    public List<User> findUsersByName(@RequestParam String name) {
        return restTemplate.getForObject(EUREKA_SERVICE_HTTP_URL + "/search?name=" + name, List.class);
    }
}
