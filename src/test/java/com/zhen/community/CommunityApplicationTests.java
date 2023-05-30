package com.zhen.community;

import com.zhen.community.dao.DemoDao;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommunityApplicationTests implements ApplicationContextAware {


	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	void testApplicationContext() {
		System.out.println(applicationContext);

		DemoDao demoDao = applicationContext.getBean(DemoDao.class);
		System.out.println(demoDao.select());
	}

	@Autowired
	public DemoDao demoDao;

	@Test
	public void testDi(){
		System.out.println(demoDao);
	}
}
