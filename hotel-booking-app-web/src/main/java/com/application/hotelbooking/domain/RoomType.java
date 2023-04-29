package com.application.hotelbooking.domain;

public enum RoomType {
    FAMILY_ROOM("Family Room"),
    SINGLE_ROOM("Single Room"),
    DOUBLE_ROOM("Double Room"),
    TWIN_ROOM("Twin Room"),
    SUITE_ROOM("Suite Room");

    public final String label;

    private RoomType(String label){
        this.label=label;
    }

}
