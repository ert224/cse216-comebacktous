import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '/api/google_signin_api.dart';
import 'package:dio/dio.dart';
import  '../model/userStruct.dart';

class AddIdeas extends StatelessWidget {
  final String userID;
  AddIdeas({ Key? key, required this.userID}) : super(key: key);
  String title ='';
  String idea='';
  Future postIdea(String title, String idea, String uid) 
    async{
      final api = 'https://dry-wave-47246.herokuapp.com/postidea';
      final data={
        'title':title,
        'idea':idea,
        'uid':uid
      };
      final dio =Dio();
      Response response;
      response = await dio.post(api,data:data);   
      if (response.statusCode ==200){
        final body = response.data;
        print('successful idea post');
        return 'successful idea post';
      } else{
        return null!;
      }
    }
  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: Text('MAZE'),
          centerTitle: true,
        ),
        body: Container(
        ),
      );
}
