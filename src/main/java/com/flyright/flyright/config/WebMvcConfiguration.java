package com.flyright.flyright.config;

//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
//import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.*;

//@EnableWebMvc
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    // ...

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/index").setViewName("index");
        //registry.addViewController("/layout").setViewName("layout");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

}