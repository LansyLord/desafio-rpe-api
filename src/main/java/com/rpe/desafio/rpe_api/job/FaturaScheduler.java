package com.rpe.desafio.rpe_api.job;

import com.rpe.desafio.rpe_api.service.ClienteService;
import com.rpe.desafio.rpe_api.service.FaturaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Executa diariamente a verificação de faturas vencidas e bloqueia clientes inadimplentes.
 */

@Component
public class FaturaScheduler {

    private final FaturaService faturaService;

    public FaturaScheduler(FaturaService faturaService, ClienteService clienteService) {
        this.faturaService = faturaService;
    }

    // Roda todos os dias às 2h da manhã
    //@Scheduled(cron = "0 0 2 * * *")
    @Scheduled(cron = "0 * * * * *") // Roda a cada 1 minuto para teste
    @Transactional
    public void verificarFaturasAtrasadas() {
        faturaService.processarFaturasVencidas();
    }
}

