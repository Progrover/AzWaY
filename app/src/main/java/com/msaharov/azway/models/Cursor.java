package com.msaharov.azway.models;

public class Cursor {
    String uuid;
    CursorType cursorType;
    int page = 0;

    Cursor lastCursor;

    public enum CursorType{
        ASC,
        DESC
    }

    public Cursor(String uuid, CursorType cursorType) {
        this.uuid = uuid;
        this.cursorType = cursorType;
    }

    public Cursor(String uuid, CursorType cursorType, int page, Cursor lastCursor) {
        this.uuid = uuid;
        this.cursorType = cursorType;
        this.page = page;
        this.lastCursor = lastCursor;
    }

    public Cursor() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public CursorType getCursorType() {
        return cursorType;
    }

    public void setCursorType(CursorType cursorType) {
        this.cursorType = cursorType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Cursor getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(Cursor lastCursor) {
        this.lastCursor = lastCursor;
    }
}
