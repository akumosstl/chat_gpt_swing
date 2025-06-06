package io.github.akumosstl.model;


public class Action {
    public String componentType;
    public String componentId;
    public String parentId;
    public String windowTitle;
    public String inputValue;
    public Long timeToNext;
    public String parentTitle;
    public long delay;
    public String eventType;

    public Action(String componentType, String componentId, String parentId,
                  String parentTitle, String inputValue, long timeToNext) {
        this.componentType = componentType;
        this.componentId = componentId;
        this.parentId = parentId;
        this.parentTitle = parentTitle;
        this.inputValue = inputValue;
        this.timeToNext = (Long) timeToNext;
    }

    public Action() {

    }

    @Override
    public String toString() {
        return String.format(
                "Action[type=%s, id=%s, parent=%s, window=%s, input=%s, delay=%d]",
                componentType, componentId, parentId, windowTitle, inputValue, timeToNext
        );
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public Long getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(Long timeToNext) {
        this.timeToNext = timeToNext;
    }
}
