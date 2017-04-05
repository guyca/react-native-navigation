import {
  Platform
} from 'react-native';
import {Navigation} from 'react-native-navigation';

// screen related book keeping
import {registerScreens} from './screens';
registerScreens();

import { iconsMap, iconsLoaded } from './icons/icons';
iconsLoaded.then(() => {
  startApp();
});

// this will start our app
const startApp = () => {
  Navigation.startTabBasedApp({
    tabs: createTabs(),
    appStyle: {
      tabBarBackgroundColor: '#0f2362',
      tabBarButtonColor: '#ffffff',
      tabBarSelectedButtonColor: '#63d7cc',
      screenBackgroundColor: '#42A5F5',
    },
    drawer: {
      left: {
        screen: 'example.LeftSideMenu'
      },
      right: {
        screen: 'example.RightSideMenu'
      }
    }
  });
};

const firstTabScreen = {
  label: 'One',
  screen: 'example.FirstTabScreen',
  icon: require('../img/one.png'),
  selectedIcon: require('../img/one_selected.png'),
  title: 'Screen One',
  subtitle: 'subsub'
};
const secondTabScreen = {
  label: 'Two',
  screen: 'example.SecondTabScreen',
  icon: require('../img/two.png'),
  selectedIcon: require('../img/two_selected.png'),
  title: 'Screen Two',
  navigatorStyle: {
    tabBarBackgroundColor: '#4dbce9',
  }
};

const createTabs = () => {
  const collapsingReactScreen = {
    label: 'Club',
    screen: 'example.collapsingReactViewScreen',
    icon: iconsMap['face'],
    title: 'Collapsing React View'
  };
  const collapsingReactTopTabsScreen = {
    label: 'Clubs',
    screen: 'example.collapsingReactViewTopTabsScreen',
    icon: iconsMap['flight'],
    title: 'Collapsing React TopTabs View',
    topTabs: [
      {
        screenId: 'example.ListScreen',
        icon: require('../img/list.png'),
        passProps: {
          onTabSelected: (navigator) => navigator.setTitle('List')
        }
      },
      {
        screenId: 'example.PushedScreen',
        icon: require('../img/list.png'),
        passProps: {
          onTabSelected: (navigator) => navigator.setTitle('Screen 2')
        }
      },
      {
        screenId: 'example.PushedScreen',
        icon: require('../img/one.png'),
        passProps: {
          onTabSelected: (navigator) => navigator.setTitle('Screen 3')
        }
      }
    ]
  };
  // let tabs = [
  //   collapsingReactTopTabsScreen
  // ];
  // let tabs = [
  //   collapsingReactScreen,
  //   collapsingReactTopTabsScreen
  // ];
  let tabs = [
    firstTabScreen,
    secondTabScreen
  ];
  if (Platform.OS === 'android') {
    tabs.push({
      label: 'List',
      screen: 'example.ListScreen',
      icon: require('../img/list.png'),
      title: 'List'
    });
    tabs.push({
      label: 'Collapsing',
      screen: 'example.CollapsingTopBarScreen',
      icon: require('../img/one.png'),
      title: 'Collapsing',
    });
    tabs.push({
      title: 'TopTabs',
      screen: 'example.TopTabsScreen',
      icon: require('../img/list.png'),
      topTabs: [
        {
          screenId: 'example.PushedScreen',
          icon: require('../img/list.png')
        },
        {
          screenId: 'example.PushedScreen',
          icon: require('../img/one.png')
        },
        {
          screenId: 'example.ListScreen',
          icon: require('../img/list.png')
        }
      ],
    })
  }
  return tabs;
};
