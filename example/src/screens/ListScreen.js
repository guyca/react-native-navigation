import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  ListView,
} from 'react-native';

const LOREM_IPSUM = 'Lorem ipsum dolor sit amet, ius ad pertinax oportere accommodare, an vix civibus corrumpit referrentur. Te nam case ludus inciderint, te mea facilisi adipiscing. Sea id integre luptatum. In tota sale consequuntur nec. Erat ocurreret mei ei. Eu paulo sapientem vulputate est, vel an accusam intellegam interesset. Nam eu stet pericula reprimique, ea vim illud modus, putant invidunt reprehendunt ne qui.';
const hashCode = function(str) {
  var hash = 15;
  for (var ii = str.length - 1; ii >= 0; ii--) {
    hash = ((hash << 5) - hash) + str.charCodeAt(ii);
  }
  return hash;
};

export default class FirstTopTabScreen extends Component {

  static navigatorStyle = {
    navBarHideOnScroll: true,
    screenBackgroundColor: '#90CAF9',
    drawUnderTabBar: true
  };

  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Add',
        icon: require('../../img/navicon_add.png'),
        id: 'add',
        showAsAction: 'always'
      },
      {
        title: 'All',
        id: 'all',
        showAsAction: 'never'
      },
      {
        title: 'Upcoming',
        id: 'upcoming',
        showAsAction: 'never'
      },
      {
        title: 'My',
        id: 'my',
        showAsAction: 'never'
      },
      {
        title: 'Participant',
        id: 'participant',
        showAsAction: 'never'
      },
    ],
    fab: {
      collapsedId: 'share',
      collapsedIcon: require('../../img/navicon_add.png'),
      backgroundColor: '#607D8B'
    }
  };

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));

    var ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
    this.state = {
      dataSource: ds.cloneWithRows(this._genRows({}))
    }
  }

  onNavigatorEvent(event) {
    if (event.id == 'tabSelected') {
      this.onTabSelected();
      return;
    }
  }

  onTabSelected() {

  }

  render() {
    return (
      <ListView
        dataSource={this.state.dataSource}
        renderRow={this._renderRow}/>
    );
  }

  _renderRow(rowData, sectionID, rowID) {
    const rowHash = Math.abs(hashCode(rowData));
    return (
      <View style={styles.row}>
        <Text style={styles.text}>
          {rowData + ' - ' + LOREM_IPSUM.substr(0, rowHash % 301 + 10)}
        </Text>
      </View>
    );
  }

  _genRows() {
    var dataBlob = [];
    for (var ii = 0; ii < 100; ii++) {
      dataBlob.push('Row ' + ii + ' ');
    }
    return dataBlob;
  }

  _renderSeparator(sectionID, rowID) {
    return (
      <View
        key={`${sectionID}-${rowID}`}
        style={{
          height: 1,
          backgroundColor: rowID % 2 == 0 ? '#3B5998' : '#CCCCCC'
        }}
      />
    );
  }
}

const styles = StyleSheet.create({
  row: {
    flexDirection: 'row',
    justifyContent: 'center',
    padding: 12
  },
  text: {
    flex: 1
  }
});


