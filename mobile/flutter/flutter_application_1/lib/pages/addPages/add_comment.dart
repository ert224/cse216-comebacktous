import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/comment_page.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '/api/google_signin_api.dart';
import '../ideas_page.dart';
import '../sign_up_page.dart';
import 'package:dio/dio.dart';
import  '../../model/userStruct.dart';
import '../ideas_page.dart';

class AddComment extends StatelessWidget {
  final String userID;
  final int ideaID;
  final String username;
  final String session_id;
  AddComment({ Key? key, required this.ideaID, required this.userID, required this.username, required this.session_id}) : super(key: key);
  String comment ='';
  Future postComment(String comment, int ideaID, String uid) 
    async{
      final api = 'https://dry-wave-47246.herokuapp.com/comment';
      final data={
        'comment':comment,
        'id':ideaID,
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
          alignment: Alignment.center,
          color: Color.fromARGB(255, 247, 236, 249),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'Input Your comment',
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 10),
              TextField(
                decoration: InputDecoration(labelText: 'Comment' ),
                onChanged: (val){
                  comment =val;
                },
              ),
              ElevatedButton(
                child: Text('Post comment'),
                 onPressed: () async { 
                  print('${comment}, ${ideaID}, ${userID}');
                  await postComment(comment,ideaID, userID);
                  Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) =>CommentPage(ideaID: ideaID, userID: userID, username: username,session_id: session_id,)));
                 }, 
                ),
            ],
          ),
        ),
      );
}
