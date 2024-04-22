import 'dart:io';

import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '/api/google_signin_api.dart';
import '../ideas_page.dart';
import '../sign_up_page.dart';
import 'package:dio/dio.dart';
import  '../../model/userStruct.dart';
import '../ideas_page.dart';
import 'package:image_picker/image_picker.dart';

class AddIdeas extends StatelessWidget {
  final String userID;
  final String session_id;
  AddIdeas({ Key? key, required this.userID, required this.session_id}) : super(key: key);
  String title ='';
  String idea='';
  String url =''; //add in url option
  String display_text='';
  String file_name='';
  //String file='';
  Future postIdea(String title, String idea, String uid, String url, String display_text, String file_name) 
    async{
      final api = 'https://dry-wave-47246.herokuapp.com/postidea';
      final data={
        'title':title,
        'idea':idea,
        'uid':uid,
        'url':url,
        'display_text': display_text,
        'file_name': file_name,
      };
      final dio =Dio();
      Response response;
      response = await dio.post(api,data:data);   
      if (response.statusCode ==200){
        final body = response.data;
        print('successful idea post');
        return 'successful idea post';
      } else{
        return '0';
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
                'Input Your IDEA',
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 32),
              TextField(
                decoration: InputDecoration(labelText: 'Title' ),
                onChanged: (val){
                  title =val;
                },
              ),
              TextField(
                decoration: InputDecoration(labelText: 'Idea' ),
                onChanged: (val)=> idea =val,
              ),
              TextField(
                decoration: InputDecoration(labelText: 'URL (Optional)' ),
                onChanged: (val){
                  url =val;
                },
              ),
              TextField(
                decoration: InputDecoration(labelText: 'Text for URL (hyperlink)' ),
                onChanged: (val){
                  display_text =val;
                },
              ),
              IconButton(
                          icon: const Icon(Icons.upload_file,),
                          onPressed:() async {
                            XFile? file = await ImagePicker().pickImage(source: ImageSource.gallery,);
                            //check if the file is null
                            if(file!=null){
                              Image.file(
                                File(file_name),
                              );
                            }
                          },
                        ),
              ElevatedButton(
                child: Text('Add Idea'),
                 onPressed: () async{ 
                  print('${title}, ${idea}, ${userID}, ${url}, ${display_text}, ${file_name}');
                  await postIdea(title, idea, userID, url, display_text, file_name);
                  Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) =>  IdeasPage(userID: userID,session_id: session_id,),));
                 }, 
                ),
            ],
          ),
        ),
      );
}
