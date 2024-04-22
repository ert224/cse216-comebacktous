import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import Newuser from "./newuser";

function Profile() {
  let navigate = useNavigate();

  const home = () => {
    navigate("/home");
  };

  const newuser = () => {
    navigate("/newuser");
  };

  // update profile to use GET request to recieve the info

  const user = JSON.parse(sessionStorage.getItem("jsArray"));
  console.log(user);

  return (
    <div className="container">
      <h1>PROFILE </h1>
      <p> Here is your information</p>
      <p> Welcome, {user.fName}</p>
      <p>
        {" "}
        <b> Email: </b> {user.email}
      </p>
      <p>
        {" "}
        <b> user_id: </b> {user.userId}
      </p>
      <p>
        {" "}
        <b> Gender Identity: </b>
        {user.gi}
      </p>
      <p>
        {" "}
        <b> SexualOrientation </b>
        {user.so}
      </p>
      <p>
        {" "}
        <b> Note:</b> {user.note}
      </p>

      <input type="submit" value="return to main page" onClick={home} />
      <input type="submit" value="edit your info" onClick={newuser} />
    </div>
  );
}

export default Profile;
