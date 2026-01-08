package views.components;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {

    private Color normalColor;
    private Color hoverColor;
    private Color textColor;
    private boolean isHovered = false;

    public ModernButton(String text) {
        this(text, ViewConstants.PRIMARY, ViewConstants.PRIMARY_FOREGROUND);
    }

    public ModernButton(String text, Color bgColor, Color txtColor) {
        super(text);
        setColors(bgColor, txtColor);

        setFont(ViewConstants.BODY_FONT);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add padding
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public void setColors(Color bgColor, Color txtColor) {
        this.normalColor = bgColor;
        this.hoverColor = bgColor.brighter();
        this.textColor = txtColor;
        setForeground(textColor);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }

        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

        // Draw text
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}
