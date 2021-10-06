package com.newcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AlphaDaoMyBaties implements AlphaDao{
    @Override
    public String select() {
        return "my hell batis";
    }
}
