package mob.poc.akka.spring.app;

import com.google.gson.Gson;
import mob.poc.akka.spring.app.model.SampleData;
import mob.poc.akka.spring.app.persistence.SampleDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@RestController
public class Application {

    @Autowired
    private SampleDataRepository repository;

    @Autowired
    private Gson serializer;

    @RequestMapping("/")
    public String home() {
        return "Hello to POC AKKA SPRING";
    }

    @RequestMapping("/getData")
    public String searchData(@RequestParam String id) {
        return repository.retrieve(id).map(data -> serializer.toJson(data)).orElse("404 NOT FOUND");
    }

    @RequestMapping("/putData")
    public String putData(SampleData content) {
        try {
            repository.add(content);
        } catch (Exception e) {
            return "500 NOT OK. error: " + e.getMessage();
        }

        return "200 OK";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}