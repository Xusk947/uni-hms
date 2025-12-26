package views.components;

import static views.constants.ViewConstants.*;

/**
 * Menu component for rendering navigation menus.
 * Provides consistent menu styling across the application.
 */
public final class Menu {

    private Menu() {
        // Prevent instantiation
    }

    /**
     * Renders a menu with title and options.
     *
     * @param title   Menu title
     * @param options Menu options (index 0 will be shown as option 1)
     */
    public static void render(String title, String... options) {
        printHeader(title);

        for (int i = 0; i < options.length; i++) {
            Button.renderNavigation(i + 1, options[i]);
        }

        Button.renderBack();
        System.out.println(MENU_SEPARATOR);
    }

    /**
     * Renders main menu (with Exit instead of Back).
     */
    public static void renderMain(String title, String... options) {
        printHeader(title);

        for (int i = 0; i < options.length; i++) {
            Button.renderNavigation(i + 1, options[i]);
        }

        Button.renderExit();
        System.out.println(MENU_SEPARATOR);
    }

    /**
     * Renders a menu with logout option.
     */
    public static void renderWithLogout(String title, String... options) {
        printHeader(title);

        for (int i = 0; i < options.length; i++) {
            Button.renderNavigation(i + 1, options[i]);
        }

        Button.renderLogout();
        System.out.println(MENU_SEPARATOR);
    }

    /**
     * Prints the menu header with title.
     */
    public static void printHeader(String title) {
        System.out.println();
        System.out.println(MENU_SEPARATOR);
        printCentered(title);
        System.out.println(MENU_SEPARATOR);
        System.out.println();
    }

    /**
     * Prints application header.
     */
    public static void printAppHeader() {
        System.out.println();
        System.out.println(MENU_SEPARATOR);
        printCentered(APP_NAME);
        printCentered("v" + APP_VERSION);
        System.out.println(MENU_SEPARATOR);
    }

    /**
     * Prints a section header (lighter style).
     */
    public static void printSectionHeader(String title) {
        System.out.println();
        System.out.println(MENU_SEPARATOR_LIGHT);
        printCentered(title);
        System.out.println(MENU_SEPARATOR_LIGHT);
    }

    /**
     * Prints centered text.
     */
    public static void printCentered(String text) {
        int padding = (MENU_SEPARATOR.length() - text.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + text);
    }

    /**
     * Clears the console (platform-dependent).
     */
    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: print empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Renders a submenu inline.
     */
    public static void renderInlineOptions(String... options) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            sb.append("[").append(i + 1).append("] ").append(options[i]);
            if (i < options.length - 1) {
                sb.append("  ");
            }
        }
        sb.append("  [0] ").append(OPTION_BACK);
        System.out.println(sb);
    }
}
