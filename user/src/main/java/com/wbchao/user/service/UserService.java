package com.wbchao.user.service;

import com.wbchao.user.bean.User;

import java.util.List;

public interface UserService {

    /**
     * Register a new user
     *
     * @param user
     */
    public void addUser(User user);

    /**
     * Soft delete the user, set active to false
     *
     * @param id
     */
    public void deleteUser(String id);

    /**
     * Active the user
     *
     * @param id
     */
    public User activeUser(String id);

    /**
     * Update the exist user
     *
     * @param user
     */
    public User updateUser(User user);

    /**
     * Find the user by id
     *
     * @param id
     */
    public User findUserById(String id);

    /**
     * Find all users
     *
     */
    public List<User> findAllUsers();

    /**
     * Find all active users
     *
     */
    public List<User> findActiveUsers();

    /**
     * Find the users by username
     *
     * @param keyword
     * @return
     */
    public List<User> findUsersByName(String keyword);
}
