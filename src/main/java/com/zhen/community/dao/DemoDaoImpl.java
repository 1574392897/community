package com.zhen.community.dao;

import org.springframework.stereotype.Repository;

@Repository
public class DemoDaoImpl implements DemoDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
