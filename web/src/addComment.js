import React from "react";
import { useNavigate } from "react-router-dom";

import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";

const backendUrl = "https://dry-wave-47246.herokuapp.com";
function AddComment() {
  let navigate1 = useNavigate();

  function addComment() {
    const user = JSON.parse(sessionStorage.getItem("Comt"));
    let hod = {
      userid: user.userid,
      userName: user.userName,
      cid: user.commentid,
      session_id: user.session_id,
    };

    let id = hod.cid;

    //document.write("<br>");

    // create label
    const label = document.createElement("label");
    label.setAttribute("for", "newco");
    label.innerHTML = "Comment  ";

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
        method: "POST",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify({
          uid: hod.userid,
          id: hod.cId,
          comment: document.getElementById("newcomment").value,
        }),
      })
        .then((response) => {
          if (response.ok) {
            return Promise.resolve(response.json());
          } else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
                response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
      sessionStorage.setItem("Comt1", JSON.stringify(hod));
      navigate1("/comment");
    });
    document.body.appendChild(btn4);
  }
  addComment();

  return <div></div>;
}

export default AddComment;
