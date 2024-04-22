import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:dio/dio.dart';
import  '../model/userStruct.dart';
import '../api/google_signin_api.dart';
import 'ideas_page.dart';
class loginPage extends StatefulWidget {
  final GoogleSignInAccount user;
  loginPage({ Key? key, required this.user,}) : super(key: key);
  @override
  _loginPageState createState() => _loginPageState();
}

class _loginPageState extends State<loginPage> {
  String usernamehold='';
  int userStatus=-3;
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

  @override
  void initState(){
    super.initState();
    postUserExists(widget.user.id, widget.user.email);
    if (userStatus==0){
      print('user exists ${userStatus}');
    }else{
      print('user does not exist $userStatus');
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
                backgroundImage: NetworkImage(widget.user.photoUrl!),
              ),
              SizedBox(height: 8),
              Text(
                'Name: ' + widget.user.displayName!,
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 8),
              Text(
                'Email: ' + widget.user.email,
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 8),
              Text(
                'Before registering fill out your Bio',
                style: TextStyle(color: Colors.black, fontSize: 16),
              ),
              SizedBox(height: 8),
              ElevatedButton(
                child: Text('Register'),
                 onPressed: () async {
                  var holdName = widget.user.displayName;
                  String stringName = holdName.toString();
                  final splitName = stringName.split(' ');
                  final firstName = splitName[0];
                  final lastName = splitName[1];
                 Response res = await postUserExists(widget.user.id, widget.user.email);
                  var ses_id = res.data['mData']['session_id'];
                  print('${widget.user.id}, ${usernamehold}, ${widget.user.email}, ${firstName}, ${lastName}, ${ses_id}' );
                  // postUser("12345678",'fap224',"fap224@lehigh.edu", 'flutter', 'app', 'cis female', 'straight', 'testing flutter app post request');
                  // var holdUser = RegisterUser(user.id,usernamehold,user.email, firstName, lastName, genIdentity, sexOri, note);
                  Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) =>  IdeasPage(userID: widget.user.id,session_id: ses_id,)));
                 }, 
                ),
                
              // ElevatedButton(
              //   child: Text('Logout'),
              //   onPressed: () async {
                    // await GoogleSignInApi.logout();
                    // Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => loginPage(),));
              //     },
              //   ),
            ],
          ),
        ),
      );
}
