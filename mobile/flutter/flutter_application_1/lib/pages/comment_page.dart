import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/addPages/edit_comment.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import '../model/commentStruct.dart';
import 'addPages/add_comment.dart';
import 'addPages/edit_comment.dart';
import 'ideas_page.dart';

final urlBase = 'https://dry-wave-47246.herokuapp.com/ideas/idea';

class CommentPage extends StatefulWidget {
  final int ideaID;
  final String userID;
  final String username;
  final String session_id;
  const CommentPage({super.key, required this.ideaID, required this.userID,required this.username, required this.session_id});
  @override
  State<CommentPage> createState() => _CommentPageState();
}

class _CommentPageState extends State<CommentPage> {
  @override
  Widget build(BuildContext context) {
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
              Text(
                'Comments',
                style: TextStyle(
                  fontSize: 23,
                  fontWeight: FontWeight.bold,
                  color: Colors.purple,
                  ),
                ),
                SizedBox(height: 20),
                Expanded(
                  child: FutureBuilder(
                    future: getComments(widget.ideaID),
                    builder: (context,snapshot){
                      if(!snapshot.hasData){
                        return Center(
                          child: CircularProgressIndicator(),
                        );
                      }else{
                        return ListView.builder(
                          itemCount: snapshot.data?.length,
                          itemBuilder: (context, index){
                            return widget.userID==snapshot.data?[index]['uid']?Card(
                              // widget.userID==snapshot.data?[index]['uid']
                              color: Color.fromARGB(255, 230, 217, 233),
                              child: ListTile(
                              title: Text(
                                "@${snapshot.data?[index]['username']}",
                                style: TextStyle(
                                    color: Color.fromARGB(255, 142, 15, 164),
                                  ),
                                ),
                              subtitle: Text(snapshot.data?[index]['comment'],
                                  style: TextStyle(
                                    color: Color.fromARGB(255, 85, 9, 98),
                                  ),
                                  ),
                              trailing: IconButton(
                                    onPressed: () async {
                                          print('in comment page');
                                          print('${widget.userID},  ${widget.session_id},\n ${snapshot.data?[index]['uid']},\n ${snapshot.data![index]['id']},\n ${snapshot.data?[index]['comment']},  ${snapshot.data?[index]['username']}');
                                          Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => 
                                          EditComment(
                                            ideaID: widget.ideaID,
                                            userID: widget.userID,
                                            session_id: widget.session_id,
                                            tapUID: snapshot.data?[index]['uid'],
                                            comID: snapshot.data![index]['id'],
                                            comment: snapshot.data?[index]['comment'], 
                                            username: snapshot.data?[index]['username'],)));
                                      },
                                      icon: const Icon(
                                      Icons.edit_outlined,
                                      color: Color.fromARGB(255, 90, 9, 105),
                                        ),
                                  ),
                              )
                            ): Card(
                              // widget.userID==snapshot.data?[index]['uid']
                              color: Color.fromARGB(255, 230, 217, 233),
                              child: ListTile(
                              title: Text(
                                "@${snapshot.data?[index]['username']}",
                                style: TextStyle(
                                    color: Color.fromARGB(255, 142, 15, 164),
                                  ),
                                ),
                              subtitle: Text(snapshot.data?[index]['comment'],
                                  style: TextStyle(
                                    color: Color.fromARGB(255, 85, 9, 98),
                                  ),
                                  ),
                              )
                            );
                            
                          },
                          );
                      }
                    },
                  ),
                
                ),
                
            ],
          )
        ),
      ),
      floatingActionButton: Stack(
            children: <Widget>[
              Align(
                alignment: Alignment(1, -0.68),
                child:
                         FloatingActionButton.extended(
                          onPressed: () async {
                            await Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) =>
                              AddComment(ideaID: widget.ideaID, userID: widget.userID, username: widget.username, session_id: widget.session_id,),
                            ));
                          },
                          label: const Text(
                            'Comment',
                             style: TextStyle(
                                fontSize: 10,
                                ),
                            ),
                          icon: const Icon(
                            size: 16,
                            Icons.message
                            ),
                        ),
              ),
              Align(
                alignment:Alignment(-0.8, 1),
                child:  FloatingActionButton.extended (
                                    label: const Text(
                                      'Return',
                                      style: TextStyle(
                                        fontSize: 10,
                                        ),
                                    ),
                                    heroTag: null,
                                    onPressed: () async {
                                          Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => IdeasPage(userID: widget.userID,session_id: widget.session_id,)));
                                    },
                                ),
              ),
            ],
          )
    );
  }
  
  Future<List<dynamic>> getComments(int id) async{
  // Future getHTTP(int id) async{
    try{
      final response = await Dio().get(urlBase+"/$id");
      var hold = response.data['mData']['comments'];
      return hold;
    }on DioError catch(e){
      print(e);
      return null!;
    }
  }
}