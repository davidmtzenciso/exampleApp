package com.example.app.conf;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.app.App;

import io.cucumber.java.Before;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class CucumberContextConfiguration {

	@Before
	public void setup_cucumber_spring_context() {}
	
}
