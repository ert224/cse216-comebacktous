import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/ideas_page.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import '../model/commentStruct.dart';
import 'addPages/add_comment.dart';

final urlBase = 'https://dry-wave-47246.herokuapp.com';

class ProfilePage extends StatefulWidget {
  final String userID;
  final String session_id;
  const ProfilePage({super.key, required this.userID,required this.session_id});
  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
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
                    future: getProfile(widget.userID),

                    builder: (context,snapshot){
                      if(!snapshot.hasData){
                        return Center(
                          child: CircularProgressIndicator(),
                        );
                      } else{
                        // _textController = TextEditingController(text: snapshot.data?[0]['username'] ?? '');
                        return 
                        SingleChildScrollView(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                           children: [
                              Text(
                                //'Email:\n ${email}',
                                'Email:\n${snapshot.data?[0]['email']}',
                                style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Colors.purple,
                                ),
                              ),
                              SizedBox(height: 8),
                              Text(
                                // 'Email:\n ${username}',
                                'Username:\n${snapshot.data?[0]['username']}',
                                style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Colors.purple,
                                ),
                              ),
                              SizedBox(height: 8),
                               TextField(
                                 controller: TextEditingController(text: snapshot.data?[0]['fname'] ?? ''),
                                  decoration: InputDecoration(
                                  labelText: 'First Name: ',
                                  ),
                                onChanged: (val){
                                  fname =val;
                                },
                              ),
                              SizedBox(height: 8),
                              TextField(
                                 controller: TextEditingController(text: snapshot.data?[0]['lname'] ?? ''),
                                  decoration: InputDecoration(
                                  labelText: 'Last Name: ',
                                  ),
                                onChanged: (val){
                                  lname =val;
                                },
                              ),
                              SizedBox(height: 8),
                              TextField(
                                 controller: TextEditingController(text: snapshot.data?[0]['orientation'] ?? ''),
                                  decoration: InputDecoration(
                                  labelText: 'Sexual Orientation: ',
                                  ),
                                onChanged: (val){
                                  so =val;
                                },
                              ),
                              SizedBox(height: 8),
                              TextField(
                                 controller: TextEditingController(text: snapshot.data?[0]['gender'] ?? ''),
                                  decoration: InputDecoration(
                                  labelText: 'Gender: ',
                                  ),
                                onChanged: (val){
                                  gi =val;
                                },
                              ),
                              SizedBox(height: 8),
                              TextField(
                                 controller: TextEditingController(text: snapshot.data?[0]['note'] ?? ''),
                                  decoration: InputDecoration(
                                  labelText: 'Note: ',
                                  ),
                                onChanged: (val){
                                  note =val;
                                },
                              ),
                        SizedBox(height: 15),
                        Center(
                          child: FloatingActionButton.extended(
                              onPressed: () async {
                                if (fname==''){
                                  fname=snapshot.data?[0]['fname'];
                                }
                                if (lname==''){
                                  lname=snapshot.data?[0]['lname'];
                                }
                                if( gi==''){
                                  gi=snapshot.data?[0]['gender'];
                                }
                                if ( so==''){
                                  so=snapshot.data?[0]['orientation'];
                                }
                                if (note==''){
                                  note=snapshot.data?[0]['note'];
                                }
                                await updateUser(widget.userID, snapshot.data?[0]['username'], snapshot.data?[0]['email'], fname, lname, gi, so, note, widget.session_id);
                                await Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => IdeasPage(userID: widget.userID, session_id: widget.session_id,)));
                              },
                              label: const Text('Submit Changes'),
                              icon: const Icon(Icons.message),
                            ),
                        ),
                    ]
                    ),
                  );
                      }
                    },
                  ),
                
                ),
            ],
          )
        ),
      ),
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

  Future updateUser(String uid,String username,String email, String fname, String lname, String gi, String so, String note, String session_id) 
  async{
    final api = urlBase+"/profile";
    final data={
      'uid':uid,
      'username':username,
      'email':email,
      'fname':fname,
      'lname':lname,
      'gi': gi,
      'so':so,
      'note':note, 
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