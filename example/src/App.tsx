import React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import bekenEngine from '@ibnusurkati/beken-engine';

export default function App() {

  const openBeken = () => {
    bekenEngine.open({
      uuid: "JOQJT0ulegUoKg6bfNjwaITfQRy1",
      name: "Jhon Doe",
      email: "jhon-doe@mail.com",
      phoneNumber: "081100299988",
      publicKey: "w/t3emG0hfX1abxrPAHUHz+U6j28jyn7XlTBblP9dFfi7CxbX6/LYUOGieujdjNN",
      secretKey: "93FxV1E3jlyzy8YZLHguXAcbmHUjcPOfYTGMkyninx/B1yegKCXHGTtPjW8sgsXn",
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
