import React, {Component} from 'react';
import {
  Text,
  View,
  TouchableOpacity,
  ScrollView,
  ListView,
  StyleSheet,
  ActivityIndicator,
  Alert,
  Platform
} from 'react-native';
import {Navigation} from 'react-native-navigation';

export default class CollapseBugScreen extends Component {
  static navigatorStyle = {
    navBarHideOnScroll: false,
    navBarBackgroundColor: '#4dbce9',
    collapsingToolBarComponent: 'example.Header',
    drawUnderTabBar: false,
    collapsingToolBarCollapsedColor: 'green',
    collapsingToolBarExpendedColor: 'red',
    screenBackgroundColor: '#ECEFF1',
    showTitleWhenExpended: false
  };

  constructor(props) {
    super(props);
    this.state = {
      loaded: false
    };
    this.delayedSetData();
  }

  delayedSetData() {
    const setData = () => {
      const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
      this.setState({
        dataSource: ds.cloneWithRows(this._genRows({})),
        loaded: true
      });
    };
    setTimeout(setData, 1700);
  }

  _genRows() {
    const dataBlob = [];
    for (let ii = 0; ii < 100; ii++) {
      dataBlob.push({
        title: 'Row ' + ii
      });
    }
    return dataBlob;
  }

  _renderRow(rowData, sectionID, rowID) {
    return (<Text>{rowData.title}</Text>);
  }

  render() {
    console.log('CollapseBugScreen', 'render ', this.state.loaded);
    if(!this.state.loaded) {
      return (
        <ScrollView contentContainerStyle={styles.scrollView}>
          <ActivityIndicator
            animating={true}
            style={{height: 80}}
            size="large"
            color="#406C97"
          />
        </ScrollView>
      );
    }

    return (
      <ListView
        style={{backgroundColor: '#EFEBE9'}}
        dataSource={this.state.dataSource}
        renderRow={this._renderRow}
        enableEmptySections={true}
        prerenderingSiblingsNumber={1}
      />
    );
  }
}

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: '#eee',
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center'
  }
});
