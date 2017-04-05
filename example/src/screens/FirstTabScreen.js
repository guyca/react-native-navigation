import React, {Component} from 'react';
import {
  Text,
  View,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Platform
} from 'react-native';
import {Navigation} from 'react-native-navigation';
import {iconsMap} from '../icons/icons';

export default class FirstTabScreen extends Component {
  // static navigatorButtons = {
  //   leftButtons: [{
  //     icon: require('../../img/navicon_menu.png'),
  //     id: 'sideMenu'
  //   }]
  // };
  static navigatorStyle = {
    navBarBackgroundColor: '#4dbce9',
    navBarTextColor: '#ffff00',
    navBarSubtitleTextColor: '#ff0000',
    navBarSubtitleColor: '#ff0000',
    navBarButtonColor: '#ffffff',
    statusBarTextColorScheme: 'light',
    tabBarBackgroundColor: '#4dbce9',
    tabBarButtonColor: '#ffffff',
    tabBarSelectedButtonColor: '#ffff00',
    titleBarDisabledButtonColor: '#ff0000',
    topBarElevationShadowEnabled: false
  };

  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  componentDidMount() {
    this.props.navigator.setButtons({
      rightButtons: [
        {
          title: 'Edit',
          id: 'edit',
          disabled: true
        },
        {
          icon: iconsMap['search'],
          id: 'search'
        },
        {
          icon: require('../../img/navicon_add.png'),
          id: 'add'
        }
      ]
    })
  }

  onNavigatorEvent(event) {
    console.log('event.id: ', JSON.stringify(event.id));
    if (event.id === 'sideMenu') {
      this.props.navigator.toggleDrawer({
        side: 'left',
        animated: true
      });
    }
    if (event.id === 'edit') {
      Alert.alert('NavBar', 'Edit button pressed');
    }
    if (event.id === 'add') {
      Alert.alert('NavBar', 'Add button pressed');
    }
  }

  render() {
    return (
      <View style={{flex: 1, padding: 20}}>
        <TouchableOpacity onPress={ this.onLightBoxPress.bind(this) }>
          <Text style={styles.button}>Show LightBox</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
          <Text style={styles.button}>Push Plain Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onPushStyledPress.bind(this) }>
          <Text style={styles.button}>Push Styled Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onModalPress.bind(this) }>
          <Text style={styles.button}>Show Modal Screen</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onReplacePrevious.bind(this) }>
          <Text style={styles.button}>replacePrevious</Text>
        </TouchableOpacity>

        {
          Platform.OS === 'android' ?
            <TouchableOpacity onPress={ this.onSetFabPress.bind(this) }>
              <Text style={styles.button}>Set Fab</Text>
            </TouchableOpacity> : false
        }

        {
          Platform.OS === 'android' ?
            <TouchableOpacity onPress={ this.onRemoveFabPress.bind(this) }>
              <Text style={styles.button}>Remove Fab</Text>
            </TouchableOpacity> : false
        }

        {
          Platform.OS === 'ios' ?
            <TouchableOpacity onPress={ this.onInAppNotificationPress.bind(this) }>
              <Text style={styles.button}>Show In-App Notification</Text>
            </TouchableOpacity> : false
        }

        <TouchableOpacity onPress={ this.onRemoveLeftButtonPress.bind(this) }>
          <Text style={styles.button}>Remove left button</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.setMenuButton.bind(this) }>
          <Text style={styles.button}>Set menu button</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onStartSingleScreenApp.bind(this) }>
          <Text style={styles.button}>Show Single Screen App</Text>
        </TouchableOpacity>

        {
          Platform.OS === 'android' ?
            <TouchableOpacity onPress={ this.showSnackbar.bind(this) }>
              <Text style={styles.button}>Show Snackbar</Text>
            </TouchableOpacity> : false
        }
        {
          Platform.OS === 'android' ?
            <TouchableOpacity onPress={ this.dismissSnackbar.bind(this) }>
              <Text style={styles.button}>Dismiss Snackbar</Text>
            </TouchableOpacity> : false
        }
      </View>
    );
  }

  showSnackbar() {
    this.props.navigator.showSnackbar({
      text: 'Hello from Snackbar',
      actionText: 'done',
      actionColor: 'green',
      textColor: 'red',
      actionId: 'fabClicked',
      backgroundColor: 'blue',
      duration: 'indefinite'
    })
  }

  dismissSnackbar() {
    this.props.navigator.dismissSnackbar();
  }

  onPushPress() {
    this.props.navigator.push({
      screen: "example.PushedScreen"
    });
  }

  onPushStyledPress() {
    this.props.navigator.push({
      title: "Styled",
      screen: "example.StyledScreen"
    });
  }

  onModalPress() {
    this.props.navigator.showModal({
      title: "Modal",
      screen: "example.ModalScreen",
      animationType: 'none'
    });
  }

  onReplacePrevious() {
    this.props.navigator.replacePrevious({
      title: "New Screen",
      screen: "example.ModalScreen"
    });
  }

  onSetFabPress() {
    this.props.navigator.setButtons({
      fab: {
        collapsedId: 'share',
        collapsedIcon: require('../../img/navicon_add.png'),
        backgroundColor: '#607D8B'
      }
    });
  }

  onRemoveFabPress() {
    this.props.navigator.setButtons({
      fab: {}
    });
  }

  onLightBoxPress() {
    this.props.navigator.showLightBox({
      screen: "example.LightBoxScreen",
      style: {
        backgroundBlur: "dark"
      },
      passProps: {
        greeting: 'hey there'
      },
    });
  }

  onInAppNotificationPress() {
    this.props.navigator.showInAppNotification({
      screen: "example.NotificationScreen"
    });
  }

  onRemoveLeftButtonPress() {
    this.props.navigator.setButtons({
      leftButtons: []
    });
  }

  setMenuButton() {
    this.props.navigator.setButtons({
      leftButtons: [{
        icon: require('../../img/navicon_menu.png'),
        id: 'sideMenu'
      }]
    });
  }

  onStartSingleScreenApp() {
    Navigation.startSingleScreenApp({
      screen: {
        screen: 'example.FirstTabScreen'
      },
      drawer: {
        left: {
          screen: 'example.SideMenu'
        }
      }
    });
  }
}

const styles = StyleSheet.create({
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop: 10,
    color: 'blue'
  }
});
