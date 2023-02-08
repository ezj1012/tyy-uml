package com.tyy.uml.configuration;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.tyy.uml.core.ctx.UMLContext;

public class UMLGUIBootstarter implements SpringApplicationRunListener, Ordered {

    final SpringApplication application;

    final String[] args;

    ConfigurableEnvironment environment;

    public UMLGUIBootstarter(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        this.environment = environment;

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        UMLContext.getContext().loadProps(environment);
        UMLContext.getContext().setApplicationContext(context);
    }

    @Override
    public int getOrder() {
        return 10;
    }

}
