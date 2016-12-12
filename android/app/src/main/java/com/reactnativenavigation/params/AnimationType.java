package com.reactnativenavigation.params;

public enum AnimationType {
    None("none"), SlideDown("slide-down"), Fade("fade");

    private String name;

    AnimationType(String name) {
        this.name = name;
    }

    public static AnimationType fromString(String animationType) {
        for (AnimationType type : values()) {
            if (type.name.equals(animationType)) {
                return type;
            }
        }
        throw new RuntimeException("AnimationType not supported: " + animationType);
    }
}
