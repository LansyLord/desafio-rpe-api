package com.rpe.desafio.rpe_api.job;

import com.rpe.desafio.rpe_api.service.ClienteService;
import com.rpe.desafio.rpe_api.service.FaturaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class FaturaScheduler {

    private final FaturaService faturaService;

    public FaturaScheduler(FaturaService faturaService, ClienteService clienteService) {
        this.faturaService = faturaService;
    }


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void verificarFaturasAtrasadas() {
        faturaService.processarFaturasVencidas();
    }
}

