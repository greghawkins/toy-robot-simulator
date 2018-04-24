package com.onestoptech.domain;

public enum CardinalDirection {
    NORTH("WEST", "EAST"), EAST("NORTH", "SOUTH"), SOUTH("EAST", "WEST"), WEST("SOUTH", "NORTH");

    private final String left;
    private final String right;

    CardinalDirection(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }
}
