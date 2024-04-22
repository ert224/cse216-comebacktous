import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/ideas_page.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import '../model/commentStruct.dart';
import 'addPages/add_comment.dart';

final urlBase = 'https://dry-wave-47246.herokuapp.com';

class ViewProfile extends StatefulWidget {
  final String userID;
  final String session_id;
  final String tapUID;
  const ViewProfile({super.key, required this.userID, required this.session_id, required this.tapUID});
  @override
  State<ViewProfile> createState() => _ViewProfileState();
}

class _ViewProfileState extends State<ViewProfile> {
  // var email;
  // var username;
  String fname  = '';
  String lname  = '';
  String gi  = '';
  String so  = '';
  String note  = '';
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
              Center(
                    child: Text(
                      'Profile',
                      style: TextStyle(
                        fontSize: 23,
                        fontWeight: FontWeight.bold,
                        color: Colors.purple,
                        ),
                      ),
              ),
                SizedBox(height: 20),
                Expanded(
                  child: FutureBuilder(
                    future: getProfile(widget.tapUID),
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
                                //'Email:\n ${email}',
                                'Email:',
                                style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Colors.purple,
                                ),
                              ),
                              SizedBox(height: 8),
                              Text(
                                '${snapshot.data?[0]['email']}',
                                style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Color.fromARGB(255, 129, 129, 129),
                                ),
                              ),
                              SizedBox(height: 8),
                              Text(
                                'Username:',
                                style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Colors.purple,
                                ),
                              ),
                              SizedBox(height: 8),
                              Text(
                                '${snapshot.data?[0]['username']}',
                                style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Color.fromARGB(255, 129, 129, 129),
                                ),
                              ),
                              SizedBox(height: 8),
                              Text(
                                'Note:',
                                style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Colors.purple,
                                ),
                              ),
                              SizedBox(height: 8),
                              Text(
                                '${snapshot.data?[0]['note']}',
                                style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Color.fromARGB(255, 129, 129, 129),
                                ),
                              ),
                              SizedBox(height: 8),
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
      floatingActionButton: FloatingActionButton.extended(
          onPressed: () async {
            await Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => IdeasPage(userID: widget.userID,session_id: widget.session_id,)));
          },
          label: const Text('Return'),
          icon: const Icon(Icons.message),
        ),
     floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
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
}