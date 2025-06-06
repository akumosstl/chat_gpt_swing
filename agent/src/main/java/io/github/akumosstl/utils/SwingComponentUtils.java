package io.github.akumosstl.utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SwingComponentUtils {

    public static Component findComponentById_(String id) {
        for (Window window : Window.getWindows()) {
            Component comp = findComponentInContainer(window, id);
            if (comp != null) {
                return comp;
            }
        }
        return null;
    }

    private static Component findComponentInContainer(Container container, String id) {
        if (id.equals(container.getName())) {
            return container;
        }
        for (Component comp : container.getComponents()) {
            if (id.equals(comp.getName())) {
                return comp;
            }
            if (comp instanceof Container) {
                Component child = findComponentInContainer((Container) comp, id);
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }

    public static String getComponentName(Component comp) {
        return comp.getName() != null ? comp.getName() : comp.getClass().getSimpleName() + "@" + Integer.toHexString(comp.hashCode());
    }

    public static String getParentName(Component comp) {
        return comp.getParent() != null ? getComponentName(comp.getParent()) : "Root";
    }

    public static String getWindowTitle(Component comp) {
        Window window = SwingUtilities.getWindowAncestor(comp);
        if (window instanceof Frame) {
            return ((Frame) window).getTitle();
        } else if (window instanceof Dialog) {
            return ((Dialog) window).getTitle();
        }
        return "NoWindow";
    }

    public static Component findComponentById(String id) {
        for (Window window : Window.getWindows()) {
            Component c = searchComponent(window, id);
            if (c != null) return c;
        }
        return null;
    }

    private static Component searchComponent(Container container, String id) {
        if (id.equals(container.getName())) {
            return container;
        }
        for (Component comp : container.getComponents()) {
            if (id.equals(comp.getName())) {
                return comp;
            }
            if (comp instanceof Container) {
                Component child = searchComponent((Container) comp, id);
                if (child != null) return child;
            }
        }
        return null;
    }

    public static ArrayList<Component> getAllComponents(Container container) {
        ArrayList<Component> list = new ArrayList<>();
        for (Component comp : container.getComponents()) {
            list.add(comp);
            if (comp instanceof Container) {
                list.addAll(getAllComponents((Container) comp));
            }
        }
        return list;
    }
}

