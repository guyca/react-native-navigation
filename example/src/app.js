import {Navigation} from 'react-native-navigation';

// screen related book keeping
import {registerScreens} from './screens';
registerScreens();

// this will start our app
Navigation.startTabBasedApp({
  tabs: [
    {
      label: 'Collapsing',
      screen: 'example.CollapsingTopBarScreen',
      icon: require('../img/one.png'),
      title: 'Collapsing TopBar example',
    },
    {
      label: 'One',
      screen: 'example.FirstTabScreen',
      icon: require('../img/one.png'),
      selectedIcon: require('../img/one_selected.png'),
      title: 'Screen One',
      navigatorStyle: {
        navBarBackgroundColor: '#4dbce9',
        navBarTextColor: '#ffff00',
        navBarSubtitleTextColor: '#ff0000',
        navBarButtonColor: '#ffffff',
        statusBarTextColorScheme: 'light',
        tabBarBackgroundColor: '#4dbce9',
        tabBarButtonColor: '#ffffff',
        tabBarSelectedButtonColor: '#ffff00'
      }
    },
    {
      label: 'Two',
      screen: 'example.SecondTabScreen',
      icon: require('../img/two.png'),
      selectedIcon: require('../img/two_selected.png'),
      title: 'Screen Two',
      navigatorStyle: {
        tabBarBackgroundColor: '#4dbce9',
        tabBarButtonColor: '#ffffff',
        tabBarSelectedButtonColor: '#ffff00'
      }
    }
  ],
  appStyle: {
    tabBarBackgroundColor: '#0f2362',
    tabBarButtonColor: '#ffffff',
    tabBarSelectedButtonColor: '#63d7cc'

  }
});
// Navigation.startSingleScreenApp({
//   screen: {
//     screen: 'example.FirstTabScreen',
//     title: 'Navigation',
//     navigatorStyle: {
//       navBarBackgroundColor: '#4dbce9',
//       navBarTextColor: '#ffff00',
//       navBarSubtitleTextColor: '#ff0000',
//       navBarButtonColor: '#ffffff',
//       statusBarTextColorScheme: 'light'
//     }
//   },
//   drawer: {
//     left: {
//       screen: 'example.SideMenu'
//     }
//   }
// });
