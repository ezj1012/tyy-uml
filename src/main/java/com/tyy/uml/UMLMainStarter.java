package com.tyy.uml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UMLMainStarter {

    public static void main(String[] args) {
        // ApplicationContextInitializer<?> initializers;
        // new SpringApplicationBuilder(UMLMainStarter.class)//
        // .web(WebApplicationType.SERVLET) //
        // .headless(false) //
        // .run(args);//
        //
        SpringApplication springApplication = new SpringApplication(UMLMainStarter.class);
        springApplication.setHeadless(false);
        springApplication.run(args);
    }

}
