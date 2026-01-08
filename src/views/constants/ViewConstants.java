package views.constants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ViewConstants {
    public static final String APP_NAME = "HMS System";
    public static final String APP_VERSION = "1.0";

    // Typography
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Colors
    public static final Color BACKGROUND = new Color(255, 255, 255);
    public static final Color FOREGROUND = new Color(9, 9, 11);

    public static final Color MUTED = new Color(244, 244, 245);
    public static final Color MUTED_FOREGROUND = new Color(113, 113, 122);

    public static final Color PRIMARY = new Color(24, 24, 27);
    public static final Color PRIMARY_FOREGROUND = new Color(250, 250, 250);

    public static final Color SECONDARY = new Color(244, 244, 245);
    public static final Color SECONDARY_FOREGROUND = new Color(24, 24, 27);

    public static final Color BORDER_COLOR = new Color(228, 228, 231);
    public static final Color INPUT_COLOR = new Color(228, 228, 231);
    public static final Color RING = new Color(24, 24, 27);

    public static final Color TABLE_HEADER_BG = new Color(248, 250, 252);
    public static final Color TABLE_ROW_ALT = new Color(249, 250, 251);
    public static final Color TABLE_SELECTION_BG = new Color(226, 232, 240);
    public static final Color TABLE_HOVER_BG = new Color(243, 244, 246);
    public static final Color ERROR_BORDER = new Color(239, 68, 68);
    public static final Color DESTRUCTIVE = new Color(239, 68, 68);
    public static final Color DESTRUCTIVE_FOREGROUND = new Color(255, 255, 255);

    // Style of GUI
    public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    public static final Border PADDING_BORDER = BorderFactory.createEmptyBorder(16, 16, 16, 16);

    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
            new views.components.RoundedBorder(BORDER_COLOR, 12, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16));

    public static final Border INPUT_BORDER = BorderFactory.createCompoundBorder(
            new views.components.RoundedBorder(INPUT_COLOR, 8, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12));
}
