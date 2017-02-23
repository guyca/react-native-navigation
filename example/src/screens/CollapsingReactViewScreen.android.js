import React, {Component} from 'react';
import {
  Text,
  View,
  TouchableOpacity,
  StyleSheet,
  Alert,
  Platform,
  ScrollView
} from 'react-native';
import {Navigation} from 'react-native-navigation';
import {iconsMap} from '../icons/icons';

export default class CollapsingReactViewScreen extends Component {
  static navigatorButtons = {
    leftButtons: [
      {
        id: 'back'
      }
    ]
  };

  static navigatorStyle = {
    navBarHideOnScroll: false,
    navBarBackgroundColor: '#4dbce9',
    navBarTextColor: '#ffff00',
    subtitleColor: '#ff0000',
    navBarButtonColor: '#ffffff',
    collapsingToolBarComponent: 'example.header',
    drawUnderTabBar: false,
    navBarTranslucent: false,
    collapsingToolBarCollapsedColor: 'green',
    collapsingToolBarExpendedColor: 'red',
    screenBackgroundColor: '#ECEFF1',
    showTitleWhenExpended: false
  };

  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
    this.props.navigator.setButtons({
      rightButtons: [
        {
          icon: iconsMap['alarm'],
          id: 'alarm'
        }
      ]
    })
  }

  onNavigatorEvent(event) {
    console.log('event.id: ', JSON.stringify(event.id));
  }

  render() {
    return (
      <ScrollView style={styles.container}>
        <View style={{flex: 1, backgroundColor: '#ffffff'}}>
          <Text style={styles.button}>Row 0</Text>
          <Text style={styles.button}>Row 1</Text>
          <Text style={styles.button}>Row 2</Text>
          <Text style={styles.button}>Row 3</Text>
          <Text style={styles.button}>Row 4</Text>
          <Text style={styles.button}>Row 5</Text>
          <Text style={styles.button}>Row 6</Text>
          <Text style={styles.button}>Row 7</Text>
          <Text style={styles.button}>Row 8</Text>

          <TouchableOpacity onPress={ this.onToggleBottomTabsPress.bind(this) }>
            <Text style={styles.button}>Toggle BottomTabs</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
            <Text style={styles.button}>Push Plain Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPushStyledPress.bind(this) }>
            <Text style={styles.button}>Push Styled Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPushStyled2Press.bind(this) }>
            <Text style={styles.button}>Push Styled Screen 2</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onModalPress.bind(this) }>
            <Text style={styles.button}>Show Modal Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onToggleNavBarPressed.bind(this) }>
            <Text style={styles.button}>Toggle Navigation Bar</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    );
  }

  onPushPress() {
    this.props.navigator.push({
      title: "More",
      screen: "example.PushedScreen"
    });
  }

  onToggleBottomTabsPress() {
    this.props.navigator.toggleTabs({
      to: 'hidden'
    });
  }

  onPushStyledPress() {
    this.props.navigator.push({
      title: "Styled",
      screen: "example.StyledScreen"
    });
  }

  onPushStyled2Press() {
    this.props.navigator.push({
      title: "Styled",
      titleImage: require('../../img/two.png'),
      screen: "example.StyledScreen"
    });
  }

  onModalPress() {
    this.props.navigator.showModal({
      title: "Modal",
      screen: "example.ModalScreen"
    });
  }

  onToggleNavBarPressed() {
    this.state.navBarVisibility = (this.state.navBarVisibility === 'shown') ? 'hidden' : 'shown';
    this.props.navigator.toggleNavBar({
      to: this.state.navBarVisibility,
      animated: true  // true is default
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
