import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet
} from 'react-native';

export default class PushedScreen extends Component {
  static navigatorStyle = {
    drawUnderTabBar: true,
    screenAnimationType: 'fade'
  };
  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) {
    if (event.id == 'tabSelected') {
      this.onTabSelected();
    }
  }

  onTabSelected() {

  }

  componentDidMount() {
    this.props.navigator.setButtons({
      fab: {
        collapsedId: 'share',
        collapsedIcon: require('../../img/navicon_add.png'),
        backgroundColor: 'blue'
      }
    });
  }

  render() {
    return (
      <ScrollView style={[styles.container, {backgroundColor: 'green'}]}>
        <View style={{flex: 1}}>
          <TouchableOpacity onPress={ this.onPushPress.bind(this) }>
            <Text style={styles.button}>Push Plain Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPushStyledPress.bind(this) }>
            <Text style={styles.button}>Push Styled Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPopPress.bind(this) }>
            <Text style={styles.button}>Pop Screen</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onPopToRootPress.bind(this) }>
            <Text style={styles.button}>Pop To Root</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onResetToPress.bind(this) }>
            <Text style={styles.button}>Reset To</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={ this.onReplacePrevious.bind(this) }>
            <Text style={styles.button}>replacePrevious</Text>
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
      title: "More",
      screen: "example.StyledScreen"
    });
  }
  onPopPress() {
    this.props.navigator.pop();
  }
  onPopToRootPress() {
    this.props.navigator.popToRoot();
  }
  onResetToPress() {
    this.props.navigator.resetTo({
      title: "New Root",
      screen: "example.FirstTabScreen"
    });
  }

  onReplacePrevious() {
    this.props.navigator.replacePrevious({
      title: "New Screen",
      screen: "example.ReplacedScreen"
    });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: 'white'
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
