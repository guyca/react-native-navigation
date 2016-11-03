package com.reactnativenavigation.params;

public class CollapsingTopBarParams {
    public String imageUri;
    public StyleParams.Color scrimColor;

    public boolean hasImage() {
        return imageUri != null;
    }
}
