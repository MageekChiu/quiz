package cn.mageek.quiz.service;


import cn.mageek.quiz.entity.Tag;
import cn.mageek.quiz.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceMongoImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

}
