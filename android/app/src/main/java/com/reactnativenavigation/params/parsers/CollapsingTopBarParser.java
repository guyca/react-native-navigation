package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.CollapsingTopBarParams;

public class CollapsingTopBarParser {
    private Bundle params;

    public CollapsingTopBarParser(Bundle params) {
        this.params = params;
    }

    public CollapsingTopBarParams parse() {
        if (!hasLocalImageResource() && !hasImageUrl()) {
            return null;
        }

        CollapsingTopBarParams result = new CollapsingTopBarParams();
        if (hasLocalImageResource()) {
            result.imageUri = params.getString("collapsingToolBarImage");
        } else {
            result.imageUri = params.getString("collapsingToolBarImageUrl");
        }
        return result;
    }

    private boolean hasLocalImageResource() {
        return params.containsKey("collapsingToolBarImage");
    }

    private boolean hasImageUrl() {
        return params.containsKey("collapsingToolBarImageUrl");
    }
}
