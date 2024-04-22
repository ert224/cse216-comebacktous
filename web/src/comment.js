import React from "react";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from 'react';
import './index.css'

const backendUrl = "https://dry-wave-47246.herokuapp.com";


//let useComment = [];

function Comment(){

    const [comments, setComments] = useState([])  // lookup reactmap 
   
    let navigate = useNavigate();

    const user = JSON.parse(sessionStorage.getItem("Comt"));
    let hod = {
        userid: user.userid ,
        userName: user.userName,
        cId: user.commentid,
        session_id : user.session_id
    }
    
    console.log(hod.userid)

    let id = hod.cId;
/*
    // new comment
    function addComment(){
    
    //document.write("<br>"); 

    // create label
const label = document.createElement("label");
label.setAttribute("for", "newco");
label.innerHTML =  " new Comment  " + hod.userName ;

// insert label
document.body.appendChild(label);
//document.write(<br></br>);

// create textbox
const input = document.createElement("input");
input.setAttribute("id", "newcomment");
input.setAttribute("type", "text");

// insert textbox
document.body.appendChild(input);

let btn4 = document.createElement("button");
    btn4.innerHTML = "Share";
   // btn4.setAttribute('data-value', id);
    btn4.addEventListener("click", function () {
        fetch(`${backendUrl}/comment`, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({
                "uid": hod.userid,
                "id": hod.cId,
                "comment": document.getElementById("newcomment").value

                
            })
        }).then((response) => {
            if (response.ok) {
                return Promise.resolve(response.json());
            }
            else {
                window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
            }
            return Promise.reject(response);
        }).then((data) => {
            
            console.log(data);
        }).catch((error) => {
            console.warn('Something went wrong.', error);
            window.alert("Unspecified error");
        });
       
    });
    document.body.appendChild(btn4);
    }
    addComment(); */
      
   
    function refresh(){

         // Issue an AJAX GET and then pass the result to update().
        const doAjax = async () => {
         await fetch(`${backendUrl}/ideas/idea/${id}`, {
          method: 'GET',
             headers: {
                 'Content-type': 'application/json; charset=UTF-8'
             }
         }).then((response) => response.json())
         .then((data) => {
           console.log(data)
            setComments(data.mData.comments);
           // console.log(data.mData.comments);
           /*useComment = data.mData.comments;
           console.log(data);

           
           

          for (let i = 0 ; i < useComment.length ; i++){  
          /*  let tr = document.createElement('tr');
            let td_id = document.createElement('td');
            let td_comment = document.createElement('td');
            td_id.innerHTML = useComment[i].id;
            td_comment.innerHTML = useComment[i].comment;

            tr.appendChild(td_id);
            tr.appendChild(td_comment);
            table.appendChild(tr);
            
           // document.write( "<p style = {min-height: 50px;  padding-bottom: 10px;}>"+useComment[i].id + "</p>");
            document.write("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
            document.write( "<p style = {min-width: 50px;  padding-bottom: 10px; padding-left: 10px;}>" + useComment[i].comment + "</p>");
            document.write("   by: " + useComment[i].username);   

           // document.write(useComment[i].uid);
            
           // document.write("<body style={background-color: #ece5ff}/>");
           let btn1 = document.createElement("button");
       btn1.innerHTML = "edit";
       btn1.setAttribute('data-value', id);
       btn1.addEventListener("click", function () {
        
       navigate("/addComment");
       });
       document.body.appendChild(btn1);
       document.write("<br/>");*/
            

            document.body.style.backgroundColor = "#ece5ff";
            document.body.style.Position = "center";
           
            
            

        }).catch((error) => {
             console.warn('Something went wrong.', error);
             window.alert("Unspecified error");
         });
     }
   //  setCounter((oldValue) => oldValue+1);
//     // make the AJAX post and output value or error message to console
doAjax().then(console.log).catch(console.log);
 }

 //useEffect(() =>{
   refresh();
   //fragment.appendChild(table);
   
 //},[]); // may have to use a dependency setcomments 
 
 // to edit the comment 
 function edit (a , b){
    console.log(a);
    console.log(b);
    console.log(hod.userid);
    console.log(hod.userid);

   // navigate("/addComment");
if (hod.userid === a){
// Issue an AJAX GET and then pass the result to update().
        fetch(`${backendUrl}/editcomment`, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },
            body: {
            uid : hod.userid,
            session_id : hod.session_id,
            comment: 'changed',
            cid : b
            }  
        }).then((response) => response.json())
        .then((data) => {
            console.log(data);
        
        }).catch((error) => {
            console.warn('Something went wrong.', error);
          //  window.alert("U can't edit someone else's comment");
        });
}
else{
window.alert("You can't edit someone else's comment")
}
 
      

}

function userI(h){
    const doAjax = async () => {
        await fetch(`${backendUrl}/user/${h}`, {
         method: 'GET',
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            }
        }).then((response) => response.json())
        .then((data) => {
           console.log(data);
          let  userIn  = {
                userId : data.mData.uid,
                username :data.mData.username,
                Fname:data.mData.fname,
                Lname:data.mData.lname,
                email: data.mData.email,
                note : data.mData.note,

              };
              sessionStorage.setItem("prof", JSON.stringify(userIn));

              navigate("/profileOthers")

           
       }).catch((error) => {
            console.warn('Something went wrong.', error);
            window.alert("Unspecified error");
        });
    }
doAjax().then(console.log).catch(console.log);
}
   
// adding a comment
function addComment(){
navigate("/addComment")
}

function returnHome(){
    navigate("/home")
    }


 
   
   console.log(comments);
  
   return (
        <div>
            <table>
            <tr>
            <th> User Id</th>
            <th> Comment</th>

        </tr>
 {comments && comments.map((post) => {
    return (
       <div className = "comment">
        
       
       
        <tr>
       <td>  <button  id="user" onClick={() => userI (post.uid)}> {post.username} </button> </td>
        <td> {post.comment} </td>
        </tr>
        <button  id="edit" onClick={() => edit(post.uid , post.id)}> EDIT</button>  
       
       </div>
       
      
    );

   
}

)}
        </table>
        <button  id="add" onClick={() => addComment ()}> Add Comment </button>
        <button  id="add" onClick={() => returnHome ()}> Return Home </button>
        </div>
   );
}

export default Comment;