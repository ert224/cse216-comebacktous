import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "./logo.svg";
//import './App.css';
import { Route, Link } from "react-router-dom";
import profile from "./profile";
import newuser from "./newuser";
import { useEffect, useState } from "react";

function Idea() {
  let navigate = useNavigate();
  const user = JSON.parse(sessionStorage.getItem("jsArray"));
  let inMessage = {
    Titl: "",
    Msg: "",
    uid: user.userId,
  };
  function addValuess() {
    inMessage.Titl = document.getElementById("title").value;
    inMessage.Msg = document.getElementById("Idea").value;
    // useEffect(() =>{

    // const add = async () => {
    console.log(inMessage);

    //return await
    fetch("https://dry-wave-47246.herokuapp.com/postidea", {
      method: "POST",
      headers: { "content-Type": "application/json" },
      body: JSON.stringify({
        title: inMessage.Titl,
        idea: inMessage.Msg,
        uid: inMessage.uid,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        console.log("HELLO");
      });
    navigate("/home");
  }

  return (
    <div className="container">
      <h1>
        <a href="/home">MAZE</a>
      </h1>
      <label for="name"> Title:</label>
      <input
        type="text"
        id="title"
        name="title"
        required
        size="50"
        className="form-control"
      />
      <br></br>
      <label for="name">Idea :</label>
      <input
        type="text"
        id="Idea"
        name="idea"
        required
        size="60"
        className="form-control"
      />
      <br></br>
      <input
        type="submit"
        value="Add Idea"
        onClick={() => {
          addValuess();
        }}
        className="form-control"
      />
    </div>
  );
}

export default Idea;
