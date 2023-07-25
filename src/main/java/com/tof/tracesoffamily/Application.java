package com.tof.tracesoffamily;

import com.tof.tracesoffamily.config.AppProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class) // app config init(pojo)
public class Application {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:application-oauth.yml";
//			+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
