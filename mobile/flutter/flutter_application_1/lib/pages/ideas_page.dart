import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_application_1/pages/profile_page.dart';
import 'package:flutter_application_1/pages/viewProfile_page.dart';
import 'package:url_launcher/link.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import '../api/google_signin_api.dart';
import '../model/data_model.dart';
import '/pages/sign_up_page.dart';
import '../model/userStruct.dart';
import 'addPages/add_idea.dart';
import 'comment_page.dart';
import 'viewProfile_page.dart';
import 'package:image_picker/image_picker.dart';

import 'package:dio/dio.dart';

final urlBase = 'https://dry-wave-47246.herokuapp.com';
class IdeasPage extends StatefulWidget {
  final String userID;
  final String session_id;
  const IdeasPage({super.key, required this.userID,required this.session_id});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  State<IdeasPage> createState() => _IdeasPageState();
}

class _IdeasPageState extends State<IdeasPage> {
  /* 
  * Scaffold to represent our default page. Centers our widget from httpreqwords and has a floating app button to add posts
  */
  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the IdeasPage object that was created by
        // the App.build method, and use it to set our appbar title.
        centerTitle: true,
        title: Text(
          'MAZE'
          ),
      ), // for comments
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the iddle of the parent
        child: HttpReqWords(userID: widget.userID,session_id: widget.session_id, )
          //builder(context, followLink) => ElevatedButton(child: Text(display_text), onPressed: () {},) //Elevated Button
      ),
      floatingActionButton: Stack(
            children: <Widget>[
              Align(
                alignment: Alignment(1, -0.85),
                child: SizedBox( 
                        height:40,
                        width:60,
                        child: FloatingActionButton.extended (
                                    shape: BeveledRectangleBorder(borderRadius: BorderRadius.zero),
                                    label: const Text(
                                      'Logout',
                                      style: TextStyle(color: Colors.purple),
                                    ),
                                    heroTag: null,
                                    onPressed: () async {
                                          await GoogleSignInApi.logout();
                                          Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => SignUpPage(),));
                                    },
                                    backgroundColor: Color.fromARGB(255, 255, 255, 255),
                                ),
                ),
              ),
              Align(
                alignment:Alignment(-0.8, -0.85),
                child: SizedBox( 
                        height:40,
                        width:60,
                        child: FloatingActionButton.extended (
                                    shape: BeveledRectangleBorder(borderRadius: BorderRadius.zero),
                                    label: const Text(
                                      'Profile',
                                      style: TextStyle(color: Colors.purple),
                                    ),
                                    heroTag: null,
                                    onPressed: () async {
                                          Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(userID: widget.userID, session_id: widget.session_id),));
                                    },
                                    backgroundColor: Color.fromARGB(255, 255, 255, 255),
                                ),
                ),
              ),
              Align(
                alignment: Alignment.bottomRight,
                child: FloatingActionButton.extended(
                  backgroundColor: Colors.purple,
                  onPressed: () async {
                    // Navigator.pushNamed(context, '/Text'); //edit her for page
                    await Navigator.of(context).pushReplacement(MaterialPageRoute(
                      builder: (context) => AddIdeas(userID: widget.userID, session_id: widget.session_id,),
                    ));
                  },
                  label: const Text('Add Post'),
                  icon: const Icon(Icons.add_box),
                ),
              ),
            ],
          )
    );
  }
}

class HttpReqWords extends StatefulWidget {
  final String userID;
  final String session_id;
  const HttpReqWords({super.key, required this.userID,required this.session_id});
  //The class for our main page, a stateful page that pulls information from a webpage
  @override
  State<HttpReqWords> createState() => _HttpReqWordsState();
}

/* 
* A Class extending our state to form our homepage
* Creates a series of Listview widgets in an infinitely scrolling list
* Listview widgets contain the message title, content, and number of likes pulled from the web
*/
class _HttpReqWordsState extends State<HttpReqWords> {
  late Future<List<MessageData>> _future_list_MessageData;

  /* 
  * Initializes our homepage by pulling the data from the web
  *Sets the initial state of our future object to the results from the web
  */
  @override
  void initState() {
    super.initState();
    _future_list_MessageData = fetchMessageData();
  }

  /* 
  * A method for retrying the fetching of data
  * fetches data from the web, sets future object to the value of that request
  */
  void _retry() {
    setState(() {
      _future_list_MessageData = fetchMessageData();
    });
  }

  /* 
  * A method for increasing the number of likes 
  * Accepts a MessageData object and the an integer representing whether to like or dislike. 1 for like, 0 for dislike
  * resets the state to indicate the new list
  * returns void
  */
  void _upvote(MessageData m, int i) {
    // print(m);
    updateLikes(m, i);
    setState(() {
      _future_list_MessageData = fetchMessageData();
    });
  }

  void resetState() {
    setState(() {
      _future_list_MessageData = fetchMessageData();
    });
  }
  /* 
  * A method for deleting a message
  * Accepts a messageData object indicating which message to teach.
  * Resets state to represent new list of message
  */
  void _delete(MessageData m) {
    deleteMessage(m);
    setState(() {
      _future_list_MessageData = fetchMessageData();
    });
  }

  /*
  *Method for building a series of listviews with infinite scrolling 
  * Creates an Asyncronous snapshot of data that contains all of our messages from the backend 
  * Creates a series of cards in a list view, each card contains a listTilte with the following elements: 
  * leading elevated button to represent likes. Trailing Elevated button to represent deleting a post 
  */
  int currentIndex =0;
  @override
  Widget build(BuildContext context) {
    int holdStatus=2;
    var fb = FutureBuilder<List<MessageData>>(
      future: _future_list_MessageData,
      builder: (BuildContext context, AsyncSnapshot<List<MessageData>> snapshot) {
        Widget child;
        if (snapshot.hasData) {
          // developer.log('`using` ${snapshot.data}', name: 'my.app.category');
          // create  listview to show one row per array element of json response
          child = ListView.builder(
              //shrinkWrap: true, //expensive! consider refactoring. https://api.flutter.dev/flutter/widgets/ScrollView/shrinkWrap.html
              padding: const EdgeInsets.all(8.0),
              itemCount: snapshot.data!.length,
              itemBuilder: /*1*/ (context, i) {
                return Card(
                    child: Material(
                        child: Column(
                        children: [
                          ListTile(
                            leading: Column(
                              children: [
                                      Icon(
                                        Icons.account_box,
                                        size: 40
                                      ),
                                      GestureDetector(
                                      child: Text(
                                          "${snapshot.data![i].username}",
                                            style: TextStyle(
                                                fontSize: 14,
                                                color: Colors.purple), 
                                                textAlign: TextAlign.center
                                            ),
                                          onTap: (){
                                            Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ViewProfile(userID: widget.userID,session_id: widget.session_id,tapUID: snapshot.data![i].uid,)));
                                          },
                                      ),
                                    ],
                            ),
                            title: Text(
                              "${snapshot.data![i].title}",
                              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                            ),
                            subtitle: Text("${snapshot.data![i].ideas}"),             
                        ),
                        //show link
                        Link(
                        target: LinkTarget.blank, //.blank opens link in external browser
                        uri: Uri.parse(snapshot.data![i].url),
                        builder: (context, followLink) => GestureDetector(onTap: followLink, child: Text(snapshot.data![i].display_text), //TextStyle //maybe add in style after testing functionality (backend currently not running right)
                        ),
                        ),
                        //show file
                        Image.file(
                          File(snapshot.data![i].file_name),
                          width: 100,
                          height: 100,
                          //fit: BoxFit.fill, //? not sure if it will make the file too large
                        ),
                        IconButton(
                          icon: const Icon(Icons.upload_file,),
                          onPressed:() async {
                            XFile? file = await ImagePicker().pickImage(source: ImageSource.gallery,);
                            //check if the file is null
                            if(file!=null){
                              Image.file(
                                File(snapshot.data![i].file_name),
                              );
                            }
                          },
                        ),
                       ButtonBar(
                        alignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          IconButton(
                            onPressed: () async {
                                var ideaID = snapshot.data![i].id;
                                var holdUsername =snapshot.data![i].username;
                                print(ideaID);
                                Navigator.of(context).pushReplacement(MaterialPageRoute(
                                  builder: (context) =>
                                      CommentPage(ideaID: ideaID, userID: widget.userID, username: holdUsername,session_id: widget.session_id,),
                                ));
                            },
                            icon: const Icon(
                              Icons.comment,
                              color: Colors.blue,
                              ),
                            ),
                            IconButton(
                            onPressed: () async {
                                 print('in up');
                                if (snapshot.data![i].voteStatus==0){
                                  holdStatus=1;
                                }else if (snapshot.data![i].voteStatus==1){
                                  holdStatus=0;
                                }
                                print('${snapshot.data![i].id}, ${snapshot.data![i].title}, ${snapshot.data![i].ideas}, ${snapshot.data![i].likes},   ${snapshot.data![i].uid}, ${snapshot.data![i].url}, ${snapshot.data![i].display_text}, ${snapshot.data![i].file_name}, ${snapshot.data![i].voteStatus}, ${snapshot.data![i].username}, ${widget.session_id}, 0' );
                                await putLike(
                                  snapshot.data![i].id, 
                                  snapshot.data![i].title,
                                  snapshot.data![i].ideas,
                                  snapshot.data![i].likes, 
                                  snapshot.data![i].uid,
                                  snapshot.data![i].url,
                                  snapshot.data![i].display_text,
                                  snapshot.data![i].file_name, 
                                  holdStatus, 
                                  snapshot.data![i].username,   
                                  widget.session_id, 
                                  1 );
                                  resetState();
                               },
                            icon: const Icon(
                              Icons.arrow_circle_up,
                              color: Colors.green,
                              ),
                            ),
                            TextButton(
                            child: Text(
                              'Votes\n${snapshot.data![i].likes}',
                              textAlign: TextAlign.center,
                              ),
                            onPressed: (){},
                            ),
                            IconButton(
                             onPressed: () async {
                                // _upvote(snapshot.data![i], 0);
                                print('in down');
                                print('${snapshot.data![i].id}, ${snapshot.data![i].title}, ${snapshot.data![i].ideas}, ${snapshot.data![i].likes},   ${snapshot.data![i].uid}, ${snapshot.data![i].url}, ${snapshot.data![i].display_text}, ${snapshot.data![i].file_name}, ${snapshot.data![i].voteStatus}, ${snapshot.data![i].username}, ${widget.session_id}, 0' );
                               // snapshot.data![i].voteStatus, 
                                if (snapshot.data![i].voteStatus==0){
                                    holdStatus=1;
                                  }else if (snapshot.data![i].voteStatus==1){
                                    holdStatus=0;
                                }
                               //first 0 for click dislike 
                                await putLike(
                                  snapshot.data![i].id, 
                                  snapshot.data![i].title,
                                  snapshot.data![i].ideas,
                                  snapshot.data![i].likes, 
                                  snapshot.data![i].uid, 
                                  snapshot.data![i].url,
                                  snapshot.data![i].display_text,
                                  snapshot.data![i].file_name,
                                  holdStatus, 
                                  snapshot.data![i].username,   
                                  widget.session_id, 
                                  0);
                                  resetState();
                            },
                            icon: const Icon(
                              Icons.arrow_circle_down,
                              color: Colors.purple,
                              ),
                            )

                        ],
                        
                       )    
                   ]   
                 ),           
                ));
              });
        } else if (snapshot.hasError) {
          // newly added
          child = Text('${snapshot.error}');
        } else {
          // awaiting snapshot data, return simple text widget
          child = const CircularProgressIndicator(); //show a loading spinner.
        }
        return child;
      },
    );

    return fb;
  }


Future putLike(int id,String title,String idea, int likes, String uid, String url, String display_text, String? file_name, int voteStatus, String username, String session_id, int upDown) 
  async{
    final api = urlBase+"/likes/$id/$voteStatus/$uid/$upDown";
    final data={
      'id': id,
      'title':title,
      'idea': idea,
      'likes': likes,
      'uid':uid,
      'url':url,
      'display_text':display_text,
      'file_name':file_name,
      'voteStatus': voteStatus,
      'session_id': session_id
    };
    final dio =Dio();
    print(data);
    Response response;
    response =await dio.put(api,data:data); 
    print('update like response');
    print(response); 
    if (response.statusCode ==200){
      final body = response.data;
    } else{
      return null;
    }
  }
}
