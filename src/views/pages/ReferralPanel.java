package views.pages;

import controllers.ReferralController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.forms.ReferralForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReferralPanel extends PageContainer {

    private final ReferralController referralController;
    private JTable referralTable;
    private DefaultTableModel tableModel;

    public ReferralPanel(ReferralController referralController) {
        super("Referrals", "Manage patient referrals to specialists.");
        this.referralController = referralController;

        ModernButton addButton = new ModernButton("New Referral");
        addButton.addActionListener(e -> openNewReferralForm());
        addHeaderAction(addButton);

        String[] columns = { "ID", "Patient", "To Specialist", "Urgency", "Date", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        referralTable = new JTable(tableModel);
        TableStyler.applyStyle(referralTable);

        setContent(TableStyler.wrapInScrollPane(referralTable));
        loadData();
    }

    private void openNewReferralForm() {
        ReferralForm form = new ReferralForm(SwingUtilities.getWindowAncestor(this));
        form.setVisible(true);

        if (form.isSubmitted()) {
            String referralId = "R" + System.currentTimeMillis();

            referralController.createReferral(
                    referralId,
                    form.getPatientId(),
                    form.getReferringClinicianId(),
                    form.getReferredToClinicianId(),
                    form.getReferringFacilityId(),
                    form.getReferredToFacilityId(),
                    form.getUrgencyLevel(),
                    form.getReason(),
                    form.getClinicalSummary(),
                    form.getRequestedInvestigations());

            loadData();
            JOptionPane.showMessageDialog(this, "Referral created successfully!");
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        var referrals = referralController.getAllReferrals();

        for (var r : referrals) {
            tableModel.addRow(new Object[] {
                    r.referralId(),
                    r.patientId(),
                    r.referredToClinicianId(),
                    r.urgencyLevel(),
                    services.Const.DATE_FORMAT.format(r.referralDate()),
                    r.status()
            });
        }
    }
}
