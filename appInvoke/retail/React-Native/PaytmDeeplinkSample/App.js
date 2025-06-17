/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useState} from 'react';
import type {Node} from 'react';
import {
  Image,
  NativeEventEmitter,
  NativeModules,
  Platform,
  TextInput,
  ToastAndroid,
} from 'react-native';

const {DeepLinker} = NativeModules;
import {
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import {Colors, Header} from 'react-native/Libraries/NewAppScreen';

const Section = ({children, title}): Node => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
};

const App: () => Node = () => {
  const [result, setResult] = useState(null);
  let [amount, setAmount] = useState(null),
    [orderId, setOrderId] = useState(null),
    [payMode, setPayMode] = useState(null);
  const OnResult = value => {
    setResult(value);
  };
  function printReceipt() {
    if (result == null) {
      ToastAndroid.show('No Transaction for receipt. Please do a transaction.', 300);
      return;
    }
    DeepLinker.printTransactionReceipt(result);
  }
  function startPayment() {
    if (amount == null || amount === '') {
      ToastAndroid.show('Please input amount', 300);
      return;
    } else if (orderId == null || orderId === '') {
      ToastAndroid.show('Please input OrderId', 300);
      return;
    }
    if (Platform.OS === 'android') {
      DeepLinker.startPayment(amount, orderId, payMode);
    }
  }
  const eventEmitter = new NativeEventEmitter();
  eventEmitter.addListener('OnPaymentResponse', OnResult, null);
  const isDarkMode = useColorScheme() === 'dark';
  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };
  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
            padding: 10,
          }}>
          <Image
            source={require('./img/paytm_logo.png')}
            style={{alignSelf: 'center', width: 95, height: 30}}
          />
          <TextInput
            placeholder={'Amount'}
            keyboardType="numeric"
            style={{
              backgroundColor: isDarkMode ? '#222' : '#f0f0f0',
              color: isDarkMode ? '#fff' : '#000',
              borderRadius: 5,
              padding: 10,
              marginTop: 10,
              marginBottom: 10,
            }}
            onChangeText={newText => {
              // Only allow digits (no decimals)
              const numericText = newText.replace(/[^0-9]/g, '');
              setAmount(numericText);
            }}
          />
          <TextInput
            placeholder={'Order Id'}
            style={{
              backgroundColor: isDarkMode ? '#222' : '#f0f0f0',
              color: isDarkMode ? '#fff' : '#000',
              borderRadius: 5,
              padding: 10,
              marginBottom: 10,
            }}
            onChangeText={newText => {
              setOrderId(newText);
            }}
          />
          <TextInput
            placeholder={'Pay Mode'}
            style={{
              backgroundColor: isDarkMode ? '#222' : '#f0f0f0',
              color: isDarkMode ? '#fff' : '#000',
              borderRadius: 5,
              padding: 10,
              marginBottom: 10,
            }}
            onChangeText={newText => {
              setPayMode(newText);
            }}
          />
          <Button title="Start Payment" onPress={startPayment} />
          <View style={{marginTop: 10}} />
          <Button title="Print Receipt" onPress={printReceipt} />
          <Text style={{marginTop: 10, fontWeight: 'bold'}}>
            {result != null ? JSON.stringify(result) : ''}
          </Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
  amountInput: {
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
  },
});

export default App;
