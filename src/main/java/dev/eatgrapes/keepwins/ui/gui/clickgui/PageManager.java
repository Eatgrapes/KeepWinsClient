package dev.eatgrapes.keepwins.ui.gui.clickgui;

public class PageManager {
    public enum PageType {
        HOME,
        MODULES
    }

    private static PageType currentPage = PageType.HOME;

    public static void setCurrentPage(PageType page) {
        currentPage = page;
    }

    public static PageType getCurrentPage() {
        return currentPage;
    }
}