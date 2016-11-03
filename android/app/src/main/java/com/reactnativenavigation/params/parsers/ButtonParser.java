package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.SearchParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.params.TitleBarSearchButtonParams;

import java.util.List;

public class ButtonParser extends Parser {
    private static final String KEY_RIGHT_BUTTONS = "rightButtons";
    private static final String KEY_LEFT_BUTTON = "leftButton";
    private static final String KEY_FAB = "fab";
    private static final String KEY_BACK_BUTTON_HIDDEN = "backButtonHidden";
    private static final String KEY_SEARCH_BUTTON = "search";

    public static List<TitleBarButtonParams> parseRightButton(Bundle params) {
        List<TitleBarButtonParams> rightButtons = null;
        if (hasKey(params, KEY_RIGHT_BUTTONS)) {
            rightButtons = new TitleBarButtonParamsParser().parseButtons(params.getBundle(KEY_RIGHT_BUTTONS));
        }
        return rightButtons;
    }

    public static TitleBarLeftButtonParams parseLeftButton(Bundle params) {
        TitleBarLeftButtonParams leftButton = null;
        if (hasKey(params, KEY_LEFT_BUTTON)) {
            leftButton =  new TitleBarLeftButtonParamsParser().parseSingleButton(params.getBundle(KEY_LEFT_BUTTON));

            boolean backButtonHidden = params.getBoolean(KEY_BACK_BUTTON_HIDDEN, false);
            if (backButtonHidden && leftButton.isBackButton()) {
                leftButton = null;
            }
        }
        return leftButton;
    }

    public static TitleBarSearchButtonParams parseSearchButton(Bundle params) {
        if (hasKey(params, KEY_SEARCH_BUTTON)) {
            SearchParams searchParams = SearchParamsParser.parse(params.getBundle(KEY_SEARCH_BUTTON));
            TitleBarSearchButtonParams buttonParams = new TitleBarButtonParamsParser().parseSearchButton(searchParams);
            return buttonParams;
        }
        return null;
    }

    public static FabParams parseFab(Bundle params, String navigatorEventId) {
        FabParams fabParams = null;
        if (hasKey(params, KEY_FAB)) {
            fabParams = new FabParamsParser().parse(params.getBundle(KEY_FAB), navigatorEventId);
        }
        return fabParams;
    }
}
