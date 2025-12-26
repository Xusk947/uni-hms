package views.components;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {

    public SidebarButton(String text, String iconPath) {
        super(text);
        setFont(ViewConstants.BODY_FONT);
        setForeground(ViewConstants.MUTED_FOREGROUND);
        setBackground(ViewConstants.BACKGROUND); // Transparent-ish initially
        setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        setHorizontalAlignment(SwingConstants.LEFT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(ViewConstants.MUTED);
                setForeground(ViewConstants.FOREGROUND);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected()) {
                    setBackground(ViewConstants.BACKGROUND);
                    setForeground(ViewConstants.MUTED_FOREGROUND);
                }
            }
        });
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if (b) {
            setBackground(ViewConstants.SECONDARY);
            setForeground(ViewConstants.SECONDARY_FOREGROUND);
            setFont(ViewConstants.SUBHEADER_FONT.deriveFont(14f));
        } else {
            setBackground(ViewConstants.BACKGROUND);
            setForeground(ViewConstants.MUTED_FOREGROUND);
            setFont(ViewConstants.BODY_FONT);
        }
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
