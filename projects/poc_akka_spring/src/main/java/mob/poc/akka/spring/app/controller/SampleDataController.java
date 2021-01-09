package mob.poc.akka.spring.app.controller;

import com.google.gson.Gson;
import mob.poc.akka.spring.app.model.SampleData;
import mob.poc.akka.spring.app.persistence.SampleDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleDataController {

    @Autowired
    private SampleDataRepository repository;

    @Autowired
    private Gson serializer;

    @GetMapping("/data/partition/{partitionValue}")
    public String searchData(@PathVariable("partitionValue") String partition) {
        return serializer.toJson(repository.retrieveByPartition(partition));
    }

    @PostMapping("/data")
    public String putData(@RequestBody SampleData content) {
        try {
            repository.save(content);
        } catch (Exception e) {
            return "500 NOT OK. error: " + e.getMessage();
        }

        return "200 OK";
    }
}
