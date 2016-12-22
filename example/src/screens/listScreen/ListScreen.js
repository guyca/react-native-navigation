import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  TouchableWithoutFeedback ,
  StyleSheet,
  ListView,
  Image
} from 'react-native';
import heroes from './heroes';
import {SharedElementTransition} from 'react-native-navigation';

const LOREM_IPSUM = 'Lorem ipsum dolor sit amet, ius ad pertinax oportere accommodare, an vix civibus corrumpit referrentur. Te nam case ludus inciderint, te mea facilisi adipiscing. Sea id integre luptatum. In tota sale consequuntur nec. Erat ocurreret mei ei. Eu paulo sapientem vulputate est, vel an accusam intellegam interesset. Nam eu stet pericula reprimique, ea vim illud modus, putant invidunt reprehendunt ne qui.';
const hashCode = function(str) {
  var hash = 15;
  for (var ii = str.length - 1; ii >= 0; ii--) {
    hash = ((hash << 5) - hash) + str.charCodeAt(ii);
  }
  return hash;
};

export default class ListScreen extends Component {

  static navigatorStyle = {
    navBarHideOnScroll: true,
    screenBackgroundColor: '#90CAF9',
    drawUnderTabBar: true
  };

  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Add',
        icon: require('../../../img/navicon_add.png'),
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
      collapsedIcon: require('../../../img/navicon_add.png'),
      backgroundColor: '#607D8B'
    }
  };

  constructor(props) {
    super(props);
    this._onRowPress = this._onRowPress.bind(this);
    this._renderRow = this._renderRow.bind(this);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
    this.sharedElements = {};

    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
    this.state = {
      dataSource: ds.cloneWithRows(this._genRows({}))
    }
  }

  onNavigatorEvent(event) {

  }

  render() {
    return (
      <ListView
        dataSource={this.state.dataSource}
        renderRow={(rowData, sectionID, rowID) => this._renderRow(rowData, sectionID, rowID)}/>
    );
  }

  _renderRow(rowData, sectionID, rowID) {
    const rowHash = Math.abs(hashCode(rowData.rowTitle));
    const sharedElementId = "image" + rowID;

    return (
      <TouchableWithoutFeedback
        style={styles.row}
        onPress={() => this._onRowPress({
          rowData,
          sharedElementId
        })}
      >
        <View style={{flexDirection: 'row'}}>
          <View>
            <SharedElementTransition
              key={sharedElementId}
              sharedElementId={sharedElementId}
              ref={(r) => this.sharedElements[sharedElementId] = r}
              style={styles.imageContainer}
            >
              <Image
                source={rowData.icon}
                style={styles.heroIcon}
              />
            </SharedElementTransition>
          </View>
          <Text style={styles.text}>
            {rowData.rowTitle + ' - ' + LOREM_IPSUM.substr(0, rowHash % 301 + 10)}
          </Text>
        </View>
      </TouchableWithoutFeedback>
    );
  }

  _onRowPress(data) {
    const {rowData} = data;
    this.props.navigator.push({
      screen: 'example.HeroScreen',
      sharedElements: [
        {
          sharedElementId: data.sharedElementId,
          ref: this.sharedElements[data.sharedElementId]
        }
      ],
      animated: false,
      passProps: {
        header: rowData.header,
        sharedIconId: data.sharedElementId,
        icon: rowData.icon
      }
    });
  }

  _genRows() {
    const dataBlob = [];
    for (let ii = 0; ii < 100; ii++) {
      dataBlob.push({
        rowTitle: 'Row ' + ii + ' ',
        icon: heroes[ii % 5].icon,
        header: heroes[ii % 5].header
      });
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
    padding: 5,
    height: 110,
    flexDirection: 'row',
    justifyContent: 'center',
    borderWidth: 1
  },
  imageContainer: {
    justifyContent: 'center'
  },
  heroIcon: {
    width: 100,
    height: 100,
    resizeMode: 'contain'
  },
  text: {
    flex: 1,
    marginLeft: 5
  }
});


