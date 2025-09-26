package dao;

import entity.Paiement;

import java.util.Map;

public interface PaiementDAO {
    public void create(Paiement paiement);
    public void update(Paiement paiement);
    public void delete();
    public Map<String, Paiement> findAll();
    public Paiement findById(String idPaiement);
    public Paiement findByAbonnementId(String idAbonnement);
}
