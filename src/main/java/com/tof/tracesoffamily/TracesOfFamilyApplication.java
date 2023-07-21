package com.tof.tracesoffamily;

import com.tof.tracesoffamily.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class) // app config init(pojo)
public class TracesOfFamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TracesOfFamilyApplication.class, args);
	}

}
