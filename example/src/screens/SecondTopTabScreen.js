import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert
} from 'react-native';

export default class SecondTopTabScreen extends Component {
  static navigatorStyle = {
    navBarHideOnScroll: true
  };

  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Add',
        icon: require('../../img/navicon_add.png'),
        id: 'add',
        showAsAction: 'never'
      }
    ]
  };

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) {

  }

  render() {
    return (
      <View style={styles.container}>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#42A5F5'
  }
});