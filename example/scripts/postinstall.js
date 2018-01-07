const cp = require('child_process');
cp.execSync(`rm -rf node_modules/react-native-navigation/node_modules`);
cp.execSync(`rm -rf node_modules/react-native-navigation/example `);
// HACK: Replace react-native dependency from maven with node_modules copy
cp.execSync(`find . -type f -name build.gradle -not -path \"*/react-native/*\" -not -path \"*/Examples/*\" -not -path \"*/examples/*\" -print0 -exec sed -i '' \"s/.*com.facebook.react:react-native:.*/ compile project(':ReactAndroid')/\" {} +`);
