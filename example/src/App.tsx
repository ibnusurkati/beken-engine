import React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import bekenEngine from '@ibnusurkati/beken-engine';

export default function App() {

  const openBeken = () => {
    bekenEngine.open({
      uuid: "TDYTwBHwMQMjVFEnNXHRPHnPf402",
      name: "Jhon Doe",
      email: "jhon-doe@mail.com",
      phoneNumber: "081234567890",
      publicKey: "XSaLrfFGrGp02NEEaCstv2oMDkhvA8Iz12odB1i6eIdaz0DwglpXjVuO4No/r8Iq",
      secretKey: "/wqlzn6CdYXaSLTXI58FHELl5p8xYMZqhmEhdg/SAWRtPM5ubJiow2lO5gpdgQOA",
      debug: true
    })
  }

  return (
    <View style={styles.container}>
      <Text style={{ marginBottom: 8 }}>Welcome to exmplae beken react native</Text>
      <Button
        onPress={openBeken}
        title="Open Beken"
        color="#841584"
        accessibilityLabel="Learn more about this purple button"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
