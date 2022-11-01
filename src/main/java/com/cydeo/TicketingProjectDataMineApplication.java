package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketingProjectDataMineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataMineApplication.class, args);
    }
    ModelMapper mapper = new ModelMapper();
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

}

//