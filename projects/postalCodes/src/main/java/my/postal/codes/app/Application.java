package my.postal.codes.app;

import com.google.gson.Gson;
import my.postal.codes.app.domain.internal.AddressWithSearchHistory;
import my.postal.codes.app.domain.internal.RequestInformation;
import my.postal.codes.app.processor.api.RequestProcessor;
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
import java.util.Optional;

@SpringBootApplication
@RestController
public class Application {

    @Autowired
    private RequestProcessor<RequestInformation, Optional<AddressWithSearchHistory>> requestProcessor;

    @Autowired
    private Gson serializer;

    @RequestMapping("/")
    public String home() {
        return "Hello Docker World: RUI";
    }

    @RequestMapping("/search")
    public String searchPostalAddress(@RequestParam String postalCode, @RequestParam String userId) {

        try {
            return serializer.toJson(requestProcessor.processRequest(new RequestInformation(postalCode, userId)));
        } catch (Exception ex) {
            return ex.getMessage();
        }
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