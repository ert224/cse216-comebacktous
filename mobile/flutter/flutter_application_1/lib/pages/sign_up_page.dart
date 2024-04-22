import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/ideas_page.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '/api/google_signin_api.dart';
import 'register_page.dart';
import 'login_page.dart';
import 'package:animated_splash_screen/animated_splash_screen.dart';

class SignUpPage extends StatefulWidget {
  @override
  _SignUpPageState createState() => _SignUpPageState();
}

class _SignUpPageState extends State<SignUpPage> {
  bool isLoading = true;
  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text('MAZE'),
        centerTitle: true,
        backgroundColor: Colors.purple,
      ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Spacer(),
              // FlutterLogo(size: 120),
              Container(
                  width: 200,
                  height: 200,
                  child: Image.network(
                    "https://jolandatetteroo.nl/wp-content/uploads/2021/12/brain-maze.png",
                    fit: BoxFit.cover,
                  ),
              ),
              Spacer(),
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Welcome Back',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              SizedBox(height: 8),
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Login to your account to continue',
                  style: TextStyle(fontSize: 16),
                ),
              ),
              Spacer(),
              SizedBox(
                width: 400, // <-- match_parent
                height: 50,
                child: ElevatedButton(
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(7.0),
                ))),
                onPressed: checkUser,
                child: Text("L O G I N".toUpperCase()),
              ), ),   
              ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  primary: Colors.white,
                  onPrimary: Colors.black,
                  minimumSize: Size(double.infinity, 50),
                ),
                icon: FaIcon(
                  FontAwesomeIcons.google,
                  color: Colors.red,
                ),
                label: Text('Sign Up with Google'),
                onPressed: () async{
                    checkUser();
                  },
                // onPressed: signIn,
              ),
             
            ],
          ),
        ),
      ); 
      
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
  Future checkUser() async {
    ScaffoldMessenger.of(context)
              .showSnackBar(SnackBar(
              content: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                            width: 200,
                            height: 200,
                            child: Image.network(
                              "https://jolandatetteroo.nl/wp-content/uploads/2021/12/brain-maze.png",
                              fit: BoxFit.cover,
                              color: Colors.white,
                            ),
                    ),
                    Text(
                        "SIGNING IN PLEASE \nwait...",
                        style: TextStyle(
                          fontSize: 30,
                          color: Colors.deepPurpleAccent), 
                          textAlign: TextAlign.center
                        ),
                    Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: CircularProgressIndicator(color: Colors.purple,)
                      ),
                  ]
                ),
              ));
    final user = await GoogleSignInApi.login();
    ScaffoldMessenger.of(context)
              .showSnackBar(SnackBar(
              content: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                            width: 200,
                            height: 200,
                            child: Image.network(
                              "https://jolandatetteroo.nl/wp-content/uploads/2021/12/brain-maze.png",
                              fit: BoxFit.cover,
                              color: Colors.white,
                            ),
                    ),
                    Text(
                        "SIGNING IN PLEASE \nwait...",
                        style: TextStyle(
                          fontSize: 30,
                          color: Colors.deepPurpleAccent), 
                          textAlign: TextAlign.center
                        ),
                    Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: CircularProgressIndicator(color: Colors.purple,)
                      ),
                  ]
                ),
              ));
     if (user != null) { 
        if (user == null) {
          ScaffoldMessenger.of(context)
              .showSnackBar(SnackBar(content: Text('Sign in Failed')));
        } else {
          Response res = await postUserExists(user.id, user.email);
          print(res.data['mData']);
          var ses_id = res.data['mData']['session_id'];
          var holdStatus = res.data['mData']['userStatus'];
                if (res.data['mData']['userStatus']==0){
                    print('user exists, Status: ${holdStatus}');
                    Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) => IdeasPage(userID: user.id, session_id: ses_id,),));
                }
                else{
                // else if (res.data['mData']['userStatus']==-1){
                  print('user does NOT exist, Status: ${holdStatus}');
                  await Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) => RegisterPage(user: user),));
                }
        }
     } else{
        print('Print error Google SignIn returned a Null');
     }
  }
  // SignIN() used for signining user to google but not longer used and was replaced with checkUser but saved for future referece 
  Future signIn() async {
    final user = await GoogleSignInApi.login();
    // GoogleSignInAuthentication userAuth = user.authentication
    print("(signuppage.singing) User: $user");
     if (user != null) {       
        if (user == null) {
          ScaffoldMessenger.of(context)
              .showSnackBar(SnackBar(content: Text('Sign in Failed')));
        } else {
          Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) => RegisterPage(user: user),));
        }
     } else{
        print('Print error SignIn returned a Null');
     }
  }
}
  
