package br.com.bootcamp.tayBank.scheduler;

import br.com.bootcamp.tayBank.scheduler.service.ContaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerTasks {

    @Autowired
    ContaService contaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerTasks.class);

    @Scheduled(fixedDelay = 300000)
    public void executarCriacaoConta(){
        LOGGER.info("Começando criação de conta...");
        contaService.criaConta();
    }
}