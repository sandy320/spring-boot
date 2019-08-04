package com.wbchao.user.repository;

import com.wbchao.user.bean.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends ElasticsearchRepository<User,String> {

    public List<User> findUsersByActiveIsTrue();

    public List<User> findUsersByUsernameLike(String keyword);
}
