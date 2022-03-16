import 'dart:convert';
import 'dart:developer';

import 'package:flutter/services.dart';

class Beken {
  static const String tag = 'BEKEN';
  static const EventChannel eventChannel = const EventChannel('event_bekenengine');
  static const MethodChannel _methodChannel = const MethodChannel('method_bekenengine');

  static void push(String data) {
    _methodChannel.invokeMethod('push', data);
  }

  static void open({
    required String uuid,
    String name = '',
    required String email,
    String phoneNumber = '',
    required String publicKey,
    required String secretKey,
    bool debug = true,
  }) {
    if (uuid == '') {
      log("$tag : uuid not implemented!");
      return;
    }
    
    if (email == '') {
      log("$tag : email not implemented!");
      return;
    }
    
    if (publicKey == '') {
      log("$tag : publicKey not implemented!");
      return;
    }
    
    if (secretKey == '') {
      log("$tag : secretKey not implemented!");
      return;
    }

    _methodChannel.invokeMethod(
      'open',
      jsonEncode(
        {
          "uuid": uuid,
          "name": name,
          "email": email,
          "phoneNumber": phoneNumber,
          "publicKey": publicKey,
          "secretKey": secretKey,
          "debug": debug,
        },
      ),
    );
  }
}
