import 'dart:convert';
import 'dart:developer' as developer;
import 'package:http/http.dart' as http;

/* 
* A method for Updating Likes 
* @param MessageDate m - a messageData object containing the database id of the message to be liked or disliked 
* @param mNum, an integer representing the whether the message should be liked or disliked. 1 for like, 0 for dislike
* Returns void or error if like fails
*/
void updateLikes(MessageData m, int up_down) async {
  //       "/likes/:id/:inc_dec/:uid/:up_down",
  // "/likes/:id/
  // :inc_dec/  // increase decrease
  // :uid/
  // :up_down" //type of button that hit ?
  print(m.id);
  print(m.voteStatus);
  int ideaID = m.id;
  int vStatus = m.voteStatus;
  String uid = m.uid;
  String url = m.url;
  String display_text = m.display_text;
  String file_name = m.file_name;
  // if (prev_inc_dec==0){
  //   up_down=1;
  // }else{
  //   prev_inc_dec=0;
  // }
  final response = await http.put(Uri.parse(
      'https://dry-wave-47246.herokuapp.com/likes/${ideaID}/${vStatus}/${uid}/${url}/${display_text}/${file_name}/${up_down}'));
  if (response.statusCode == 200) {
    // If the server did return a 200 OK response,
    // then parse the JSON.
    return;
  } else {
    // If the server did not return a 200 OK response,
    // then throw an exception.
    print(response.statusCode);
    throw Exception('Failed to update likes.');
  }
}

/* 
* A method for deleting messages from the database 
* @param MessageData m, the messagedata object to be deleted, contains the id necessary to delete the object 
* returns void or exception upon failure
*/
void deleteMessage(MessageData m) async {
  int deleteID = m.id;
  final response = await http.delete(
      Uri.parse('https://dry-wave-47246.herokuapp.com/ideas/${deleteID}'));

  if (response.statusCode == 200) {
    // If the server did return a 200 OK response,
    // then parse the JSON.
    return;
  } else {
    // If the server did not return a 200 OK response,
    // then throw an exception.
    print(response.statusCode);
    throw Exception('Failed to delete.');
  }
}

/* 
* A method for posting a user message to the data base 
* Takes the message title and content and encodes them in a json format and send it over the web to the post route 
* @param title, a String representing a message title 
* @param idea, a String representing a message idea
* @param url, a String representing a link
* @param, display_text, a String representing the hyperlink
* @param, file_name, a String representing the file
*/
void postUserMessage(String title, String idea, String url, String display_text, String file_name) async {
  final response = await http.post(
      Uri.parse('https://dry-wave-47246.herokuapp.com/postidea'),
      body: jsonEncode(<String, String>{
        'title': title,
        'ideas': idea,
        'url': url,
        'display_text': display_text,
        'file_name': file_name,
      }));

  if (response.statusCode == 200) {
    // If the server did return a 200 OK response,
    // then parse the JSON.
    return;
  } else {
    // If the server did not return a 200 OK response,
    // then throw an exception.
    print(response.statusCode);
    throw Exception('Failed to update likes.');
  }
}

/* 
* A class represeting a message and all of the associated fields 
* id -  an integer representing the database id of a message 
* title- a String representing the title of the message 
* ideas-  a String representing the content of a message 
* likes - an integer representing the quantity of likes
*/
class MessageData {
  final int id;

  final String title;

  final String ideas;

  final int likes;

  final String uid;

  final String url;

  final String display_text;

  final String file_name;

  final String username;

  final int voteStatus;

  const MessageData({
    required this.id,
    required this.title,
    required this.ideas,
    required this.likes,
    required this.uid,
    required this.url, //how to make optional
    required this.display_text,
    required this.file_name,
    required this.username,
    required this.voteStatus
  });

  /* 
  * A factory build method for creating MessageData objects from json formatted Maps 
  */
  factory MessageData.fromJson(Map<String, dynamic> json) {
    return MessageData(
      id: json['id'],
      title: json['title'],
      ideas: json['idea'],
      likes: json['likes'],
      uid: json['uid'],
      url: json['url'],
      display_text: json['display_text'],
      file_name: json['file_name'],
      voteStatus: json['voteStatus'],
      username: json['username']
    );
  }

}

/*
* A method for fetching data from the web 
* async property means that it waits for a response from the webpage before returning 
* On success returns a future list of lists of type messageData 
* on failure throws error
*/
Future<List<MessageData>> fetchMessageData() async {
  final response =
      await http.get(Uri.parse('https://dry-wave-47246.herokuapp.com/ideas'));

  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    final List<MessageData> returnData = [];
    var res = jsonDecode(response.body);
    res = res['mData'];
    if (res is List) {
      for (int i = 0; i < res.length; i++) {
        returnData.add(MessageData.fromJson(res[i]));
      }
    } else {
      developer
          .log('ERROR: Unexpected json response type (was not a List or Map).');
    }
    return returnData;
  } else {
    // If the server did not return a 200 OK response,
    // then throw an exception.
    throw Exception('Did not receive success status code from request.');
  }
}

