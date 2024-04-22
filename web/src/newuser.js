import React from "react";
import {useNavigate} from "react-router-dom";
import logo from './logo.svg';
//import './App.css';
import {Route , Link } from 'react-router-dom';
import profile from './profile';
import newuser from './newuser';
import {useEffect, useState} from 'react';

function Newuser () {
    let navigate = useNavigate();
   const user = JSON.parse(sessionStorage.getItem("jsArray"));
    console.log(user);

    let SexualOrientation = "Straight";
    let fName = "";
    let lName = "";
    let GenderIdentity = "Male";
    let Note = "Default note ";
    let myArray = user.email.split("@");
    
   
    // This is the variable I will POST to the register 
    let UserCompleteD  = {
        userId : user.userId,
        email: user.email,
        userN : myArray[0],  //username
        so : SexualOrientation,
        gi :  GenderIdentity,
        note : Note,
        sessID : user.sID,
        fname : fName,
        lname : lName
      };

      //console.log(UserCompleteD);

      // User inputs Gender , SO , NOTE 

      function addValuess(){
       UserCompleteD.note = document.getElementById("Note").value;
       UserCompleteD.so = document.getElementById("sexO").value;
       UserCompleteD.gi = document.getElementById("gend").value;
       UserCompleteD.fName = document.getElementById("FName").value;
       UserCompleteD.lName = document.getElementById("LName").value;
        console.log(UserCompleteD);
        sessionStorage.setItem("jsArray1", JSON.stringify(UserCompleteD));

        //in this function we send the data to the back end 
        
        const register = async () => {
        return await fetch('https://dry-wave-47246.herokuapp.com/register', {
        method:'POST',
        headers:{'Accept': 'application/json',
                 'Content-Type': 'application/json'},
        body: JSON.stringify({
            "uid": UserCompleteD.userId,
            "username": UserCompleteD.userN,
            "email": UserCompleteD.email,
            "gi": UserCompleteD.gi,
            "so": UserCompleteD.so,
            "note": UserCompleteD.note,
            "fname": UserCompleteD.fname,
            "lname": UserCompleteD.lname
        })
      }).then((response) => (response.json()))
      
.then((data) => console.log(data));


}

register();
sessionStorage.setItem("toProfile", JSON.stringify(UserCompleteD));
navigate("/profile");
}


       
     

      
      

     // if (document.getElementById("Note").value != null){ addValuess();}

     

      
      //UserCompleteD.note = document.getElementById("addNote").value;
      //UserCompleteD.so = document.getElementById("Note");

    
    
    //add data for the user, and make it so that it saves to the user table 
return (
   
<div>
        <h1> Registration Page</h1>
        <p> Welcome, {UserCompleteD.userN}</p>
        <p> Here is some of the informaiton here we have about you</p>
        <p> <b> Email: </b> {user.email}</p>
        <p> <b> username: </b> {UserCompleteD.userN}</p>
        <p> <b> user_id: </b> {user.userId}</p>
        <p>Please choose a sexual Orientation that best describes you</p>

        


<form action="#">
      <label htmlFor="lang"> Sexual Orientation </label>
      <select name="Gender" id="sexO">
        <option value="HeteroSexual"> HeteroSexual </option>
        <option value="Gay"> Gay </option>
        <option value="Lesbian"> Lesbian</option>
        <option value="Queer"> Queer </option>
        <option value="Bi"> Bisexual </option>
      </select>
   
</form>

<p>What is your Gender Indentity </p>
    <form action="#">
      <label htmlFor="lang"> Gender Identity </label>
      <select name="Gender" id="gend">
        <option value="Male"> Male </option>
        <option value="Female"> Female </option>
        <option value="Other"> Other</option>
      </select>
    
    </form>
<p>Please add a brief Bio </p>
    
    <label>First Name: </label>
    <input id="FName" ></input>  
    <br></br>
    <label>Last Name: </label> 
    <input id="LName" ></input> 
    <br></br>
    <label>Note: </label>
    <input id="Note" ></input>
    <br></br>
    <input type="submit" value="save" onClick={() => {addValuess()}} />
    
</div>
    
    
);

} 

export default Newuser;

