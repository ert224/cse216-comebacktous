import React from "react";
import { useNavigate, useRouteLoaderData } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import ReactDOM from "react-dom";
import download from "downloadjs";
const backendUrl = "https://dry-wave-47246.herokuapp.com";

function IdeaInfo() {
  document.body.style.backgroundColor = "#ece5ff";

  let location = useLocation();
  const user = JSON.parse(sessionStorage.getItem("jsArray"));
  let myArray = user.email.split("@");
  let usernname = myArray[0];
  let session_id = user.session_id;

  const [idea, setIdea] = useState();
  const [vote, setVoteStatus] = useState("none");
  const [addFile, setAddFile] = useState(false);
  const [addLink, setAddLink] = useState(false);
  const [uploadComment, setUploadComment] = useState(-1);

  // const user = JSON.parse(sessionStorage.getItem("toProfile"));

  let navigate = useNavigate();

  const [counter, setCounter] = useState(0);
  // useEffect(() =>{

  //const dataFetchedRef = useRef(false);

  document.body.style.backgroundColor = "#ece5ff";

  const uploadLink = () => {
    let url = document.getElementById("url").value;
    let display_text = document.getElementById("displayText").value;
    console.log(url);
    console.log(display_text);
    if (uploadComment === -1) {
      fetch(`${backendUrl}/uploadLink`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          session_id: session_id,
          uid: user.userId,
          url: url,
          id: idea.id,
          cid: -1,
          display_text: display_text,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          refresh(true);
        });
    } else {
      fetch(`${backendUrl}/uploadLink`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          session_id: session_id,
          uid: user.userId,
          url: url,
          id: -1,
          cid: uploadComment,
          display_text: display_text,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          refresh(true);
        });
    }
    refresh(true);
  };

  const uploadFile = () => {
    let file_name = document.getElementById("fileName").value;
    let file = document.getElementById("fileContent").files[0];
    console.log(file_name);
    console.log(file);
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
      console.log(reader.result);
      console.log(uploadComment);
      if (uploadComment == -1) {
        fetch(`${backendUrl}/uploadFile`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            session_id: session_id,
            uid: user.userId,
            id: idea.id,
            cid: -1,
            file_name: file_name,
            file_contents: reader.result,
          }),
        })
          .then((response) => response.json())
          .then((data) => {
            console.log(data);
            refresh(true);
          });
      } else {
        fetch(`${backendUrl}/uploadFile`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            session_id: session_id,
            uid: user.userId,
            cid: uploadComment,
            id: -1,
            file_name: file_name,
            file_contents: reader.result,
          }),
        })
          .then((response) => response.json())
          .then((data) => {
            console.log(data);
            refresh(true);
          });
      }
    };
    reader.onerror = function (error) {
      console.log("Error: ", error);
    };
    refresh(true);
  };

  //like
  function Like(id) {
    // to do add button to view user data : https://dry-wave-47246.herokuapp.com/user/:uid is ther request

    let usrId = user.userId;
    let sesion = session_id;
    let votestatus = vote === "like" ? 1 : 0;

    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        session_id: sesion,
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
    refresh(true);
  }

  // dislike
  function dislike(id) {
    // dislike
    let usrId = user.userId;
    let sesion = session_id;
    let voteStatus = vote === "dislike" ? 1 : 0;

    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        session_id: sesion,
        id: id,
      }),
    };

    fetch(
      `${backendUrl}/likes/${id}/${voteStatus}}/${usrId}/${0}`,
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
    refresh(true);
  }
  function comment() {
    fetch(`${backendUrl}/comment`, {
      method: "POST",
      headers: {
        "Content-type": "application/json; charset=UTF-8",
      },
      body: JSON.stringify({
        uid: user.userId,
        id: idea.id,
        comment: document.getElementById("com").value,
        session_id: user.session_id,
      }),
    })
      .then((response) => {
        return Promise.resolve(response.json());
      })
      .then((data) => {
        console.log("data");
        console.log(data);
        refresh(true);
      })
      .catch((error) => {
        window.alert("Unspecified error");
      });
    document.getElementById("com").value = "";
    refresh(true);
  }

  // taking in the data from our idea GET request
  function refresh(force = false) {
    if (!idea || force) {
      document.body.style.backgroundColor = "#ece5ff";
      var useData = [];
      let id = sessionStorage.getItem("currentIdea");

      // Issue an AJAX GET and then pass the result to update().
      // const doAjax = async () => {await
      fetch(`${backendUrl}/ideas/idea/${id}`, {
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
          console.log(data);
          if (data.mData.file) {
            fetch(`${backendUrl}/downloadFile`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                file_name: data.mData.file.file_name,
              }),
            })
              .then((response) => response.json())
              .then((fileData) => {
                console.log("FILE");
                console.log(fileData.mData);
                // download(fileData.mData);
                data.mData.contents = fileData.mData;
                setIdea(data.mData);
              });
          } else {
            setIdea(data.mData);
          }
          for (let i = 0; i < data.mData.comments.length; i++) {
            if (data.mData.comments[i].file) {
              fetch(`${backendUrl}/downloadFile`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                  file_name: data.mData.comments[i].file.file_name,
                }),
              })
                .then((response) => response.json())
                .then((commentData) => {
                  console.log("FILE");
                  console.log(commentData.mData);
                  data.mData.comments[i].contents = commentData.mData;
                  console.log(data.mData);
                  setIdea(data.mData);
                });
            } else {
              setIdea(data.mData);
            }
          }
        });
    }
  }

  refresh(false);

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
      <h1>
        <a href="/home">MAZE</a>
      </h1>
      {idea && (
        <div className="card">
          <h5>{idea.title}</h5>
          <p>{idea.idea}</p>
          <p>Likes: {idea.likes}</p>
          {idea.link && idea.link.display_text && (
            <a href={"https://" + idea.link.url}>{idea.link.display_text}</a>
          )}
          {idea.contents &&
            (idea.contents.includes("jpg") ||
              idea.contents.includes("png")) && (
              <img
                className="img-fluid"
                alt={idea.file.file_name}
                src={idea.contents}
              />
            )}
          {idea.contents && idea.contents.includes("video/mp4") && (
            <video className="mx-auto" width="320" height="240" controls>
              <source src={idea.contents} type="video/mp4" />
            </video>
          )}
          {idea.contents && idea.contents.includes("audio/mpeg") && (
            <audio className="mx-auto" controls>
              <source src={idea.contents} type="audio/mpeg" />
            </audio>
          )}
          {idea.contents && idea.contents.includes("application/pdf") && (
            <iframe
              className="mx-auto"
              title={idea.file.file_name}
              src={idea.contents}
              style={{ width: "600px", height: "500px" }}
              frameBorder="0"
            ></iframe>
          )}
          <button
            className="btn btn-primary m-1"
            id="like"
            onClick={() => Like(idea.id)}
          >
            {" "}
            Like{" "}
          </button>
          <button
            className="btn btn-primary m-1"
            id="dislike"
            onClick={() => dislike(idea.id)}
          >
            {" "}
            Dislike{" "}
          </button>
          <button
            className="btn btn-primary m-1"
            id="user"
            onClick={() => userI(idea.uid)}
          >
            {" "}
            {idea.username}{" "}
          </button>
          {addFile ? (
            <div>
              <label className="m-1">File Name</label>
              <input id="fileName" type="text" />
              <label className="m-1">File</label>
              <input id="fileContent" type="file" />
              <button
                className="btn btn-secondary"
                onClick={() => {
                  uploadFile();
                }}
              >
                Upload File
              </button>
            </div>
          ) : (
            <></>
          )}
          {addLink ? (
            <div>
              <label className="m-1">Display Text</label>
              <input id="displayText" type="text" />
              <label className="m-1">Link</label>
              <input id="url" type="text" />
              <button
                className="btn btn-secondary"
                onClick={() => {
                  uploadLink();
                }}
              >
                Upload Link
              </button>
            </div>
          ) : (
            <></>
          )}
          {user.userId === idea.uid ? (
            <div>
              <button
                className="m-1 btn btn-primary"
                onClick={() => {
                  setAddFile(!addFile);
                  setUploadComment(-1);
                  setAddLink(false);
                }}
              >
                Add File
              </button>
              <button
                className="m-1 btn btn-primary"
                onClick={() => {
                  setAddLink(!addLink);
                  setUploadComment(-1);
                  setAddFile(false);
                }}
              >
                Add Link
              </button>
            </div>
          ) : (
            <></>
          )}
          <div>
            <label className="m-1">Comment</label>
            <input id="com" type="text" placeholder="Say something useful..." />
            <button className="btn btn-primary m-1" onClick={comment}>
              Post
            </button>
          </div>
          <div>
            {idea.comments.map((comment) => {
              return (
                <div className="border" key={comment.id}>
                  <label className="m-1">{comment.username}: </label>
                  <p>{comment.comment}</p>
                  {comment.link && (
                    <a href={"https://" + comment.link.url}>
                      {comment.link.display_text}
                    </a>
                  )}
                  {comment.contents &&
                    comment.contents.includes("video/mp4") && (
                      <video
                        className="mx-auto"
                        width="320"
                        height="240"
                        controls
                      >
                        <source src={comment.contents} type="video/mp4" />
                      </video>
                    )}
                  {comment.contents &&
                    comment.contents.includes("audio/mpeg") && (
                      <audio className="mx-auto" controls>
                        <source src={comment.contents} type="audio/mpeg" />
                      </audio>
                    )}
                  {comment.contents &&
                    comment.contents.includes("application/pdf") && (
                      <iframe
                        className="mx-auto"
                        title={comment.file.file_name}
                        src={comment.contents}
                        style={{ width: "600px", height: "500px" }}
                        frameBorder="0"
                      ></iframe>
                    )}
                  {comment.uid === user.userId ? (
                    <div>
                      <button
                        className="m-1 btn btn-primary btn-sm"
                        onClick={() => {
                          setAddFile(!addFile);
                          setUploadComment(comment.id);
                          setAddLink(false);
                        }}
                      >
                        Add File
                      </button>
                      <button
                        className="m-1 btn btn-primary btn-sm"
                        onClick={() => {
                          setAddLink(!addLink);
                          setUploadComment(comment.id);
                          setAddFile(false);
                        }}
                      >
                        Add Link
                      </button>
                    </div>
                  ) : (
                    <></>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}
export default IdeaInfo;
