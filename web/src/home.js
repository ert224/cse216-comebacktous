import React from "react";
import { useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import ReactDOM from "react-dom";
const backendUrl = "https://dry-wave-47246.herokuapp.com";

function Home() {
  document.body.style.backgroundColor = "#ece5ff";

  let location = useLocation();
  const user = JSON.parse(sessionStorage.getItem("jsArray"));
  let myArray = user.email.split("@");
  let usernname = myArray[0];
  let sID = user.userId;
  let session_id = user.sID;

  const [ideas, setIdeas] = useState([]);
  const [vote, setVoteStatus] = useState("none");

  // const user = JSON.parse(sessionStorage.getItem("toProfile"));

  let navigate = useNavigate();

  const [counter, setCounter] = useState(0);
  // useEffect(() =>{

  //const dataFetchedRef = useRef(false);

  document.body.style.backgroundColor = "#ece5ff";

  //like
  function Like(id) {
    // to do add button to view user data : https://dry-wave-47246.herokuapp.com/user/:uid is ther request

    let usrId = sID;
    let sesion = session_id;
    let votestatus = vote === "like" ? 1 : 0;

    const element = document.querySelector("#likes");
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        session_id: sesion,
        id: id,
        uid: user.userId,
      }),
    };

    fetch(
      `${backendUrl}/likes/${id}/${votestatus}/${usrId}/${1}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        refresh(true);
      });
    if (vote === "like") {
      setVoteStatus("none");
    } else {
      setVoteStatus("like");
    }
  }

  // dislike
  function dislike(id) {
    // dislike
    let usrId = sID;
    let sesion = session_id;
    let voteStatus = vote === "dislike" ? 0 : 1;

    const element = document.querySelector("#likes");
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        session_id: sesion,
        id: id,
        uid: user.userId,
      }),
    };

    fetch(
      `${backendUrl}/likes/${id}/${voteStatus}/${usrId}/${0}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        refresh(true);
      });
    if (vote === "dislike") {
      setVoteStatus("none");
    } else {
      setVoteStatus("dislike");
    }
  }

  /* let btn2 = document.createElement("button");
       btn2.innerHTML = "Delete";
       btn2.setAttribute('data-value', id);
       btn2.addEventListener("click", function () {
        
             fetch(`${backendUrl}/ideas/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => response.json()
            ).then((data) => {
                
                console.log(data);
            }).catch((error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });

            window.location.reload();
       });
       document.body.appendChild(btn2);*/
  function comment(id) {
    let toComment = {
      userid: sID,
      userName: usernname,
      commentid: id,
      session_id: session_id,
    };
    sessionStorage.setItem("currentIdea", id);
    sessionStorage.setItem("Comt", JSON.stringify(toComment));
    navigate("/ideaInfo");
    //window.location.reload();
  }
  // taking in the data from our idea GET request
  function refresh(force) {
    if (ideas.length === 0 || force) {
      document.body.style.backgroundColor = "#ece5ff";
      var useData = [];

      // Issue an AJAX GET and then pass the result to update().
      // const doAjax = async () => {await
      fetch(`${backendUrl}/ideas`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {
          // If we get an "ok" message, clear the form
          if (response.ok) {
            return Promise.resolve(response.json());
          }
          // Otherwise, handle server errors with a detailed popup message
          else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
                response.statusText
            );
          }
          return Promise.reject(response);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        })
        .then((data) => {
          console.log(data.mData);
          setIdeas(data.mData);
          /*
          console.log(data);
        useData = data.mData;
        //console.log(useData );
          
          for (let i = 0 ; i < useData.length ; i++){
          document.write("Title: "+useData[i].title);
          //document.write("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
          document.write("<p style = {text-align: justify;}>"+useData[i].idea +"</p>");
          document.write("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
          document.write(useData[i].likes);
          buttons(useData[i].id);
          document.write("  by:" + useData[i].username);

          document.write("<br>");           
          console.log(useData[i].id);
          }

          let btn4 = document.createElement("button");
          btn4.innerHTML = "Add Idea";
          // btn4.setAttribute('data-value', id);
          btn4.addEventListener("click", function () {
            //  console.log("Hi HOO")
              navigate("/idea");
          });
          document.body.appendChild(btn4);
          document.write("<br>");      

          */
        });
    }

    //  }
    //  setCounter((oldValue) => oldValue+1);
    // make the AJAX post and output value or error message to console
    // doAjax().then(console.log).catch(console.log);
  }

  // if (dataFetchedRef.current) return;
  // dataFetchedRef.current = true;
  refresh(false);

  // add IDEA
  //location.reload();

  //  },[])

  function userI(h) {
    const doAjax = async () => {
      await fetch(`${backendUrl}/user/${h}`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          let userIn = {
            userId: data.mData.uid,
            username: data.mData.username,
            Fname: data.mData.fname,
            Lname: data.mData.lname,
            email: data.mData.email,
            note: data.mData.note,
          };
          sessionStorage.setItem("prof", JSON.stringify(userIn));

          navigate("/profileOthers");
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };
    doAjax().then(console.log).catch(console.log);
  }

  return (
    <div className="container text-center">
      <h1>MAZE</h1>
      <button
        className="btn btn-primary"
        id="user"
        onClick={() => navigate("/idea")}
      >
        {" "}
        Add Idea{" "}
      </button>

      {ideas &&
        ideas.map((post) => {
          return (
            <div className="card" key={post.id}>
              <p>Title : {post.title}</p>

              <p>Idea: {post.idea}</p>

              <p id="likes">Likes: {post.likes}</p>
              <button id="like" onClick={() => Like(post.id)}>
                {" "}
                Like{" "}
              </button>
              <button id="dislike" onClick={() => dislike(post.id)}>
                {" "}
                dislike{" "}
              </button>
              <button id="comment" onClick={() => comment(post.id)}>
                {" "}
                Comment{" "}
              </button>
              <button id="user" onClick={() => userI(post.uid)}>
                {" "}
                {post.username}{" "}
              </button>
            </div>
          );
        })}
    </div>
  );
}
export default Home;
