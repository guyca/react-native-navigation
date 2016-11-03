package com.reactnativenavigation.params.parsers;

import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.bridge.BundleConverter;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.SearchViewParams;

public class SearchViewParamsParser extends Parser {
    public SearchViewParams parse(ReadableMap params) {
        SearchViewParams result = new SearchViewParams();
        result.screenId = params.getString("screen");
        result.navigationParams = new NavigationParams(BundleConverter.toBundle(params));
        return result;
    }
}
