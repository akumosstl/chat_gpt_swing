package io.github.akumosstl.automation;

import com.google.gson.Gson;
import io.github.akumosstl.database.DatabaseManager;
import io.github.akumosstl.model.Action;
import io.github.akumosstl.utils.SwingComponentUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Recorder {

    private static final Map<Integer, Action> queue = new LinkedHashMap<>();
    private static long lastEventTime = System.currentTimeMillis();
    private static int actionCounter = 0;
    private static final AtomicBoolean recording = new AtomicBoolean(false);
    private static final DatabaseManager dbManager = new DatabaseManager();
    private static long lastTimestamp = System.currentTimeMillis();
    
    public static void startRecording() {
        if (recording.compareAndSet(false, true)) {
            System.out.println("[Recorder] Recording started.");
            queue.clear();
            lastTimestamp = System.currentTimeMillis();
        }
    }

    public static void stopRecording() {
        if (recording.compareAndSet(true, false)) {
            System.out.println("[Recorder] Recording stopped.");
            saveQueueToJson();
            recording.set(false);
            System.out.println("[Recorder] Recording stopped");
            System.out.println("[Recorder] Actions captured: " + queue.size());
            System.out.println("[Recorder] Last event time: " + lastEventTime);
            System.out.println("[Recorder] Last timestamp: " + lastTimestamp);
            System.out.println("[Recorder] Action counter: " + actionCounter);
            System.out.println("[Recorder] Queue contents: " + queue);
            dbManager.saveQueue(queue);
        }
    }

    public static void setupGlobalListeners() {
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (!recording.get()) return;

            if (event instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) event;
                if (me.getID() == MouseEvent.MOUSE_CLICKED) {
                    captureComponentEvent(me.getComponent(), "click", null);
                }
            } else if (event instanceof FocusEvent) {
                FocusEvent fe = (FocusEvent) event;
                if (fe.getID() == FocusEvent.FOCUS_GAINED) {
                    captureComponentEvent(fe.getComponent(), "focus", null);
                } else if (fe.getID() == FocusEvent.FOCUS_LOST) {
                    captureComponentEvent(fe.getComponent(), "blur", null);
                }
            } else if (event instanceof KeyEvent) {
                KeyEvent ke = (KeyEvent) event;
                if (ke.getID() == KeyEvent.KEY_TYPED) {
                    captureComponentEvent(ke.getComponent(), "key", String.valueOf(ke.getKeyChar()));
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.FOCUS_EVENT_MASK
                | AWTEvent.KEY_EVENT_MASK
                | AWTEvent.INPUT_METHOD_EVENT_MASK
                | AWTEvent.COMPONENT_EVENT_MASK);
    }

    private static String getInputValue(Component comp) {
        if (comp instanceof JTextComponent) {
            return ((JTextComponent) comp).getText();
        } else if (comp instanceof AbstractButton) {
            return ((AbstractButton) comp).getText();
        } else if (comp instanceof JLabel) {
            return ((JLabel) comp).getText();
        } else if (comp instanceof JComboBox) {
            Object selected = ((JComboBox<?>) comp).getSelectedItem();
            return selected != null ? selected.toString() : "";
        } else if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if (row >= 0 && col >= 0) {
                Object value = table.getValueAt(row, col);
                return "Row: " + row + ", Col: " + col + ", Value: " + (value != null ? value.toString() : "null");
            } else {
                return "No row selected";
            }
        } else if (comp instanceof JList) {
            JList<?> list = (JList<?>) comp;
            Object selected = list.getSelectedValue();
            return selected != null ? selected.toString() : "No selection";
        }
        return "";
    }

    private static void captureComponentEvent(Component comp, String eventType, String input) {
        if (comp == null) return;

        Action action = new Action();
        action.eventType = eventType;
        action.componentType = comp.getClass().getSimpleName();
        action.componentId = SwingComponentUtils.getComponentName(comp);
        action.parentId = SwingComponentUtils.getParentName(comp);
        action.windowTitle = SwingComponentUtils.getWindowTitle(comp);
        action.parentTitle = getParentTitle(comp);
        if (eventType.equals("blur")) {
            action.inputValue = getInputValue(comp);

        } else {
            action.inputValue = input;

        }
        long now = System.currentTimeMillis();
        action.timeToNext = (Long) (now - lastTimestamp);
        lastTimestamp = now;

        queue.put(Integer.valueOf(++actionCounter), action);
        System.out.println("[Recorder] Captured: " + action);

    }

    private static String getParentTitle(Component comp) {
        Window parent = SwingUtilities.getWindowAncestor(comp);
        if (parent instanceof JFrame) {
            return ((JFrame) parent).getTitle();
        } else if (parent instanceof JDialog) {
            return ((JDialog) parent).getTitle();
        }
        return "no-title";
    }

    private static void saveQueueToJson() {
        try (FileWriter writer = new FileWriter("recorded_actions.json")) {
            new Gson().toJson(queue, writer);
            System.out.println("[Recorder] Saved to recorded_actions.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
