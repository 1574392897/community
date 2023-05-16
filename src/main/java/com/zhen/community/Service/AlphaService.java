package com.zhen.community.Service;

import com.zhen.community.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaService {
    @Autowired
    private DemoDao demoDao;

    public String Find(){
        return demoDao.select();
    }
}
