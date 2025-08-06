package br.edu.ifpb.sgm.projeto_sgm;

import br.edu.ifpb.sgm.projeto_sgm.service.TestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final TestService testService;

    public DataLoader(TestService testService) {
        this.testService = testService;
    }

    @Override
    public void run(String... args) throws Exception {
        testService.insertTestData();
    }
}