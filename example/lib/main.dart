import 'package:bekenengine/beken.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  const App({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: OutlinedButton(
            onPressed: () {
              Beken.open(
                uuid: "JOQJT0ulegUoKg6bfNjwaITfQRy1",
                name: "Jhon Dose",
                email: "dose@mail.com",
                phoneNumber: "081100299988",
                publicKey: "w/t3emG0hfX1abxrPAHUHz+U6j28jyn7XlTBblP9dFfi7CxbX6/LYUOGieujdjNN",
                secretKey: "93FxV1E3jlyzy8YZLHguXAcbmHUjcPOfYTGMkyninx/B1yegKCXHGTtPjW8sgsXn",
                debug: true,
              );
            },
            style: OutlinedButton.styleFrom(
              backgroundColor: Colors.blue,
            ),
            child: Text(
              'OPEN BEKEN',
              style: TextStyle(
                color: Colors.white,
              ),
            ),
          ),
        ),
      ),
    );
  }
}
