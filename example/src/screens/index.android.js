import {Navigation} from 'react-native-navigation';

import FirstTabScreen from './FirstTabScreen';
import SecondTabScreen from './SecondTabScreen';
import PushedScreen from './PushedScreen';
import StyledScreen from './StyledScreen';
import LeftSideMenu from './LeftSideMenu';
import RightSideMenu from './RightSideMenu';
import ModalScreen from './ModalScreen';
import TopTabsScreen from './TopTabsScreen';
import FirstTopTabScreen from './FirstTopTabScreen';
import SecondTopTabScreen from './SecondTopTabScreen';
import CollapsingTopBarScreen from './CollapsingTopBarScreen';
import ListScreen from './ListScreen';
import ReplacedScreen from './ReplacedScreen';
import CollapsingReactViewScreen from './CollapsingReactViewScreen';
import CollapsingReactViewTopTabsScreen from './CollapsingReactViewTopTabsScreen';
import Header  from './Header';
import LightBoxScreen from './LightBoxScreen';

// register all screens of the app (including internal ones)
export function registerScreens() {
  Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);
  Navigation.registerComponent('example.SecondTabScreen', () => SecondTabScreen);
  Navigation.registerComponent('example.PushedScreen', () => PushedScreen);
  Navigation.registerComponent('example.ReplacedScreen', () => ReplacedScreen);
  Navigation.registerComponent('example.StyledScreen', () => StyledScreen);
  Navigation.registerComponent('example.ModalScreen', () => ModalScreen);
  Navigation.registerComponent('example.LeftSideMenu', () => LeftSideMenu);
  Navigation.registerComponent('example.RightSideMenu', () => RightSideMenu);
  Navigation.registerComponent('example.CollapsingTopBarScreen', () => CollapsingTopBarScreen);
  Navigation.registerComponent('example.TopTabsScreen', () => TopTabsScreen);
  Navigation.registerComponent('example.SecondTopTabScreen', () => SecondTopTabScreen);
  Navigation.registerComponent('example.FirstTopTabScreen', () => FirstTopTabScreen);
  Navigation.registerComponent('example.ListScreen', () => ListScreen);
  Navigation.registerComponent('example.collapsingReactViewScreen', () => CollapsingReactViewScreen);
  Navigation.registerComponent('example.collapsingReactViewTopTabsScreen', () => CollapsingReactViewTopTabsScreen);
  Navigation.registerComponent('example.header', () => Header);
  Navigation.registerComponent('example.LightBoxScreen', () => LightBoxScreen);
}
