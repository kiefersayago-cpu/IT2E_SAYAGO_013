package com.scc;

import dao.ScenarioDao;
import model.ScenarioEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final ScenarioDao dao = new ScenarioDao();

    public static void main(String[] args) {
        try {
            dao.createTable();

            boolean running = true;

            while (running) {
                System.out.println("\n========================================");
                System.out.println("       PET CLINIC VACCINATION LOG");
                System.out.println("========================================");
                System.out.println("[1] Add Pet Record");
                System.out.println("[2] View Vaccination Records");
                System.out.println("[3] Exit System");
                System.out.println("========================================");
                System.out.print("Enter choice: ");

                String choice = sc.nextLine();

                switch (choice) {
                    case "1" -> addPetRecord();
                    case "2" -> viewRecords();
                    case "3" -> {
                        System.out.println("\nExiting system... Thank you!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    private static void addPetRecord() {
        System.out.println("\n--- ADD PET VACCINATION RECORD ---");

        System.out.print("Enter Pet Name: ");
        String petName = sc.nextLine().trim();

        System.out.print("Enter Species (DOG / CAT / BIRD): ");
        String species = sc.nextLine().trim().toUpperCase();

        System.out.print("Enter Vaccine Type (RABIES / DHPP / FVRCP): ");
        String vaccine = sc.nextLine().trim().toUpperCase();

        if (petName.isEmpty() || species.isEmpty() || vaccine.isEmpty()) {
            System.out.println("Invalid input. Fields cannot be empty.");
            return;
        }

        System.out.print("Enter Service Fee: ");

        try {
            double fee = Double.parseDouble(sc.nextLine());

            if (!Double.isFinite(fee) || fee < 0) {
                System.out.println("Invalid service fee.");
                return;
            }

            ScenarioEntity pet =
                    new ScenarioEntity(0, petName, species, vaccine, fee);

            if (dao.addRecord(pet)) {
                System.out.println("\nSUCCESS: Pet vaccination record saved!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid service fee. Enter a valid number.");
        }
    }

    private static void viewRecords() {
        List<ScenarioEntity> pets = dao.getAllRecords();

        System.out.println("\n==============================================================");
        System.out.println("                  PET VACCINATION RECORDS");
        System.out.println("==============================================================");

        System.out.printf("%-4s | %-15s | %-8s | %-12s | %-10s%n",
                "ID", "PET NAME", "SPECIES", "VACCINE", "FEE");

        System.out.println("--------------------------------------------------------------");

        for (ScenarioEntity pet : pets) {
            System.out.printf("%-4d | %-15s | %-8s | %-12s | %,.2f%n",
                    pet.petId(),
                    pet.petName(),
                    pet.species(),
                    pet.vaccine(),
                    pet.fee());
        }

        System.out.println("--------------------------------------------------------------");
        System.out.println("Total Records Displayed: " + pets.size());
    }
}