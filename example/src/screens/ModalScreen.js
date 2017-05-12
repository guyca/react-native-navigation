import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet
} from 'react-native';

export default class ModalScreen extends Component {
  static navigatorButtons = {
    leftButtons: [{
      title: 'Close',
      id: 'close'
    }]
  };
  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={ this.onShowLandscapeModalPress.bind(this) }>
          <Text style={styles.button}>Show Horizontal Modal</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onShowPortraitModalPress.bind(this) }>
          <Text style={styles.button}>Show Portrait Modal</Text>
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

        <TouchableOpacity onPress={ this.onClosePress.bind(this) }>
          <Text style={styles.button}>Close Modal</Text>
        </TouchableOpacity>

        <TouchableOpacity onPress={ this.onCloseAllPress.bind(this) }>
          <Text style={styles.button}>Close All Modals</Text>
        </TouchableOpacity>

      </View>
    );
  }
  onNavigatorEvent(event) {
    if (event.id ==='close') {
      this.props.navigator.dismissModal();
    }
  }
  onShowLandscapeModalPress() {
    this.props.navigator.showModal({
      screen: 'example.ModalScreen',
      navigatorStyle: {
        orientation: 'landscape'
      }
    });
  }
  onShowPortraitModalPress() {
    this.props.navigator.showModal({
      screen: 'example.ModalScreen',
      navigatorStyle: {
        orientation: 'portrait'
      }
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
  onPushPress() {
    this.props.navigator.push({
      title: "More",
      screen: "example.PushedScreen"
    });
  }
  onPushStyledPress() {
    this.props.navigator.push({
      title: "More",
      screen: "example.StyledScreen"
    });
  }
  onClosePress() {
    this.props.navigator.dismissModal();
  }
  onCloseAllPress() {
    this.props.navigator.dismissAllModals();
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    padding: 20
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
