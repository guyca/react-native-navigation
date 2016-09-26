import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';

export default class ThirdTabScreen extends Component {
  static navigatorStyle = {
    drawUnderTabBar: true,
    navBarButtonColor: '#ffffff',
    navBarTextColor: '#ffffff'
  };

  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Edit',
        id: 'edit'
      },
      {
        icon: require('../../img/navicon_add.png'),
        id: 'add'
      }
    ]
  };

  constructor(props) {
    super(props);
    this.state = {
      navBarVisability: 'shown'
    }
  }
  render() {
    return (
        <ScrollView style={styles.container}>
          <View style={{flex: 1, backgroundColor: '#ffffff'}}>
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
  onPushStyledPress() {
    this.props.navigator.push({
      title: "Styled",
      screen: "example.StyledScreen"
    });
  }
  onPushStyled2Press () {
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
    this.state.navBarVisability = (this.state.navBarVisability === 'shown') ? 'hidden' : 'shown';
    this.props.navigator.toggleNavBar({
      to: this.state.navBarVisability,
      animated: true  // true is default
    });
  }

  componentDidUpdate() {
    console.error('this is an error: ' + Math.random());
    this.state.navBarState = 'shown';
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#eeeeee'
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:30,
    color: 'blue'
  }
});
