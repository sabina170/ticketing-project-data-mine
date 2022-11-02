package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication // this includes  @Configuration that we need for @Bean
public class TicketingProjectDataMineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataMineApplication.class, args);
    }


    @Bean
    public ModelMapper mapper(){
        return new ModelMapper(); // instead of writing ModelMapper mapper = new ModelMapper();
    }

}

//