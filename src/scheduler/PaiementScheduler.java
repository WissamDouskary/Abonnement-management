package scheduler;

import entity.Paiement;
import entity.enums.statut_paiement;
import services.PaiementService;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PaiementScheduler {
    private final PaiementService paiementService;
    private final ScheduledExecutorService scheduler;

    public PaiementScheduler(PaiementService paiementService) {
        this.paiementService = paiementService;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkOverduePaiements, 0, 5, TimeUnit.SECONDS);
    }

    private void checkOverduePaiements() {
        try {
            Map<String, Paiement> paiements = paiementService.findAll();
            LocalDate today = LocalDate.now();

            for (Paiement paiement : paiements.values()) {
                if (paiement.getDateEcheance().isBefore(today)
                        && paiement.getDatePaiement() == null
                        && paiement.getStatus_paiment().equals(statut_paiement.NON_PAYE.getDisplayName())) {

                    paiement.setStatus_paiment(statut_paiement.EN_RETARD.getDisplayName());
                    paiementService.updatePaiement(paiement);
                    System.out.println("Paiement " + paiement.getIdPaiement() + " mis à jour en EN_RETARD.");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des paiements en retard: " + e.getMessage());
        }
    }
}
