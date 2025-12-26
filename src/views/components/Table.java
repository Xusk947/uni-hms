package views.components;

import views.constants.ViewConstants;
import java.util.List;

/**
 * Reusable Table component for displaying tabular data.
 * Similar to shadcn/ui Table component.
 */
public class Table {

    private String[] headers;
    private List<String[]> rows;
    private int[] columnWidths;

    /**
     * Creates a new Table with specified headers.
     *
     * @param headers column header names
     */
    public Table(String[] headers) {
        this.headers = headers;
        this.columnWidths = new int[headers.length];
        calculateColumnWidths();
    }

    /**
     * Creates a new Table with headers and data.
     *
     * @param headers column header names
     * @param rows    table data rows
     */
    public Table(String[] headers, List<String[]> rows) {
        this.headers = headers;
        this.rows = rows;
        this.columnWidths = new int[headers.length];
        calculateColumnWidths();
    }

    /**
     * Sets the table data rows.
     *
     * @param rows list of row data
     */
    public void setRows(List<String[]> rows) {
        this.rows = rows;
        calculateColumnWidths();
    }

    /**
     * Calculates optimal column widths based on content.
     */
    private void calculateColumnWidths() {
        // Start with header widths
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = Math.max(headers[i].length(), ViewConstants.MIN_COLUMN_WIDTH);
        }

        // Check row data widths
        if (rows != null) {
            for (String[] row : rows) {
                for (int i = 0; i < Math.min(row.length, headers.length); i++) {
                    if (row[i] != null) {
                        columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                    }
                }
            }
        }

        // Apply max width constraint
        for (int i = 0; i < columnWidths.length; i++) {
            columnWidths[i] = Math.min(columnWidths[i], ViewConstants.MAX_COLUMN_WIDTH);
        }
    }

    /**
     * Renders the table to console.
     */
    public void render() {
        printTopBorder();
        printHeaderRow();
        printSeparatorLine();

        if (rows != null && !rows.isEmpty()) {
            for (String[] row : rows) {
                printDataRow(row);
            }
        } else {
            printEmptyMessage();
        }

        printBottomBorder();
    }

    /**
     * Prints the top border line.
     */
    private void printTopBorder() {
        StringBuilder sb = new StringBuilder();
        sb.append(ViewConstants.CORNER_TOP_LEFT);

        for (int i = 0; i < columnWidths.length; i++) {
            sb.append(repeat(ViewConstants.BORDER_HORIZONTAL, columnWidths[i] + 2));
            if (i < columnWidths.length - 1) {
                sb.append(ViewConstants.T_DOWN);
            }
        }

        sb.append(ViewConstants.CORNER_TOP_RIGHT);
        System.out.println(sb);
    }

    /**
     * Prints the bottom border line.
     */
    private void printBottomBorder() {
        StringBuilder sb = new StringBuilder();
        sb.append(ViewConstants.CORNER_BOTTOM_LEFT);

        for (int i = 0; i < columnWidths.length; i++) {
            sb.append(repeat(ViewConstants.BORDER_HORIZONTAL, columnWidths[i] + 2));
            if (i < columnWidths.length - 1) {
                sb.append(ViewConstants.T_UP);
            }
        }

        sb.append(ViewConstants.CORNER_BOTTOM_RIGHT);
        System.out.println(sb);
    }

    /**
     * Prints a separator line between header and data.
     */
    private void printSeparatorLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(ViewConstants.T_RIGHT);

        for (int i = 0; i < columnWidths.length; i++) {
            sb.append(repeat(ViewConstants.BORDER_HORIZONTAL, columnWidths[i] + 2));
            if (i < columnWidths.length - 1) {
                sb.append(ViewConstants.CROSS);
            }
        }

        sb.append(ViewConstants.T_LEFT);
        System.out.println(sb);
    }

    /**
     * Prints the header row with bold styling.
     */
    private void printHeaderRow() {
        StringBuilder sb = new StringBuilder();
        sb.append(ViewConstants.BORDER_VERTICAL);

        for (int i = 0; i < headers.length; i++) {
            sb.append(" ");
            sb.append(ViewConstants.BOLD);
            sb.append(padRight(headers[i], columnWidths[i]));
            sb.append(ViewConstants.RESET);
            sb.append(" ");
            sb.append(ViewConstants.BORDER_VERTICAL);
        }

        System.out.println(sb);
    }

    /**
     * Prints a data row.
     *
     * @param row the row data to print
     */
    private void printDataRow(String[] row) {
        StringBuilder sb = new StringBuilder();
        sb.append(ViewConstants.BORDER_VERTICAL);

        for (int i = 0; i < columnWidths.length; i++) {
            sb.append(" ");
            String value = (i < row.length && row[i] != null) ? row[i] : "";
            sb.append(padRight(truncate(value, columnWidths[i]), columnWidths[i]));
            sb.append(" ");
            sb.append(ViewConstants.BORDER_VERTICAL);
        }

        System.out.println(sb);
    }

    /**
     * Prints empty table message.
     */
    private void printEmptyMessage() {
        int totalWidth = 0;
        for (int width : columnWidths) {
            totalWidth += width + 3;
        }
        totalWidth -= 1;

        StringBuilder sb = new StringBuilder();
        sb.append(ViewConstants.BORDER_VERTICAL);
        String message = "No data available";
        int padding = (totalWidth - message.length()) / 2;
        sb.append(repeat(" ", padding));
        sb.append(message);
        sb.append(repeat(" ", totalWidth - padding - message.length()));
        sb.append(ViewConstants.BORDER_VERTICAL);
        System.out.println(sb);
    }

    /**
     * Pads a string to the right with spaces.
     */
    private String padRight(String text, int length) {
        if (text.length() >= length) {
            return text.substring(0, length);
        }
        return text + repeat(" ", length - text.length());
    }

    /**
     * Truncates text if it exceeds max length.
     */
    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Repeats a string n times.
     */
    private String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
