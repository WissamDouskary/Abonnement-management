package dao.impl;

import config.DBconnection;
import dao.PaiementDAO;
import entity.Paiement;
import entity.enums.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PaimentDAOImpl implements PaiementDAO {
    private final Connection conn;

    public PaimentDAOImpl(DBconnection dBconnection) {
        this.conn = dBconnection.getConnection();
    }

    @Override
    public void create(Paiement paiement) {
        String sql = "INSERT INTO paiement (id_paiement, id_abonnement, date_echeance, date_paiement, type_paiement, statut) VALUES (?, ? , ?, ? , ?, ?)";

        try(PreparedStatement st = conn.prepareStatement(sql)){

            st.setString(1, paiement.getIdPaiement().toString());
            st.setString(2, paiement.getIdAbonnement().toString());
            st.setObject(3, paiement.getDateEcheance(), Types.DATE);
            st.setObject(4, paiement.getDatePaiement(), Types.DATE);
            st.setObject(5, paiement.getTypePaiement(), Types.OTHER);
            st.setObject(6, paiement.getStatus_paiment(), Types.OTHER);

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error creating paiement: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Paiement paiement) {
        String sql = "UPDATE paiement SET date_echeance = ?, date_paiement = ?, type_paiement = ?, statut = ? WHERE id_paiement = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, paiement.getDateEcheance(), Types.DATE);
            st.setObject(2, paiement.getDatePaiement(), Types.DATE);
            st.setObject(3, paiement.getTypePaiement(), Types.OTHER);
            st.setObject(4, paiement.getStatus_paiment(), Types.OTHER);
            st.setString(5, paiement.getIdPaiement().toString());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No paiement found with ID: " + paiement.getIdPaiement());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating paiement: " + e.getMessage(), e);
        }
    }

    //do not implement yet
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM paiement WHERE id_paiement = ?";

        try(PreparedStatement st = conn.prepareStatement(sql)){
            st.setString(1, id);
            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting paiement: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Paiement> findAll() {
        String sql = "SELECT * FROM paiement";
        Map<String, Paiement> paiments = new HashMap<>();

        try(PreparedStatement st = conn.prepareStatement(sql)){
            ResultSet rset = st.executeQuery();
                while(rset.next()){
                    Paiement paiement = new Paiement(
                            UUID.fromString(rset.getString("id_paiement")),
                            UUID.fromString(rset.getString("id_abonnement")),
                            rset.getDate("date_echeance").toLocalDate(),
                            rset.getDate("date_paiement") != null ? rset.getDate("date_paiement").toLocalDate() : null,
                            type_paiement.valueOf(rset.getString("type_paiement")),
                            rset.getString("statut")
                    );
                    paiments.put(rset.getString("id_paiement"), paiement);
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error findall paiement: " + e.getMessage(), e);
        }

        return paiments;
    }

    @Override
    public Paiement findById(String idPaiement) {
        String sql = "SELECT * FROM paiement WHERE id_paiement = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, idPaiement.toString());
            ResultSet nrs = st.executeQuery();
            if (nrs.next()) {
                return new Paiement(
                        UUID.fromString(nrs.getString("id_paiement")),
                        UUID.fromString(nrs.getString("id_abonnement")),
                        nrs.getDate("date_echeance").toLocalDate(),
                        nrs.getDate("date_paiement") != null ? nrs.getDate("date_paiement").toLocalDate() : null,
                        type_paiement.valueOf(nrs.getString("type_paiement")),
                        nrs.getString("statut")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding paiement by ID", e);
        }
        return null;
    }

    public Paiement findByAbonnementId(String idAbonnement) {
        String sql = "SELECT * FROM paiement WHERE id_abonnement = ? LIMIT 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, idAbonnement);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Paiement(
                        UUID.fromString(rs.getString("id_paiement")),
                        UUID.fromString(rs.getString("id_abonnement")),
                        rs.getDate("date_echeance").toLocalDate(),
                        rs.getDate("date_paiement").toLocalDate(),
                        type_paiement.valueOf(rs.getString("type_paiement")),
                        rs.getString("statut")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error find paiement by abonnement: " + e.getMessage(), e);
        }
        return null;
    }

}
