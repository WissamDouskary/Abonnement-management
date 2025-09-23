package dao;

import entity.Abonnement;

import java.sql.SQLException;
import java.util.List;

public interface AbonnementDAO {
    public void create(Abonnement abonnement) throws SQLException;
    public void update(Abonnement abonnement) throws SQLException;
    public void delete(Abonnement abonnement) throws SQLException;
    public List<Abonnement> findAll() throws SQLException;
    public Abonnement findbyId(String id) throws SQLException;
}
