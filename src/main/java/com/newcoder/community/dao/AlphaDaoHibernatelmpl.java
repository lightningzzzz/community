package com.newcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHab")
public class AlphaDaoHibernatelmpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }



}
