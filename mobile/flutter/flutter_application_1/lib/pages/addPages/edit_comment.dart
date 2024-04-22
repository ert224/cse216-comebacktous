import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/ideas_page.dart';
import 'dart:developer' as developer;
import 'dart:convert';

import '../comment_page.dart';

final urlBase = 'https://dry-wave-47246.herokuapp.com';

class EditComment extends StatefulWidget {
  final String userID;
  final String session_id;
  final String tapUID;
  final int comID;
  final String comment;
  final String username;
  final int ideaID;
  const EditComment({super.key, required this.userID, required this.session_id, required this.ideaID, required this.tapUID, required this.comID, required this.comment, required this.username} );
  @override
  State<EditComment> createState() => _EditCommentState();
}

class _EditCommentState extends State<EditComment> {
  String holdcomment = '';
  @override
  Widget build(BuildContext context) {
    //TextEditingController _textController;
    return Scaffold(
      appBar: AppBar(
        title: Text('MAZE'),
      ), // for comments
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 15),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
                SizedBox(height: 20),
                Expanded(
                  child: FutureBuilder(
                    future:  getProfile(widget.userID),
                    builder: (context,snapshot){
                      if(!snapshot.hasData){
                        return Center(
                          child: CircularProgressIndicator(),
                        );
                      } else{
                        // _textController = TextEditingController(text: snapshot.data?[0]['username'] ?? '');
                        return Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                           children: [
                              Text(
                                '@${widget.username} comment:',
                                style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Colors.purple,
                                ),
                              ),
                              SizedBox(height: 8),
                              SizedBox(height: 8),
                              TextField(
                                 controller: TextEditingController(text: widget.comment ?? ''),
                                  decoration: InputDecoration(
                                  labelText: 'Edit comment: ',
                                  ),
                                onChanged: (val){
                                  holdcomment =val;
                                },
                              ),
                              SizedBox(height: 8),
                              
                        SizedBox(height: 15),
                        Center(
                          child: FloatingActionButton.extended(
                              onPressed: () async {
                                if (holdcomment==''){
                                  holdcomment=widget.comment;
                                }
                                print('in edit comment');
                                print('${widget.tapUID}, ${widget.comID}, ${holdcomment}, ${widget.username}, ${widget.session_id}');
                                await updateComment(widget.tapUID, widget.comID, holdcomment,widget.username,widget.session_id);
                                await Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => CommentPage(ideaID: widget.ideaID,userID: widget.userID,username:widget.username,session_id: widget.session_id,)));
                                final int ideaID;
  // final String userID;
  // final String username;
  // final String session_id;
  // const CommentPage({super.key, required this.ideaID, required this.userID,required this.username, required this.session_id});
                              },
                              label: const Text('Submit Changes'),
                              icon: const Icon(Icons.message),
                            ),
                        ),
                    ]
                    );
                      }
                    },
                  ),
                
                ),
            ],
          )
        ),
      ),
      // floatingActionButton: FloatingActionButton.extended(
      //   onPressed: () async {
      //     var uid = '111371442766850107091';
      //     await updateUser(widget.userID, username, email, fname, lname, gi, so, note);
      //     // await Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => IdeasPage());
      //   },
      //   label: const Text('Submit Changes'),
      //   icon: const Icon(Icons.message),
      // ),
      // floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
    );
  }
  
  Future<dynamic> getProfile(String uid) async{
    try{
      final response = await Dio().get(urlBase+"/user/$uid");
      var hold = [response.data['mData']];
      // print(hold);
      return hold;
    }on DioError catch(e){
      print(e);
      return null!;
    }
  }

  Future updateComment(String tapUID, int comID,String comment, String username, String session_id) 
  async{
    final api = urlBase+"/editcomment";
    final data={
      'cid': comID,
      'comment': comment,
      'uid': tapUID,
      'username':username,
      'session_id': session_id
    };
    final dio =Dio();
    print(data);
    Response response;
    response =await dio.put(api,data:data); 
    print('update user response');
    print(response); 
    if (response.statusCode ==200){
      final body = response.data;
    } else{
      return null!;
    }
  }
}