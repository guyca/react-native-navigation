import React, {Component} from 'react';
import {
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Image,
} from 'react-native';
import {SharedElementTransition} from 'react-native-navigation';
import { createAnimatableComponent, View, Text } from 'react-native-animatable';
import * as heroStyles from './styles';

export default class HeroScreen extends Component {
  static navigatorStyle = {
    ...heroStyles.navigatorStyle
  };

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
    this.state = {
      animationType: 'fadeIn'
    };
  }

  render() {
    return (
      <View style={[styles.container]}>
        {this._renderHeader()}
        {this._renderContent()}
        {this._renderIcon()}
      </View>
    );
  }

  _renderHeader() {
    return (
      <View animation={this.state.animationType} duration={300} style={[styles.header, heroStyles.primaryDark]} useNativeDriver>
        <Text style={styles.title}>{this.props.title}</Text>
      </View>
    );
  }

  _renderContent() {
    return (
      <View animation={this.state.animationType} duration={300} style={[styles.body, heroStyles.primaryLight]} useNativeDriver/>
    );
  }

  _renderIcon() {
    return (
      <SharedElementTransition
        key={this.props.sharedIconId}
        sharedElementId={this.props.sharedIconId}
        style={styles.iconContainer}
        interpolation={
            {
              show: {
                type: 'path',
                controlX1: '0.5',
                controlY1: '1',
                controlX2: '0',
                controlY2: '0.5',
                easing: 'accelerateDecelerate'
              },
              hide: {
                type: 'path',
                controlX1: '0.5',
                controlY1: '0',
                controlX2: '1',
                controlY2: '0.5',
                easing:'accelerate'
              }
            }
          }
      >
        <Image
          source={this.props.icon}
          style={styles.heroIcon}
        />
      </SharedElementTransition>
    );
  }

  onNavigatorEvent(event) {
    if (event.id === 'backPress') {
      this.setState({
        animationType: 'fadeOut'
      });
      this.props.navigator.pop();
    }
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    backgroundColor: 'transparent'
  },
  header: {
    flex: 1,
    justifyContent: 'flex-end',
    width: undefined
  },
  title: {
    fontSize: 25,
    left: 124,
    marginBottom: 8,
    ...heroStyles.textLight
  },
  iconContainer: {
    position: 'absolute',
    top: 65,
    left: 30
  },
  heroIcon: {
    width: 90,
    height: 90,
    resizeMode: 'contain'
  },
  body: {
    flex: 4
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop: 10,
    color: 'blue'
  }
});
