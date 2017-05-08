import React, {Component} from 'react';
import {
  Text,
  ScrollView,
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
  //     icon: require('../../img/navicon_add@2x.android.png'),
  //     id: 'sideMenu'
  //   }]
  // };
  static navigatorStyle = {
    navBarBackgroundColor: '#4dbce9',
    navBarTextColor: '#ffff00',
    navBarSubtitleTextColor: '#ff0000',
    navBarSubtitleColor: '#ff0000',
    navBarButtonColor: 'red',
    statusBarTextColorScheme: 'light',
    tabBarBackgroundColor: '#4dbce9',
    tabBarButtonColor: '#ffffff',
    tabBarSelectedButtonColor: '#ffff00',
    titleBarDisabledButtonColor: '#ff0000',
    topBarElevationShadowEnabled: false
  };

  constructor(props) {
    super(props);
    this.buttonsCounter = 0;
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  componentDidMount() {
    // this.setCustomButtons();
  }

  setCustomButtons() {
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
      ],
      leftButtons: [
        {
          icon: iconsMap['face'],
          id: 'face'
        }
      ]
    })
  }

  setCustomLeftButton() {
    this.props.navigator.setButtons({
      leftButtons: [
        {
          icon: iconsMap['flight'],
          id: 'flight'
        }
      ]
    });
  }

  onNavigatorEvent(event) {
    console.log('style', JSON.stringify(event));
    if (event.type === 'DeepLink') {
      const parts = event.link.split('/');
      console.log('style: ', JSON.stringify(parts));
      if (parts[1] == 'updateStyle') {
        this.onSetStylePress();
      }
    }
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
      <ScrollView style={{flex: 1, padding: 20}}>
        <TouchableOpacity onPress={ this.onSetButtonsPress.bind(this) }>
          <Text style={styles.button}>Set Buttons</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.setCustomLeftButton.bind(this) }>
          <Text style={styles.button}>Set Custom Left Button</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onSetStylePress.bind(this) }>
          <Text style={styles.button}>Set Style</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onChangeButtonsPress.bind(this) }>
          <Text style={styles.button}>Change Buttons</Text>
        </TouchableOpacity>

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
      </ScrollView>
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

  onSetButtonsPress() {
    this.setCustomButtons();
  }

  onSetStylePress() {
    this.props.navigator.setStyle({
      navBarBackgroundColor: 'green',
      navBarButtonColor: 'purple',
      screenBackgroundColor: 'purple',
      statusBarColor: 'purple',
      navBarTextColor: 'blue',
      navBarSubtitleColor: 'white'
    })
  }

  onChangeButtonsPress() {
    let buttons;
    if (this.buttonsCounter % 3 === 0) {
      buttons = [
        {
          title: 'Edit',
          id: 'edit',
          disabled: true
        },
        {
          icon: require('../../img/navicon_add.png'),
          id: 'add'
        }
      ];
    } else if (this.buttonsCounter % 3 === 1) {
      buttons = [{
        title: 'Save',
        id: 'save'
      }];
    } else {
      buttons = [];
    }
    this.buttonsCounter++;

    this.props.navigator.setButtons({
      rightButtons: buttons,
      animated: true
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
