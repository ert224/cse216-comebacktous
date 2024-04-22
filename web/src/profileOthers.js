import React, { useState, useRef } from "react";
import {useNavigate} from "react-router-dom";
import Newuser from "./newuser";

function ProfileOthers(){

    let navigate = useNavigate();

    const home = () => {
        navigate("/home");
    }

   


    // update profile to use GET request to recieve the info 
    
    const user = JSON.parse(sessionStorage.getItem("prof"));
     console.log(user);
     
    return (
        <div>
    
        
        <p> <h1> {user.Fname}'s profile </h1> </p>
        <p> <b> Email: </b> {user.email}</p>
        <p> <b> First Name: </b> {user.Fname}</p>
        <p> <b> Last Name: </b> {user.Lname}</p>    
        <p> <b> user_id: </b> {user.userId}</p>
        <p> <b> Note:</b> {user.note}</p>

       <input type="submit" value="return to main page" onClick={home} />
     





        </div>
    );
}

export default ProfileOthers;