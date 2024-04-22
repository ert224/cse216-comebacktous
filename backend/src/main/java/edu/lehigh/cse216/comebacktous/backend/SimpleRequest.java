package edu.lehigh.cse216.comebacktous.backend;

/**
 * SimpleRequest provides a format for clients to present title and message
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The title being provided by the client.
     */
    public String title;

    /**
     * The idea being provided by the client.
     */
    public String idea;

    /**
     * The comment being provided by the client.
     */
    public String comment;

    /**
     * The id of the idea post.
     */
    public int id;

    /**
     * The user id provided by the client.
     */
    public String uid;

    /**
     * The comment id.
     */
    public int cid;

    /**
     * The likes being provided by the users.
     */
    public int likes;

    /**
     * Username provided by the client.
     */
    public String username;

    /**
     * Email provided by the client.
     */
    public String email;

    /**
     * Gender identity provided by the client.
     */
    public String gi;

    /**
     * Sexual orientation provided by the client.
     */
    public String so;

    /**
     * Bio provided by the client.
     */
    public String note;

    /**
     * The code provided by the client.
     */
    public String code;

    /**
     * The redirect uri provided by the client
     */
    public String redirect;

    /**
     * The session id provided by the client.
     */
    public String session_id;

    /**
     * The first name provided by the client.
     */
    public String fname;

    /**
     * The last name provided by the client.
     */
    public String lname;

    /**
     * The file path specified by the user
     */
     public String file_contents; 

    /** 
     * The file type specified by the user
     */
     public String file_name;

     public String url;

     public String display_text;
}