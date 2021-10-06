package com.newcoder.community.service;


import com.newcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    public AlphaService()
    {
        System.out.println("servicehard");
    }

    @PostConstruct
    public void init()
    {
        System.out.println("serviceinit");
    }

    @PreDestroy
    public void destroy()
    {
        System.out.println("destory");
    }

    public String find()
    {
        return alphaDao.select();
    }

}
