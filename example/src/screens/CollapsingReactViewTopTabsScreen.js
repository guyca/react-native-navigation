import React, {Component} from 'react';

export default class CollapsingReactViewTopTabsScreen extends Component {
  static navigatorStyle = {
    navBarHideOnScroll: false,
    topBarCollapseOnScroll: true,
    navBarBackgroundColor: '#0f2362',
    topTabIconColor: 'white',
    navBarTextColor: 'white',
    collapsingToolBarComponent: 'example.header',
    expendCollapsingToolBarOnTopTabChange: false,
    selectedTopTabIconColor: '#4dbce9',
    selectedTopTabIndicatorColor: '#4dbce9',
    collapsingToolBarCollapsedColor: '#0f2362',
  };
}