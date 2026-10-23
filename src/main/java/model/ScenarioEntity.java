package model;

public record ScenarioEntity(
        int petId,
        String petName,
        String species,
        String vaccine,
        double fee
) {
    @Override
    public String toString() {
        return String.format(
                "[%d] %s (%s) - Vaccine: %s - Fee: %.2f",
                petId, petName, species, vaccine, fee
        );
    }
}