package dao;

import entity.Abonnement;

import java.sql.SQLException;
import java.util.Map;

public interface AbonnementDAO {
    public void create(Abonnement abonnement) throws SQLException;
    public void update(Abonnement abonnement) throws SQLException;
    public void delete(Abonnement abonnement) throws SQLException;
    public Map<String, Abonnement> findAll() throws SQLException;
    public Abonnement findbyId(String id) throws SQLException;
}
