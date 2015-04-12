package pl.konczak.atpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mangofactory.swagger.plugin.EnableSwagger;

@SpringBootApplication
@ComponentScan(basePackages = "pl.konczak.atpserver")
@EnableSwagger
public class AtpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtpServerApplication.class, args);
    }
}
