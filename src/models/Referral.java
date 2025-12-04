package models;

public record Referral(String referredGP_ID, String targetSpecialist_ID) {

    public void updateStatus(String newStatus) {
    }
}
