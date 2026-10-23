package dao;

import model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS pets (
                    pet_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pet_name TEXT NOT NULL,
                    species TEXT NOT NULL,
                    vaccine_type TEXT NOT NULL,
                    service_fee REAL NOT NULL
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean addRecord(ScenarioEntity pet) {
        String sql = """
                INSERT INTO pets
                (pet_name, species, vaccine_type, service_fee)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pet.petName());
            pstmt.setString(2, pet.species());
            pstmt.setString(3, pet.vaccine());
            pstmt.setDouble(4, pet.fee());

            pstmt.executeUpdate();
            System.out.println("Pet record successfully saved!");
            return true;

        } catch (SQLException e) {
            System.err.println("Failed to save pet record: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> getAllRecords() {
        String sql = """
                SELECT pet_id, pet_name, species, vaccine_type, service_fee
                FROM pets
                ORDER BY pet_id
                """;

        List<ScenarioEntity> pets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ScenarioEntity pet = new ScenarioEntity(
                        rs.getInt("pet_id"),
                        rs.getString("pet_name"),
                        rs.getString("species"),
                        rs.getString("vaccine_type"),
                        rs.getDouble("service_fee")
                );

                pets.add(pet);
            }

        } catch (SQLException e) {
            System.err.println("Failed to retrieve records: " + e.getMessage());
        }

        return pets;
    }
}