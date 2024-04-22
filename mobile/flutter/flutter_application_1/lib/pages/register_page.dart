import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '/api/google_signin_api.dart';
import 'ideas_page.dart';
import 'sign_up_page.dart';
import 'package:dio/dio.dart';
import  '../model/userStruct.dart';
import '../model/userStruct.dart';

class RegisterPage extends StatelessWidget {
  final GoogleSignInAccount user;

  RegisterPage({ Key? key, required this.user,}) : super(key: key);
  String genIdentity ='';
  String sexOri='';
  String note='';
  String usernamehold='';
////
 Future<RegisterUser> postUser(String uid, String username, String email, String fname, String lname, String gi, String so, String note)// String session_id) 
  async{
    final api = 'https://dry-wave-47246.herokuapp.com/register';
    final data={
      'uid':uid,
      'username':username,
      'email':email,
      'fname':fname,
      'lname':lname,
      'gi': gi,
      'so':so,
      'note':note, 
      //'session_id': session_id
    };
    final dio =Dio();
    Response response;
    response = await dio.post(api,data:data);   
    if (response.statusCode ==200){
      final body = response.data;
      return RegisterUser(uid: uid, username: username, email: email, fname: fname, lname: lname, gi: gi, so: so, note: note);// session_id: session_id);
    } else{
      return null!;
    }
  }
////
Future postUserExists(String uid, String email) async{
          final api = 'https://dry-wave-47246.herokuapp.com/userexists';
          final data={
            'uid':uid,
            'email':email,
          }; 
          final dio = Dio();
          Response response = await dio.post(api,data:data);   
          print(response);
          if (response.statusCode ==200){
            return response;
          } else{
            return null!;
          }
}

  // final RegisterUser regUser =null!; 
  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: Text('MAZE'),
          centerTitle: true,
        ),
        body: Container(
          alignment: Alignment.center,
          color: Color.fromARGB(255, 247, 236, 249),
          child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'PROFILE',
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              // SizedBox(height: 32),
              CircleAvatar(
                radius: 40,
                backgroundImage: NetworkImage(user.photoUrl!),
              ),
              SizedBox(height: 8),
              Text(
                'Name: ' + user.displayName!,
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 8),
              Text(
                'Email: ' + user.email,
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 8),
              Text(
                'Before registering fill out your Bio',
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 8),
              TextField(
                decoration: InputDecoration(labelText: 'Username' ),
                onChanged: (val){
                  usernamehold =val;
                },
              ),
              TextField(
                decoration: InputDecoration(labelText: 'Sexual Orientation' ),
                onChanged: (val){
                  sexOri =val;
                },
              ),
              TextField(
                decoration: InputDecoration(labelText: 'Gender Identity' ),
                onChanged: (val)=> genIdentity =val,
              ),
              TextField(
                decoration: InputDecoration(labelText: 'Note' ),
                onChanged: (val){
                  note =val;
                },
              ),
              ElevatedButton(
                child: Text('Register'),
                 onPressed: () async {
                  var holdName = user.displayName;
                  String stringName = holdName.toString();
                  final splitName = stringName.split(' ');
                  final firstName = splitName[0];
                  final lastName = splitName[1];
                  await postUserExists(user.id, user.email);
                  print('after resUser');
                  print('${user.id}, ${usernamehold}, ${user.email}, ${firstName}, ${lastName}, ${genIdentity}, ${sexOri}, ${note}' );
                  await postUser(user.id,usernamehold,user.email, firstName, lastName, genIdentity, sexOri, note);
                  Response res = await postUserExists(user.id, user.email);
                  var ses_id = res.data['mData']['session_id'];
                  Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) =>  IdeasPage(userID: user.id,session_id: ses_id,)));
                 }, 
                ),
              ElevatedButton(
                child: Text('Logout'),
                onPressed: () async {
                    await GoogleSignInApi.logout();
                    Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => SignUpPage(),));
                  },
                ),
            ],
          ),
        ),
        ),
      );
}
