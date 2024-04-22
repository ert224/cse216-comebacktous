import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import '/pages/sign_up_page.dart';
import 'dart:async';
import 'package:flutter/services.dart';
import 'pages/ideas_page.dart';
import 'pages/profile_page.dart';
import 'pages/addPages/edit_comment.dart';
import 'pages/viewProfile_page.dart';
import 'package:image_picker/image_picker.dart';

Future main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
  ]);
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'MAZE',
        theme: ThemeData(
          // This is the theme of your application.
          //
          // Try running your application with "flutter run". You'll see the
          // application has a blue toolbar. Then, without quitting the app, try
          // changing the primarySwatch below to Colors.green and then invoke
          // "hot reload" (press "r" in the console where you ran "flutter run",
          // or simply save your changes to "hot reload" in a Flutter IDE).
          // Notice that the counter didn't reset back to zero; the application
          // is not restarted.
          primarySwatch: Colors.purple,
        ),
//   required this.userID, required this.session_id, required this.tapUID, required this.comID, required this.comment, required this.username} );
        // home: EditComment(userID: '111371442766850107091',session_id: '2537', tapUID: '111371442766850107091',comID: 35,comment: "boi",username: "ert224"), 
        home: SignUpPage(),
        // home: ViewProfile(userID: '111371442766850107091',session_id: '2537'),
      );
  }
}