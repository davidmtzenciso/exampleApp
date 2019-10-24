package com.example.app.conf;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty" },
				 glue = {"com/example/app/integration/controller", "com/example/app/conf"},
				 features = "src/test/resources/features",
				 monochrome = true)
public class CucumberTest {}
