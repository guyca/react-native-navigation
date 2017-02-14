import {Dimensions} from 'react-native';

// 5=320, 6=375, 6+=414
export const screenWidth = Dimensions.get('window').width;
export const screenHeight = Dimensions.get('window').height;

export const isSmallDevice = (screenWidth === 320);
