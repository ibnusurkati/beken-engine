import React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import bekenEngine from '@ibnusurkati/beken-engine';

export default function App() {

  const openBeken = () => {
    bekenEngine.open({
      uuid: "0ea59ac5-c3fa-4c3d-bd3e-a5708cc09445",
      name: "Jhon Doe",
      email: "jhon-doe@mail.com",
      phoneNumber: "081234567890",
      publicKey: "d1a6849c4d1f4daea87740a0d04af27b67090fc16997424fbe12",
      secretKey: "04af27b67090fc16997424fbe12d1a6849c4d1f4daea87740a0d",
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
