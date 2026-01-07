package views.components;

import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class TableStyler {

    private TableStyler() {
    }

    public static void applyStyle(JTable table) {
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(ViewConstants.BORDER_COLOR);
        table.setFont(ViewConstants.BODY_FONT);
        table.setBackground(ViewConstants.BACKGROUND);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.setSelectionBackground(ViewConstants.TABLE_SELECTION_BG);
        table.setSelectionForeground(ViewConstants.FOREGROUND);
        table.setFocusable(false);

        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);

        styleHeader(table.getTableHeader());
        applyCellRenderer(table);
    }

    private static void styleHeader(JTableHeader header) {
        header.setFont(ViewConstants.BODY_FONT.deriveFont(Font.BOLD, 13f));
        header.setBackground(ViewConstants.TABLE_HEADER_BG);
        header.setForeground(ViewConstants.MUTED_FOREGROUND);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ViewConstants.BORDER_COLOR));
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(ViewConstants.TABLE_HEADER_BG);
                setForeground(ViewConstants.MUTED_FOREGROUND);
                setFont(ViewConstants.BODY_FONT.deriveFont(Font.BOLD, 13f));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ViewConstants.BORDER_COLOR),
                        BorderFactory.createEmptyBorder(0, 16, 0, 16)));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
    }

    private static void applyCellRenderer(JTable table) {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

                if (isSelected) {
                    setBackground(ViewConstants.TABLE_SELECTION_BG);
                    setForeground(ViewConstants.FOREGROUND);
                } else {
                    setBackground(row % 2 == 0 ? ViewConstants.BACKGROUND : ViewConstants.TABLE_ROW_ALT);
                    setForeground(ViewConstants.FOREGROUND);
                }

                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    public static JScrollPane wrapInScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(ViewConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(ViewConstants.BORDER_COLOR));
        return scrollPane;
    }
}
