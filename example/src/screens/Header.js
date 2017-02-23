import React, {Component} from 'react';
import {
  Text,
  View,
  Image,
  TouchableOpacity,
  StyleSheet,
  PixelRatio,
  Alert,
  Platform,
  TouchableHighlight
} from 'react-native';
import {Navigation} from 'react-native-navigation';
import {screenWidth} from '../utils/deviceSize';

export default class Header extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <View style={styles.header}>
        <Image
          style={styles.image}
          source={require('../../img/dwtd.png')}
          resizeMode={'cover'}
        />
        <View style={styles.content}>
          <Text style={styles.title}>React view w00t ^^</Text>

          <TouchableHighlight style={styles.buttonContainer} underlayColor={'transparent'} onPress={this.onPress.bind(this)}>
            <View style={styles.buttonWrapper}>
              <Text style={styles.buttonText}>La Button</Text>
            </View>
          </TouchableHighlight>
        </View>
      </View>
    );
  }

  onPress() {
    console.log('Header', 'onPress');
  }
}

const styles = StyleSheet.create({
  header: {
    backgroundColor: '#673AB7'
  },
  image: {
    height: 200
  },
  content: {
    backgroundColor: '#0f2362',
    flexDirection: 'column',
    padding: 16
  },
  title: {
    fontSize: 23,
    color: 'white'
  },
  buttonContainer: {
    width: 100,
    alignSelf: 'flex-end'
  },
  buttonWrapper: {
    backgroundColor: 'white',
    borderRadius: 10,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center'
  },
  buttonText: {
    color: '#673AB7',
    textAlign: 'center',
    fontSize: 17,
    fontWeight: 'normal',
  }
});
