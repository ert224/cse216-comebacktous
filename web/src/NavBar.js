import React from "react";
import { Link } from "react-router-dom";

function NavBar(){
    return (
        <ul>
            <li>
                <Link to = "/">newuser</Link>
            </li>
            <li>
                <Link to = "/">profile</Link>
            </li>
        </ul>
    )
}


export default NavBar;