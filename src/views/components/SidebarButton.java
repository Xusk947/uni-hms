package views.components;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JToggleButton {

    private boolean hovered = false;

    public SidebarButton(String text, String iconPath) {
        super(text);
        setFont(ViewConstants.BODY_FONT);
        setForeground(ViewConstants.MUTED_FOREGROUND);
        setBackground(ViewConstants.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        setHorizontalAlignment(SwingConstants.LEFT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                updateStyle();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                updateStyle();
            }
        });

        // Listen for selection changes (from ButtonGroup)
        addItemListener(e -> updateStyle());

        // Initial style
        updateStyle();
    }

    private void updateStyle() {
        if (isSelected()) {
            setBackground(ViewConstants.SECONDARY);
            setForeground(ViewConstants.SECONDARY_FOREGROUND);
            setFont(ViewConstants.SUBHEADER_FONT.deriveFont(14f));
        } else if (hovered) {
            setBackground(ViewConstants.MUTED);
            setForeground(ViewConstants.FOREGROUND);
            setFont(ViewConstants.BODY_FONT);
        } else {
            setBackground(ViewConstants.BACKGROUND);
            setForeground(ViewConstants.MUTED_FOREGROUND);
            setFont(ViewConstants.BODY_FONT);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed() || isSelected() || !getBackground().equals(ViewConstants.BACKGROUND)) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
