import logo from "./logo.svg";
//import './App.css';
import { Route, Link, Navigate } from "react-router-dom";
import profile from "./profile";
import newuser from "./newuser";
import React from "react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  let navigate = useNavigate();
  let count = 0;

  // This constant indicates the path to our backend server (change to your own)
  const backendUrl = "https://dry-wave-47246.herokuapp.com";

  // getting the code from the return URL
  function getCurrentURL() {
    return window.location.href;
  }

  const url = getCurrentURL();

  //const url = 'https://dry-wave-47246.herokuapp.com/?code=4/0ARtbsJrUxM0qOojyuUrUUWAfhOIrUEUzRcFyBT0E6-Ef54x-koe1iyjsco8LBbXFM2OBgQ&scope=email%20https://www.googleapis.com/auth/userinfo.email%20openid&authuser=0&hd=lehigh.edu&prompt=consent';

  // Due to error I will simply pass dummies until we can fix it

  let userid = "12";
  let useremail = "smn224@lehigh.edu";

  let sessionID = "00";

  // pass this to the new user form page

  let userInf = {
    userId: userid,
    email: useremail,
    session_id: sessionID, //session ID
  };

  useEffect(() => {
    if (url.endsWith("none")) {
      console.log(url);
      let paramStr = url;
      let searchParams = new URLSearchParams(paramStr); // change the redirect
      let codeReturn = {
        code: searchParams.get("http://localhost:3000/?code"),
        redirect: "http://localhost:3000",
      }; //depends on the url before 'code'   https://dry-wave-47246.herokuapp.com
      console.log(codeReturn);
      const login = async () => {
        return await fetch("https://dry-wave-47246.herokuapp.com/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
          },
          body: JSON.stringify(codeReturn),
        })
          .then((response) => response.json())
          .then((data) => {
            console.log(data);
            if (data.mStatus == "ok") {
              if (data.mData.userStatus == 1) {
                // New user
                console.log("new user");
                userInf = {
                  userId: data.mData.id,
                  email: data.mData.email,
                  session_id: data.mData.session_id,
                };
                sessionStorage.setItem("jsArray", JSON.stringify(userInf));
                navigate("/newuser");
              } else {
                console.log("old user");
                userInf = {
                  userId: data.mData.id,
                  email: data.mData.email,
                  session_id: data.mData.session_id,
                };
                sessionStorage.setItem("jsArray", JSON.stringify(userInf));
                navigate("/profile");
              }
            }
          });
      };
      login();
    } //https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=72850831242-dc9tqvcge6iqhjmr6bdjl0uq435asqrg.apps.googleusercontent.com&redirect_uri=https://dry-wave-47246.herokuapp.com&scope=email
  }, []);

  //.then((response) => {console.log(response.json())})

  sessionStorage.setItem("jsArray", JSON.stringify(userInf));
  document.body.style.backgroundColor = "#ece5ff";
  // session id/token - generate it on the backend and send it to the front end

  // change the redirect uri

  return (
    <div className="h-100 d-flex align-items-center justify-content-center mt-5">
      <body>
        <a
          href="https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=72850831242-dc9tqvcge6iqhjmr6bdjl0uq435asqrg.apps.googleusercontent.com&redirect_uri=http://localhost:3000&scope=email"
          class="btn btn-primary"
        >
          MAZE Login
        </a>
      </body>
    </div>
  );
}

export default Login;
