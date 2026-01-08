package controllers;

import services.ReferralService;
import parser.Referrals;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public final class ReferralController {
    private final ReferralService referralService;

    public ReferralController() {
        this.referralService = ReferralService.getInstance();
    }

    public List<Referrals.ReferralData> getAllReferrals() {
        return referralService.getAllReferrals();
    }

    public List<Referrals.ReferralData> getReferralsByPatient(String patientId) {
        return referralService.getReferralsByPatient(patientId);
    }

    public Optional<Referrals.ReferralData> findReferral(String referralId) {
        return referralService.findReferral(referralId);
    }

    public void createReferral(String referralId, String patientId, String referringClinicianId,
                               String referredToClinicianId, String referringFacilityId,
                               String referredToFacilityId, String urgencyLevel, String reason,
                               String clinicalSummary, String requestedInvestigations) {
        referralService.createReferral(referralId, patientId, referringClinicianId,
                referredToClinicianId, referringFacilityId, referredToFacilityId,
                urgencyLevel, reason, clinicalSummary, requestedInvestigations);
    }

    public void updateReferralStatus(String referralId, String newStatus) {
        referralService.updateReferralStatus(referralId, newStatus);
    }

    public void connectReferral(String referralId) {
        referralService.connectReferral(referralId);
    }

    public void addToQueue(Referrals.ReferralData referral) {
        referralService.addToQueue(referral);
    }

    public Optional<Referrals.ReferralData> getNextFromQueue() {
        return referralService.getNextFromQueue();
    }

    public int getQueueSize() {
        return referralService.getQueueSize();
    }

    public List<Referrals.ReferralData> getPendingReferrals() {
        return getAllReferrals().stream()
                .filter(r -> r.status().equals("New") || r.status().equals("Pending"))
                .toList();
    }

    public Path sendReferralEmail(String referralId) {
        return referralService.sendReferralEmail(referralId);
    }

    public void deleteReferral(String referralId) {
        referralService.deleteReferral(referralId);
    }

    public void updateReferral(String referralId, String patientId, String referringClinicianId,
                               String referredToClinicianId, String referringFacilityId,
                               String referredToFacilityId, String urgencyLevel, String reason,
                               String clinicalSummary, String requestedInvestigations, String status) {
        referralService.updateReferral(referralId, patientId, referringClinicianId,
                referredToClinicianId, referringFacilityId, referredToFacilityId,
                urgencyLevel, reason, clinicalSummary, requestedInvestigations, status);
    }
}
