package entity;

import entity.enums.*;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Abonnement {
    private UUID id;
    private String nomService;
    private double montantMensuel;
    private LocalDate dateDebut;
    private LocalDate datefin;
    private statut_abonnement status;
    private type_abonnement type_abonnement;


    public Abonnement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate datefin, statut_abonnement status, type_abonnement type) {
        setId(UUID.randomUUID());
        setNomService(nomService);
        setMontantMensuel(montantMensuel);
        setDateDebut(dateDebut);
        setDatefin(datefin);
        setStatus(status);
        setType_abonnement(type);
    }

    public Abonnement(String id, String nomService, double montantMensuel, LocalDate dateDebut, LocalDate datefin, statut_abonnement status, type_abonnement type) {
        this.id = UUID.fromString(id);
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.datefin = datefin;
        this.status = status;
        this.type_abonnement = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public String toString() {
        return "Abonnement ID        : " + getId() + "\n" +
                "Nom du service        : '" + getNomService() + "'\n" +
                "Montant total        : " + getMontantMensuel() + "\n" +
                "Type                 : " + getType_abonnement() + "\n" +
                "Date de début        : " + getDateDebut() + "\n" +
                "Date de fin          : " + getDatefin();
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public double getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(double montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDatefin() {
        return datefin;
    }

    public void setDatefin(LocalDate datefin) {
        this.datefin = datefin;
    }

    public statut_abonnement getStatus() {
        return status;
    }

    public void setStatus(statut_abonnement status) {
        this.status = status;
    }


    public type_abonnement getType_abonnement() {
        return type_abonnement;
    }

    public void setType_abonnement(type_abonnement type_abonnement) {
        this.type_abonnement = type_abonnement;
    }
}
