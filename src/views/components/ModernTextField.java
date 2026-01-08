package views.components;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernTextField extends JTextField {

    public ModernTextField() {
        super();
        init();
    }

    private void init() {
        setOpaque(false);
        setFont(ViewConstants.BODY_FONT);
        setForeground(ViewConstants.FOREGROUND);
        setCaretColor(ViewConstants.FOREGROUND);
        setMargin(new Insets(8, 12, 8, 12));
        setBorder(ViewConstants.INPUT_BORDER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void setBorder(javax.swing.border.Border border) {
        super.setBorder(border);
    }
}
