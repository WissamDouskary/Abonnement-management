package entity;

import java.time.LocalDate;
import entity.enums.*;

public class Paiement {
    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private String typePaiement;
    private statut_paiement status_paiment;
}
