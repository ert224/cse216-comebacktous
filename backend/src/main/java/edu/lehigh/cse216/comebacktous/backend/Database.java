package edu.lehigh.cse216.comebacktous.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// import java.util.Map;

public class Database {

  /**
   * The connection to the database. When there is no connection, it should
   * be null. Otherwise, there is a valid open connection
   */
  private Connection mConnection;

  /**
   * A prepared statement for getting all data in the database
   */
  private PreparedStatement selectAllIdeas;

  /**
   * A prepared statement for getting one row from the database
   */
  private PreparedStatement selectOneIdea;

  /**
   * A prepared statement for deleting a row from the database
   */
  private PreparedStatement deleteOneIdea;

  /**
   * A prepared statement for inserting comment into the database
   */
  private PreparedStatement insertOneComment;

  /**
   * A prepared statement for inserting idea into the database
   */
  private PreparedStatement insertOneIdea;

  /**
   * A prepared statement for adding a like
   */
  private PreparedStatement incrementLikes;

  /**
   * A prepared statement for removing a like
   */
  private PreparedStatement decrementLikes;

  /**
   * A prepared statement for getting all comments on a post
   */
  private PreparedStatement selectAllCommentsOnIdea;

  /**
   * A prepared statement for adding a dislike
   */
  private PreparedStatement incrementDislikes;

  /**
   * A prepared statement for removing a dislike
   */
  private PreparedStatement decrementDislikes;

  /**
   * A prepared statement for updating user profile
   */
  private PreparedStatement updateUserProfile;

  /**
   * A prepared statement for inserting new user
   */
  private PreparedStatement insertUser;

  /**
   * A prepared statement for getting single user
   */
  private PreparedStatement selectOneUser;

  /**
   * A prepared statement for updating comment
   */
  private PreparedStatement updateComment;

  /**
   * A prepared statement for clearing user vote on an idea
   */
  private PreparedStatement clearUserVote;

  /**
   * A prepared statement for getting like count for a post
   */
  private PreparedStatement getLikeCount;

  /**
   * A prepared statement for getting dislike count for a post
   */
  private PreparedStatement getDislikeCount;

  /**
   * A prepared statement for getting like status for user on a post
   */
  private PreparedStatement getVoteStatus;

  /** 
   * A Prepared statement for inserting a file into the file table
   */
  private PreparedStatement insertFile; 

  private PreparedStatement selectOneIdeaLink; 
  private PreparedStatement selectOneIdeaFile;
  private PreparedStatement selectOneCommentFile; 
  private PreparedStatement selectOneCommentLink; 

  private PreparedStatement insertLink;


  /**
   * IdeaRow is like a struct in C: we use it to hold data, and we allow
   * direct access to its fields. In the context of this Database, IdeaRow
   * represents the data we'd see in a row.
   *
   * We make IdeaRow a static class of Database because we don't really want
   * to encourage users to think of IdeaRow as being anything other than an
   * abstract representation of a row of the database. IdeaRow and the
   * Database are tightly coupled: if one changes, the other should too.
   */
  public static class IdeaRow {

    /**
     * The ID of this row of the database
     */
    int id;
    /**
     * The title stored in this row
     */
    String title;
    /**
     * The idea stored in this row
     */
    String idea;
    /**
     * The number of likes stored in this row
     */
    int likes;
    /**
     * The user who posted the idea
     */
    String uid;
    /**
     * The comments on the idea
     */
    Object[] comments;

    /**
     * The vote status for this user
     */
    int voteStatus;

    /**
     * The username of the poster
     */
    String username;

    FileRow file;
    LinkRow link;

    /**
     * Construct a IdeaRow object by providing values for its fields
     */
    public IdeaRow(
      int id,
      String title,
      String idea,
      int likes,
      String uid,
      String username
    ) {
      this.id = id;
      this.title = title;
      this.idea = idea;
      this.likes = likes;
      this.uid = uid;
      this.username = username;
    }

    /**
     * Construct a IdeaRow object by providing values for its fields
     */
    public IdeaRow(
      int id,
      String title,
      String idea,
      int likes,
      String uid,
      String username,
      int voteStatus
    ) {
      this.id = id;
      this.title = title;
      this.idea = idea;
      this.likes = likes;
      this.uid = uid;
      this.voteStatus = voteStatus;
      this.username = username;
    }

    /**
     * Construct a IdeaRow object by providing values for its fields
     */
    public IdeaRow(
      int id,
      String title,
      String idea,
      int likes,
      String uid,
      String username,
      Object[] comments
    ) {
      this.id = id;
      this.title = title;
      this.idea = idea;
      this.likes = likes;
      this.comments = comments;
      this.uid = uid;
      this.username = username;
    }

    /**
     * Construct a IdeaRow object by providing values for its fields
     */
    public IdeaRow(
      int id,
      String title,
      String idea,
      int likes,
      String uid,
      String username,
      Object[] comments,
      int voteStatus
    ) {
      this.id = id;
      this.title = title;
      this.idea = idea;
      this.likes = likes;
      this.comments = comments;
      this.uid = uid;
      this.voteStatus = voteStatus;
      this.username = username;
    }
  }

  public static class UserRow {

    /**
     * The ID of this row of the database
     */
    String uid;
    /**
     * The username stored in this row
     */
    String username;
    /**
     * The email stored in this row
     */
    String email;

    /**
     * The gender of the user
     */
    String gender;

    /**
     * The sexual orientation of the user
     */
    String orientation;

    /**
     * The note for the user
     */
    String note;

    /**
     * The first name of the user
     */
    String fname;

    /**
     * The last name of the user
     */
    String lname;

    /**
     * Construct a UserRow object by providing values for its fields
     */
    public UserRow(
      String uid,
      String username,
      String email,
      String fname,
      String lname,
      String GI,
      String SO,
      String note
    ) {
      this.uid = uid;
      this.username = username;
      this.email = email;
      this.note = note;
      this.gender = GI;
      this.orientation = SO;
      this.fname = fname;
      this.lname = lname;
    }
  }

  public static class CommentRow {

    /**
     * The ID of the idea this comment was posted on
     */
    int id;
    /**
     * The comment stored in this row
     */
    String comment;
    /**
     * The user who posted this comment
     */
    String uid;
    /**
     * The username of the poster
     */
    String username;

    FileRow file;
    LinkRow link;

    /**
     * Construct a IdeaRow object by providing values for its fields
     */
    public CommentRow(int id, String comment, String uid, String username) {
      this.id = id;
      this.comment = comment;
      this.uid = uid;
      this.username = username;
    }
  }

  public static class VoteRow {

    /**
     * The ID of the idea this vote was posted on
     */
    int id;
    /**
     * The user who posted this vote
     */
    String uid;
    /**
     * The vote stored in this row, 0 for dislike, 1 for like
     */
    int vote;

    /**
     * Construct a VoteRow object by providing values for its fields
     */
    public VoteRow(int id, String uid, int vote) {
      this.id = id;
      this.uid = uid;
      this.vote = vote;
    }
  } 

  public static class FileRow {

    /**
     * The id of the associated comment
     */
    int comment_id;
    /** 
     * The id of an idea
     */
    int id;
    /** 
     * The file_name of a file object
     */
    String file_name;

    /**
     * Construct a FileRow object by providing values for its fields
     */
    public FileRow(int id, int comment_id, String file_name) {
      this.id = id;
      this.comment_id = comment_id;
      this.file_name = file_name;
    }
  }

  public static class LinkRow {

    /**
     * The id of the associated comment
     */
    int comment_id;
    /** 
     * The id of an idea
     */
    int id;
    /** 
     * The file_name of a file object
     */
    String display_text;

    String url; 

    int link_id;

    /**
     * Construct a FileRow object by providing values for its fields
     */
    public LinkRow(int link_id, int id, int comment_id, String display_text, String url) {
      this.link_id = link_id;
      this.id = id;
      this.comment_id = comment_id;
      this.display_text = display_text;
      this.url = url;
    }
  }

  /*
   * Session information
   */
  public static class Session {

    String uid;
    String sessionid;
  }

  /**
   * The Database constructor is private: we only create Database objects
   * through the getDatabase() method.
   */
  private Database() {}

  /**
   * Get a fully-configured connection to the database
   *
   * @param ip   The IP address of the database server
   * @param port The port on the database server to which connection requests
   *             should be sent
   * @param user The user ID to use when connecting
   * @param pass The password to use when connecting
   *
   * @return A Database object, or null if we cannot connect properly
   */
  static Database getDatabase(String db_url) {
    // Create an un-configured Database object
    Database db = new Database();

    // Give the Database object a connection, fail if we cannot get one
    try {
      Class.forName("org.postgresql.Driver");
      URI dbUri = new URI(db_url);
      String username = dbUri.getUserInfo().split(":")[0];
      String password = dbUri.getUserInfo().split(":")[1];
      String dbUrl =
        "jdbc:postgresql://" +
        dbUri.getHost() +
        ':' +
        dbUri.getPort() +
        dbUri.getPath();
      Connection conn = DriverManager.getConnection(dbUrl, username, password);
      if (conn == null) {
        System.err.println(
          "Error: DriverManager.getConnection() returned a null object"
        );
        return null;
      }
      db.mConnection = conn;
    } catch (SQLException e) {
      System.err.println(
        "Error: DriverManager.getConnection() threw a SQLException"
      );
      e.printStackTrace();
      return null;
    } catch (ClassNotFoundException cnfe) {
      System.out.println("Unable to find postgresql driver");
      return null;
    } catch (URISyntaxException s) {
      System.out.println("URI Syntax Error");
      return null;
    }

    // Attempt to create all of our prepared statements. If any of these
    // fail, the whole getDatabase() call should fail
    try {
      // NB: we can easily get ourselves in trouble here by typing the
      // SQL incorrectly. We really should have things like "ideas"
      // as constants, and then build the strings for the statements
      // from those constants.

      // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
      // creation/deletion, so multiple executions will cause an exception

      // Standard CRUD operations
      db.deleteOneIdea =
        db.mConnection.prepareStatement("DELETE FROM ideas WHERE id = ?");
      db.insertOneIdea =
        db.mConnection.prepareStatement(
          "INSERT INTO ideas VALUES (default, ?, ?, ?, 'v')"
        );
      db.insertOneComment =
        db.mConnection.prepareStatement(
          "INSERT INTO comments VALUES (default, ?, ?, ?)"
        );

      db.selectAllIdeas =
        db.mConnection.prepareStatement(
          "select * from (ideas inner join users on ideas.user_id=users.user_id) full outer join files on ideas.id = files.id ORDER BY title ASC"
        );
      db.selectOneIdea =
        db.mConnection.prepareStatement(
          "Select * from (ideas inner join users on ideas.user_id = users.user_id) full outer join files on ideas.id = files.id where ideas.id = ? and (files.file_name is Null OR files.comment_id = -1)"
        );
      db.selectAllCommentsOnIdea =
        db.mConnection.prepareStatement(
          "SELECT * from (comments INNER JOIN users on comments.user_id=users.user_id) FULL OUTER JOIN files on comments.comment_id = files.comment_id WHERE comments.id=?"
        );

      db.clearUserVote =
        db.mConnection.prepareStatement(
          "DELETE FROM votes WHERE id=? AND user_id=?"
        );
      // post id, user id, 1 for like
      db.incrementLikes =
        db.mConnection.prepareStatement(
          "INSERT INTO votes VALUES (default, ?, ?, 1)"
        );
      // post id, user id, 0 for dislike
      db.incrementDislikes =
        db.mConnection.prepareStatement(
          "INSERT INTO votes VALUES (default, ?, ?, 0)"
        );

      db.decrementLikes =
        db.mConnection.prepareStatement(
          "DELETE FROM votes WHERE id=? AND user_id=?"
        );
      db.decrementDislikes =
        db.mConnection.prepareStatement(
          "DELETE FROM votes WHERE id=? AND user_id=?"
        );

      db.updateUserProfile =
        db.mConnection.prepareStatement(
          "UPDATE users SET first_name=?, last_name=?, gender_identity=?, orientation=?, notes=? WHERE user_id=?"
        );
      db.insertUser =
        db.mConnection.prepareStatement(
          "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
      db.selectOneUser =
        db.mConnection.prepareStatement("SELECT * from users WHERE user_id=?");

      db.updateComment =
        db.mConnection.prepareStatement(
          "UPDATE comments SET comment=? WHERE comment_id=?"
        );

      db.getDislikeCount =
        db.mConnection.prepareStatement(
          "SELECT COUNT(*) FROM votes WHERE id=? AND vote=0"
        );
      db.getLikeCount =
        db.mConnection.prepareStatement(
          "SELECT COUNT(*) FROM votes WHERE id=? AND vote=1"
        );

      db.getVoteStatus =
        db.mConnection.prepareStatement(
          "SELECT vote from votes where id=? AND user_id=?"
        );
      
      db.insertFile = 
        db.mConnection.prepareStatement(
          "INSERT into files VALUES (?, ?, ?)"
        ); 

      db.insertLink = 
        db.mConnection.prepareStatement(
          "INSERT into link VALUES (DEFAULT, ?, ?, ?, ?)"
        ); 
      
      db.selectOneIdeaLink = db.mConnection.prepareStatement("Select * from link where id=?");
      db.selectOneIdeaFile = db.mConnection.prepareStatement("Select * from files where id=?");
      db.selectOneCommentLink= db.mConnection.prepareStatement("select * from link where comment_id=?");
      db.selectOneCommentFile = db.mConnection.prepareStatement("select * from files where comment_id=?");

      
    } catch (SQLException e) {
      System.err.println("Error creating prepared statement");
      e.printStackTrace();
      db.disconnect();
      return null;
    }
    return db;
  }

  /**
   * Close the current connection to the database, if one exists.
   *
   * NB: The connection will always be null after this call, even if an
   * error occurred during the closing operation.
   *
   * @return True if the connection was cleanly closed, false otherwise
   */
  boolean disconnect() {
    if (mConnection == null) {
      System.err.println("Unable to close connection: Connection was null");
      return false;
    }
    try {
      mConnection.close();
    } catch (SQLException e) {
      System.err.println("Error: Connection.close() threw a SQLException");
      e.printStackTrace();
      mConnection = null;
      return false;
    }
    mConnection = null;
    return true;
  }

  /**
   * Insert a row into the database
   *
   * @param title The title for this new row
   * @param idea  The idea body for this new row
   *
   * @return The number of rows that were inserted
   */
  int insertIdea(String title, String idea, String uid) {
    int count = 0;
    try {
      insertOneIdea.setString(1, uid);
      insertOneIdea.setString(2, title);
      insertOneIdea.setString(3, idea);
      count += insertOneIdea.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  int insertUser(
    String uid,
    String username,
    String email,
    String fname,
    String lname,
    String gi,
    String so,
    String note
  ) {
    int count = 0;
    try {
      insertUser.setString(1, uid);
      insertUser.setString(2, username);
      insertUser.setString(3, email);
      insertUser.setString(4, fname);
      insertUser.setString(5, lname);
      insertUser.setString(6, gi);
      insertUser.setString(7, so);
      insertUser.setString(8, note);
      insertUser.setString(9, "v");
      count += insertUser.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  int editComment(String comment, int id) {
    int count = 0;
    try {
      updateComment.setString(1, comment);
      updateComment.setInt(2, id);
      count += updateComment.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  int insertComment(String comment, int postID, String uid) {
    int count = 0;
    try {
      insertOneComment.setInt(1, postID);
      insertOneComment.setString(2, uid);
      insertOneComment.setString(3, comment);
      count += insertOneComment.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  /**
   * Query the database for a list of all titles and their IDs
   *
   * @return All rows, as an ArrayList
   */
  ArrayList<IdeaRow> selectAllIdeas() {
    ArrayList<IdeaRow> res = new ArrayList<IdeaRow>();
    try {
      ResultSet rs = selectAllIdeas.executeQuery();
      while (rs.next()) {
        // Get likes and dislikes for this post
        getLikeCount.setInt(1, rs.getInt("id"));
        ResultSet likes = getLikeCount.executeQuery();
        getDislikeCount.setInt(1, rs.getInt("id"));
        ResultSet dislikes = getDislikeCount.executeQuery();
        int likesMinusDislikes = 0;
        if (likes.next()) {
          likesMinusDislikes = likes.getInt(1);
        }
        if (dislikes.next()) {
          likesMinusDislikes -= dislikes.getInt(1);
        }
        res.add(
          new IdeaRow(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("idea"),
            likesMinusDislikes,
            rs.getString("user_id"),
            rs.getString("username")
          )
        );
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      res.add(new IdeaRow(0, e.toString(), "error", 1, "error checking", "errors"));//debug
      return res;
    }
  }

  int getVoteStatus(String uid, int id) {
    int res = 0;
    try {
      getVoteStatus.setInt(1, id);
      getVoteStatus.setString(2, uid);
      ResultSet rs = getVoteStatus.executeQuery();
      if (rs.next()) {
        res = rs.getInt("vote");
      } else {
        return -99;
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  UserRow selectOneUser(String uid) {
    UserRow res = null;
    try {
      selectOneUser.setString(1, uid);
      ResultSet rs = selectOneUser.executeQuery();
      if (rs.next()) {
        res =
          new UserRow(
            rs.getString("user_id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("gender_identity"),
            rs.getString("orientation"),
            rs.getString("notes")
          );
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  LinkRow selectOneIdeaLink(int id) {
    LinkRow res = null;
    try {
      selectOneIdeaLink.setInt(1, id);
      ResultSet rs = selectOneIdeaLink.executeQuery();
      if (rs.next()) {
        res =
          new LinkRow(
            rs.getInt("lid"),
            rs.getInt("id"),
            rs.getInt("comment_id"),
            rs.getString("display_text"),
            rs.getString("url")
          );
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  FileRow selectOneIdeaFile(int id) {
    FileRow res = null;
    try {
      selectOneIdeaFile.setInt(1, id);
      ResultSet rs = selectOneIdeaFile.executeQuery();
      if (rs.next()) {
        res =
          new FileRow(
            rs.getInt("id"),
            rs.getInt("comment_id"), 
            rs.getString("file_name")
          );
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  LinkRow selectOneCommentLink(int id) {
    LinkRow res = null;
    try {
      selectOneCommentLink.setInt(1, id);
      ResultSet rs = selectOneCommentLink.executeQuery();
      if (rs.next()) {
        res =
          new LinkRow(
            rs.getInt("lid"),
            rs.getInt("id"),
            rs.getInt("comment_id"),
            rs.getString("display_text"), 
            rs.getString("url")
          );
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  FileRow selectOneCommentFile(int id) {
    FileRow res = null;
    try {
      selectOneCommentFile.setInt(1, id);
      ResultSet rs = selectOneCommentFile.executeQuery();
      if (rs.next()) {
        res =
          new FileRow(
            rs.getInt("id"),
            rs.getInt("comment_id"), 
            rs.getString("file_name")
          );
      }
      rs.close();
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get all data for a specific row, by ID
   *
   * @param id The id of the row being requested
   *
   * @return The data for the requested row, or null if the ID was invalid
   */
  IdeaRow selectOneIdea(int id) {
    IdeaRow res = null;
    try {
      // Get likes and dislikes for this post
      getLikeCount.setInt(1, id);
      ResultSet likes = getLikeCount.executeQuery();
      getDislikeCount.setInt(1, id);
      ResultSet dislikes = getDislikeCount.executeQuery();
      int likesMinusDislikes = 0;
      if (likes.next()) {
        likesMinusDislikes = likes.getInt(1);
      }
      if (dislikes.next()) {
        likesMinusDislikes -= dislikes.getInt(1);
      }
      selectOneIdea.setInt(1, id);
      ResultSet rs = selectOneIdea.executeQuery();
      selectAllCommentsOnIdea.setInt(1, id);
      ResultSet commentsRS = selectAllCommentsOnIdea.executeQuery();
      ArrayList<CommentRow> comments = new ArrayList<CommentRow>();
      while (commentsRS.next()) {
        comments.add(
          new CommentRow(
            commentsRS.getInt("comment_id"),
            commentsRS.getString("comment"),
            commentsRS.getString("user_id"),
            commentsRS.getString("username")
          )
        );
      }
      if (rs.next()) {
        res =
          new IdeaRow(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("idea"),
            likesMinusDislikes,
            rs.getString("user_id"),
            rs.getString("username"),
            comments.toArray()
          );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * Delete a row by ID
   *
   * @param id The id of the row to delete
   *
   * @return The number of rows that were deleted. -1 indicates an error.
   */
  int deleteIdea(int id) {
    int res = -1;
    try {
      deleteOneIdea.setInt(1, id);
      res = deleteOneIdea.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * Update the likes for a row in the database
   *
   * @param id   The id of the row to update
   * @param idea The new idea contents
   *
   * @return The number of rows that were updated. -1 indicates an error.
   */
  int updateLikes(int id, String uid, int tf, int upDown) {
    int res = -1;
    try {
      clearUserVote.setInt(1, id);
      clearUserVote.setString(2, uid);
      clearUserVote.executeUpdate();
      if (upDown == 1) {
        if (tf == 1) { // add a like
          incrementLikes.setInt(1, id);
          incrementLikes.setString(2, uid);
          res = incrementLikes.executeUpdate();
        } else if (tf == 0) { // remove a like
          decrementLikes.setInt(1, id);
          decrementLikes.setString(2, uid);
          res = decrementLikes.executeUpdate();
        }
      } else {
        if (tf == 1) { // add a dislike
          incrementDislikes.setInt(1, id);
          incrementDislikes.setString(2, uid);
          res = incrementDislikes.executeUpdate();
        } else if (tf == 0) { // remove a dislike
          decrementDislikes.setInt(1, id);
          decrementDislikes.setString(2, uid);
          res = decrementDislikes.executeUpdate();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
  }

  /*
   * Update user profile
   */
  int updateUserProf(
    String uid,
    String username,
    String email,
    String fname,
    String lname,
    String gi,
    String so,
    String note
  ) {
    int res = -1;
    try {
      updateUserProfile.setString(1, fname);
      updateUserProfile.setString(2, lname);
      updateUserProfile.setString(3, gi);
      updateUserProfile.setString(4, so);
      updateUserProfile.setString(5, note);
      updateUserProfile.setString(6, uid);
      res = updateUserProfile.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
  }

  /*
   * Login a user
   */
  int login(String uid, String email) {
    int res = -1;
    try {
      selectOneUser.setString(1, uid);
      ResultSet rs = selectOneUser.executeQuery();
      if (rs.next()) { // user exists, login
        res = 0;
      } else { // user does not exist
        res = 1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
  }

  int insertFile(int id, int comment_id, String file_name) {
    int count = 0; 
    try { 
      insertFile.setString(1, file_name); 
      insertFile.setInt(2, id); 
      insertFile.setInt(3, comment_id); 

      count += insertFile.executeUpdate(); 
     
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }
  
  int insertLink(int id, int comment_id, String display_text, String url) { 
     int count = 0; 
    try { 
      insertLink.setInt(1, id);
      insertLink.setInt(2, comment_id);
      insertLink.setString(3, display_text);
      insertLink.setString(4, url); 

      count += insertLink.executeUpdate(); 
     
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }
}
