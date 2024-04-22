
import 'package:flutter/material.dart';

class CommentModel{
  final String uid;
  final String id;
  final String comment;
  final String comment_id;
  final String url;
  final String display_text;
  final String file_name;
 

  CommentModel({
    required this.comment_id,
    required this.id,
    required this.uid,
    required this.comment,
    required this.url, //how to make not required
    required this.display_text,
    required this.file_name
  });

  factory CommentModel.fromJson(Map<String,dynamic>json){
    return CommentModel (
      comment_id: json['comment_id'],
       id: json['id'],
      uid: json['uid'], 
      comment: json['comment'],
      url: json['url'],
      display_text: json['display_text'],
      file_name: json['file_name']
     );
  }
  Map<String,dynamic> toJson()=>{
    'uid': uid,
    'id':  id,
    'comment;': comment,
    'comment_id': comment_id,
    'url': url,
    'display_text': display_text,
    'file_name': file_name
  };

  static List<CommentModel> listFromJson(List<dynamic> list)=>
      List<CommentModel>.from(list.map((x)=>CommentModel.fromJson(x)));
}
