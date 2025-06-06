package io.github.akumosstl.automation;

import io.github.akumosstl.model.Action;
import io.github.akumosstl.utils.SwingComponentUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class AutomationExecutor {
    private final Map<Integer, Action> queue;

    public AutomationExecutor(Map<Integer, Action> queue) {
        this.queue = new TreeMap<>(queue);
    }

    public static void execute(Action action) {
        Component comp = SwingComponentUtils.findComponentById(action.componentId);

        if (comp == null) {
            System.out.println("[Automation] Component not found: " + action.componentId);
            return;
        }

        try {
            performAction(comp, action);
            System.out.println("[Automation] Executed action on " + action.componentId);

            Thread.sleep(action.delay);

        } catch (Exception e) {
            System.err.println("[Automation] Error executing action: " + e.getMessage());
        }
    }

    private static void performAction(Component component, Action action) {
        if (component instanceof JButton || component instanceof JMenuItem) {
            ((AbstractButton) component).doClick();
        }
        /**
        else if (component instanceof JTable) {
            JTable table = (JTable) component;
            int row = Integer.parseInt(action.inputValue);
            if (row >= 0 && row < table.getRowCount()) {
                table.setRowSelectionInterval(row, row);
                table.scrollRectToVisible(table.getCellRect(row, 0, true));
            }
         */
        else if (component instanceof AbstractButton) {
            clickButton((AbstractButton) component);
        } else if (component instanceof JTextComponent) {
            setText((JTextComponent) component, action.inputValue);
        } else if (component instanceof JComboBox) {
            selectComboBoxItem((JComboBox<?>) component, action.inputValue);
        } else if (component instanceof JTable) {
            selectTableCell((JTable) component, action.inputValue);
        } else if (component instanceof JList) {
            selectListItem((JList<?>) component, action.inputValue);
        } else {
            System.out.println("Unsupported component type: " + component.getClass().getSimpleName());
            component.requestFocus();
        }
    }

    private static void clickButton(AbstractButton button) {
        SwingUtilities.invokeLater(() -> button.doClick());
    }

    private static void setText(JTextComponent textComponent, String text) {
        SwingUtilities.invokeLater(() -> textComponent.setText(text));
    }

    private static void selectComboBoxItem(JComboBox<?> comboBox, String item) {
        SwingUtilities.invokeLater(() -> comboBox.setSelectedItem(item));
    }

    private static void selectListItem(JList<?> list, String item) {
        SwingUtilities.invokeLater(() -> list.setSelectedValue(item, true));
    }

    private static void selectTableCell(JTable table, String inputValue) {
        try {
            // Expect input like "Row: 2, Col: 1"
            String[] parts = inputValue.split(",");
            int row = Integer.parseInt(parts[0].split(":")[1].trim());
            int col = Integer.parseInt(parts[1].split(":")[1].trim());

            SwingUtilities.invokeLater(() -> {
                table.changeSelection(row, col, false, false);
            });
        } catch (Exception e) {
            System.err.println("Invalid table input format: " + inputValue);
        }
    }

}

