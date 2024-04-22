import 'package:flutter/material.dart';

//                   print('${user.id}, ${usernamehold}, ${user.email}, ${firstName}, ${lastName}, ${genIdentity}, ${sexOri}, ${note}, ${holdSessionID}' );

class RegisterUser{
  final String uid;
  final String email;
  final String username;
  final String fname;
  final String lname;
  final String gi;
  final String so;
  final String note;
  // final String session_id;

  RegisterUser({
    required this.uid,
    required this.email,
    required this.username,
    required this.fname,
    required this.lname,
    required this.gi,
    required this.so,
    required this.note,
    // required this.session_id
  });

  factory RegisterUser.fromJson(Map<String,dynamic>json){
    return RegisterUser (
      uid: json['uid'], 
      email: json['email'],
      username: json['username'],
      fname: json['fname'],
      lname: json['lname'],
      gi: json['gi'], 
      so: json['so'], 
      note: json['note'],
      // session_id: json['session_id']
     );
  }
  Map<String,dynamic> toJson()=>{
    'uid': uid,
    'email': email,
    'username':  username,
    'fname': fname,
    'lname': lname,
    'gi': gi,
    'so': so,
    'note': note,
    // 'session_id': session_id
  };

  static List<RegisterUser> listFromJson(List<dynamic> list)=>
      List<RegisterUser>.from(list.map((x)=>RegisterUser.fromJson(x)));
}
