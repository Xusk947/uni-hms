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

    public ModernTextField(int columns) {
        super(columns);
        init();
    }

    private void init() {
        setOpaque(false); // We paint background ourselves
        setFont(ViewConstants.BODY_FONT);
        setForeground(ViewConstants.FOREGROUND);
        setCaretColor(ViewConstants.FOREGROUND);
        setMargin(new Insets(8, 12, 8, 12));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // Padding for text
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));

        // Paint border
        g2.setColor(ViewConstants.INPUT_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));

        g2.dispose();
        super.paintComponent(g);
    }

    // Override setBorder to prevent external overrides breaking the look, 
    // or handle it gracefully if it's an error border
    @Override
    public void setBorder(javax.swing.border.Border border) {
        // Allow setting border if it's the empty padding border we set in init
        // or if we want to support error borders later.
        // For now, let's keep super.setBorder to allow padding changes.
        super.setBorder(border);
    }
}
