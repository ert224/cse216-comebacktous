//import './App.css';
import { Route, Routes } from "react-router-dom";
import Profile from "./profile";
import Login from "./login";
import Newuser from "./newuser";
import Home from "./home";
import React from "react";
import Idea from "./idea";
import Comment from "./comment";
import addComment from "./addComment";
import { useEffect, useState } from "react";
import AddComment from "./addComment";
import ProfileOthers from "./profileOthers";
import "bootstrap/dist/css/bootstrap.css";
import IdeaInfo from "./ideaInfo";

let log = false;
function App() {
  return (
    <div className="App">
      <title>MAZE</title>
      <Routes>
        <Route exact path="/*" element={<Login />}></Route>
        <Route exact path="/profileOthers" element={<ProfileOthers />}></Route>
        <Route exact path="/newuser" element={<Newuser />}></Route>
        <Route exact path="/profile" element={<Profile />}></Route>
        <Route exact path="/ideaInfo" element={<IdeaInfo />}></Route>
        <Route exact path="/home" element={<Home />}></Route>
        <Route exact path="/idea" element={<Idea />}></Route>
        <Route exact path="/comment" element={<Comment />}></Route>
        <Route exact path="/addComment" element={<AddComment />}></Route>
      </Routes>
    </div>
  );
}

export default App;
