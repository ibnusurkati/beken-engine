import 'dart:convert';

import 'package:bekenengine/beken.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  const App({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Beken.eventChannel.receiveBroadcastStream().listen((event) {
      Beken.push(jsonEncode({
        "status": true,
        "product": "KRMTUNAI",
        "type": "transaction",
        "data": jsonEncode({
          "success": true,
          "reffid": 'QWEASD1234'
        }),
      }));
    });

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: OutlinedButton(
            onPressed: () {
              Beken.open(
                uuid: "testing-081100299988",
                name: "Jhon Dose",
                email: "dose@mail.com",
                phoneNumber: "081100299988",
                publicKey: "43YMUV5NB4WE6MLBHYXIPJKAAZDOQCL5RK24EEKETV77TP5GU2HXYAGLAKO2IYVX",
                secretKey: "P5ALDNSKEGAIGMM3NHBXKCUMICJG4KUNVYNB2EWTWM24IV76UHRPYEWA3HSVAP4S",
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
