package com.wbchao.user.service;


import com.wbchao.user.bean.User;
import com.wbchao.user.config.RabbitMqConfig;
import com.wbchao.user.dao.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@CacheConfig(cacheNames = {"user"})
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    @Autowired
    UserDao userDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    @Autowired
    RedisCacheManager userRedisCacheManager;

    /**
     * Register a new user
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        logger.info("Add user " + user.toString());
        userDao.addUser(user);
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        String exchangeName = rabbitMqConfig.getExchange();
        String routekey = rabbitMqConfig.getRoutekey();
        rabbitTemplate.convertAndSend(exchangeName, routekey, map);
    }

    /**
     * Soft delete the user, set active to false
     *
     * @param id
     */
    @Override
    @CacheEvict(beforeInvocation = true)
    public void deleteUser(String id) {
        logger.info("Delete user " + id);
        userDao.deleteUser(id);
    }

    /**
     * Active the user
     *
     * @param id
     */
    @CachePut(key = "#result.id")
    public User activeUser(String id) {
        logger.info("Active user " + id);
        return userDao.activeUser(id);
    }

    /**
     * Update the exist user
     *
     * @param user
     */
    @Override
    @CachePut(key = "#user.id")
    public User updateUser(User user) {
        logger.info("Update user " + user.toString());
        return userDao.updateUser(user);
    }

    /**
     * Find the user by id
     *
     * @param id
     */
    @Override
    @Cacheable(key = "#id")
    public User findUserById(String id) {
        logger.info("Find user " + id);
        return userDao.findUserById(id);
    }

    /**
     * Find all users
     */
    @Override
    public List<User> findAllUsers() {
        logger.info("Find all users");
        Cache cache = userRedisCacheManager.getCache("user");
        List<User> result = userDao.findAllUsers();
        result.stream().forEach(u ->{
            cache.put(u.getId(),u);
        });
        return result;
    }

    /**
     * Find all active users
     */
    public List<User> findActiveUsers() {
        logger.info("Find all active users");
        Cache cache = userRedisCacheManager.getCache("user");
        List<User> result = userDao.findActiveUsers();
        result.stream().forEach(u ->{
            cache.put(u.getId(),u);
        });
        return result;
    }

    /**
     * Find the users by username
     *
     * @param keyword
     * @return
     */
    public List<User> findUsersByName(String keyword) {
        logger.info("Find all users with name keyword " + keyword);
        Cache cache = userRedisCacheManager.getCache("user");
        List<User> result = userDao.findUsersByName(keyword);
        result.stream().forEach(u ->{
            cache.put(u.getId(),u);
        });
        return result;
    }
}
