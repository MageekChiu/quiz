package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Person;

import java.util.List;

public interface PersonService {
    List<Person> findByAddress(String address);
}
