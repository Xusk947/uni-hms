package views.components;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class PageContainer extends JPanel {

    private final JPanel headerPanel;
    private final JPanel titleContainer;
    private final JLabel titleLabel;
    private final JLabel subtitleLabel;
    private final JPanel actionsContainer;
    private JComponent contentComponent;

    public PageContainer(String title) {
        this(title, null);
    }

    public PageContainer(String title, String subtitle) {
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(ViewConstants.BACKGROUND);

        titleLabel = new JLabel(title);
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleContainer.add(titleLabel);

        subtitleLabel = new JLabel();
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (subtitle != null && !subtitle.isEmpty()) {
            titleContainer.add(Box.createRigidArea(new Dimension(0, 6)));
            subtitleLabel.setText(subtitle);
            titleContainer.add(subtitleLabel);
        }

        headerPanel.add(titleContainer, BorderLayout.WEST);

        actionsContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionsContainer.setBackground(ViewConstants.BACKGROUND);
        headerPanel.add(actionsContainer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    public void addHeaderAction(JComponent component) {
        actionsContainer.add(component);
    }

    public void setContent(JComponent content) {
        if (contentComponent != null) {
            remove(contentComponent);
        }
        contentComponent = content;
        add(content, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void addSubHeader(JComponent panel) {
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(ViewConstants.BACKGROUND);
        topWrapper.add(headerPanel, BorderLayout.NORTH);
        topWrapper.add(panel, BorderLayout.CENTER);

        remove(headerPanel);
        add(topWrapper, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
}
