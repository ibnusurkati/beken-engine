import 'dart:convert';
import 'dart:developer';

import 'package:flutter/services.dart';

class Beken {
  static const MethodChannel _channel = const MethodChannel('method_bekenengine');

  static void open({
    String uuid,
    String name = '',
    String email,
    String phoneNumber = '',
    String publicKey,
    String secretKey,
    bool debug = true,
  }) {
    if (uuid != null && uuid == '') {
      log("BEKEN : uuid not implemented!");
      return;
    }
    
    if (email != null && email == '') {
      log("BEKEN : email not implemented!");
      return;
    }
    
    if (publicKey != null && publicKey == '') {
      log("BEKEN : publicKey not implemented!");
      return;
    }
    
    if (secretKey != null && secretKey == '') {
      log("BEKEN : secretKey not implemented!");
      return;
    }

    _channel.invokeMethod(
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
