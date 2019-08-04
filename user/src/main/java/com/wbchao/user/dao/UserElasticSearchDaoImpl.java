package com.wbchao.user.dao;

import com.google.common.collect.Lists;
import com.wbchao.user.bean.User;
import com.wbchao.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserElasticSearchDaoImpl implements UserDao {

    @Autowired
    UserRepository userRepository;

    /**
     * Register a new user
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        user.setId(UUID.randomUUID()
                       .toString());
        user.setActive(true);
        userRepository.index(user);
    }

    /**
     * Soft delete the user, set active to false
     *
     * @param id
     */
    @Override
    public void deleteUser(String id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return;
        }
        user.setActive(false);
        updateUser(user);
    }

    /**
     * Active the user
     *
     * @param id
     */
    public User activeUser(String id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return user;
        }
        user.setActive(true);
        updateUser(user);
        return user;
    }

    /**
     * Update the exist user
     *
     * @param user
     */
    @Override
    public User updateUser(User user) {
        if (user.getId() == null || user.getId()
                                        .isEmpty()) {
            return user;
        }
        userRepository.save(user);
        return user;
    }

    /**
     * Find the user by id
     *
     * @param id
     */
    @Override
    public User findUserById(String id) {
        return userRepository.findOne(id);
    }

    /**
     * Find all users
     *
     */
    @Override
    public List<User> findAllUsers() {
        return Lists.newArrayList(userRepository.findAll());
    }

    /**
     * Find all active users
     *
     */
    public List<User> findActiveUsers(){
        return  userRepository.findUsersByActiveIsTrue();
    }

    /**
     * Find the users by username
     *
     * @param keyword
     * @return
     */
    public List<User> findUsersByName(String keyword){
        return userRepository.findUsersByUsernameLike(keyword);
    }
}
