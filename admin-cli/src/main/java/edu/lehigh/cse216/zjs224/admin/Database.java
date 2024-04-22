package edu.lehigh.cse216.zjs224.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Map;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should
     * be null. Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAllFromIdeas;
    private PreparedStatement mSelectAllFromUsers;
    private PreparedStatement mSelectAllFromComments;
    private PreparedStatement mSelectAllFromVotes;
    private PreparedStatement mSelectAllFromFiles;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOneFromIdeas;
    private PreparedStatement mSelectOneFromUsers;
    private PreparedStatement mSelectOneFromComments;
    private PreparedStatement mSelectOneFromVotes;
    private PreparedStatement mSelectOneFromFiles;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOneFromIdeas;
    private PreparedStatement mDeleteOneFromUsers;
    private PreparedStatement mDeleteOneFromVotes;
    private PreparedStatement mDeleteOneFromComments;
    private PreparedStatement mDeleteOneFromFiles;

    /**
     * 
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOneFromIdeas;
    private PreparedStatement mInsertOneFromUsers;
    private PreparedStatement mInsertOneFromVotes;
    private PreparedStatement mInsertOneFromComments;
    private PreparedStatement mInsertOneFromFiles;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTableIdeas;
    private PreparedStatement mCreateTableVotes;
    private PreparedStatement mCreateTableUsers;
    private PreparedStatement mCreateTableComments;
    private PreparedStatement mCreateTableFiles;
    private PreparedStatement mCreateTableLink;
    private PreparedStatement mFileCreator;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTableIdeas;
    private PreparedStatement mDropTableVotes;
    private PreparedStatement mDropTableUsers;
    private PreparedStatement mDropTableComments;
    private PreparedStatement mDropTableFiles;
    private PreparedStatement mDropTableLink;

    private PreparedStatement mInvalidateRowFromIdeas;
    private PreparedStatement mInvalidateRowFromUsers;
    private PreparedStatement mInvalidateRowFromFiles;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow
     * direct access to its fields. In the context of this Database, RowData
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database. RowData and the
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class IdeaRowData {
        int mId;
        String mtitle;
        String midea;
        String mUserID;
        String mValid;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public IdeaRowData(int id, String user_id, String title, String idea, String validity) {
            mId = id;
            mUserID = user_id;
            mtitle = title;
            midea = idea;
            mValid = validity;
        }
    }

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow
     * direct access to its fields. In the context of this Database, RowData
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database. RowData and the
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class FileRowData {
        String mFile_Name;
        int mID;
        int mComment_ID;
        String mDisplay_text;
        String mUrl;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public FileRowData(int id, String file_name, int comment_id, String Url, String Display_text) {
            mID = id;
            mFile_Name = file_name;
            mComment_ID = comment_id;
            mDisplay_text = Display_text;
            mUrl = Url;
        }
    }

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow
     * direct access to its fields. In the context of this Database, RowData
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database. RowData and the
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class UserRowData {
        /**
         * The ID of this row of the database
         */
        int mId;
        String mUserID;
        String mValid;
        String mEmail;
        String mUsername;
        String mGenIden;
        String mOrientation;
        String mNote;
        String mFirstName;
        String mLastName;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public UserRowData(String user_id, String username, String email, String first_name, String last_name,
                String gender_identity, String orientation, String notes, String validity) {
            mUserID = user_id;
            mUsername = username;
            mEmail = email;
            mFirstName = first_name;
            mLastName = last_name;
            mGenIden = gender_identity;
            mOrientation = orientation;
            mNote = notes;
            mValid = validity;
        }
    }

    public static class CommentRowData {
        /**
         * The ID of this row of the database
         */
        int mId;
        String mUserID;
        int mCommentId;
        String mComment;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public CommentRowData(int comment_id, int id, String user_id, String comment) {
            mCommentId = comment_id;
            mId = id;
            mUserID = user_id;
            mComment = comment;
        }
    }

    public static class VoteRowData {
        int mId;
        String mUserID;
        int mVoteId;
        int mVotes;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public VoteRowData(int vote_id, int id, String user_id, int votes) {
            mVoteId = vote_id;
            mId = id;
            mUserID = user_id;
            mVotes = votes;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

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
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
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
            db.mCreateTableIdeas = db.mConnection.prepareStatement(
                    "CREATE TABLE ideas (id SERIAL, user_id varchar(100), title VARCHAR(50) NOT NULL, idea VARCHAR(500) NOT NULL, validity varchar(1),"
                            + " Primary Key(id), Foreign Key(user_id) references users(user_id)); ");
            db.mDropTableIdeas = db.mConnection.prepareStatement("DROP TABLE ideas");

            db.mCreateTableLink = db.mConnection.prepareStatement(
                    "CREATE TABLE link (lid SERIAL, id int, comment_id int , display_text VARCHAR(500) NOT NULL, url varchar(500),"
                            + " Primary Key(lid)); ");
            db.mDropTableLink = db.mConnection.prepareStatement("DROP TABLE link");

            db.mCreateTableFiles = db.mConnection.prepareStatement(
                    "CREATE TABLE files (file_name VARCHAR(500) , id int,  comment_id int , Primary key( file_name)) ;"); // Foreign
            // Key(id),Foreign Key(id) references ideas(id), Foreign Key( comment_id)
            // references comments(comment_id));"); // Foreign
            // Key(id)
            // references
            // ideas(id))
            // ,
            // Foreign

            db.mDropTableFiles = db.mConnection.prepareStatement("DROP TABLE files");

            db.mCreateTableUsers = db.mConnection.prepareStatement(
                    "CREATE TABLE users (user_id varchar(100) PRIMARY KEY, username VARCHAR(100)"
                            + " NOT NULL, email VARCHAR(50) NOT NULL, first_name VARCHAR(50), last_name VARCHAR(50), gender_identity VARCHAR(100), orientation varchar(100), notes varchar(100), validity varchar(1)); ");
            db.mDropTableUsers = db.mConnection.prepareStatement("DROP TABLE users");

            db.mCreateTableComments = db.mConnection.prepareStatement(
                    "CREATE TABLE comments (comment_id SERIAL, id int, user_id varchar(100), comment VARCHAR(200), "
                            + "Primary Key(comment_id), Foreign Key(id) references ideas(id), Foreign Key(user_id) references users(user_id));");
            db.mDropTableComments = db.mConnection.prepareStatement("DROP TABLE comments");

            // Find the file creator
            db.mFileCreator = db.mConnection.prepareStatement(
                    "SELECT username FROM users WHERE user_id = (SELECT user_id FROM comments,  files WHERE comments.comment_id = files.comment_id)");

            db.mCreateTableVotes = db.mConnection.prepareStatement(
                    "CREATE TABLE votes (vote_id SERIAL, id int, user_id varchar(100), vote int, "
                            + " Primary Key(vote_id), Foreign Key(id) references ideas(id), "
                            + " Foreign Key(user_id) references users(user_id));");
            db.mDropTableVotes = db.mConnection.prepareStatement("DROP TABLE votes");

            // Standard CRUD operations
            db.mDeleteOneFromIdeas = db.mConnection.prepareStatement("DELETE FROM ideas WHERE id = ? and user_id = ?");
            db.mInsertOneFromIdeas = db.mConnection
                    .prepareStatement("INSERT INTO ideas VALUES (default, ?, ?, ?, 'v')");
            db.mSelectAllFromIdeas = db.mConnection.prepareStatement("SELECT * FROM ideas");
            db.mSelectOneFromIdeas = db.mConnection.prepareStatement("SELECT * from ideas WHERE id=? and user_id = ?");

            db.mDeleteOneFromUsers = db.mConnection.prepareStatement("DELETE FROM users WHERE user_id = ?");
            db.mInsertOneFromUsers = db.mConnection
                    .prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'v')");
            db.mSelectAllFromUsers = db.mConnection.prepareStatement("SELECT * FROM users");
            db.mSelectOneFromUsers = db.mConnection.prepareStatement("SELECT * from users WHERE user_id=?");

            db.mDeleteOneFromComments = db.mConnection
                    .prepareStatement("DELETE FROM comments WHERE comment_id = ? and id = ? and user_id = ?");
            db.mInsertOneFromComments = db.mConnection
                    .prepareStatement("INSERT INTO comments VALUES (default, ?, ?, ?)");
            db.mSelectAllFromComments = db.mConnection.prepareStatement("SELECT * FROM comments");
            db.mSelectOneFromComments = db.mConnection
                    .prepareStatement("SELECT * from comments WHERE comment_id=? and id = ? and user_id = ?");

            db.mDeleteOneFromVotes = db.mConnection
                    .prepareStatement("DELETE FROM votes WHERE vote_id = ? and id = ? and user_id = ?");
            db.mInsertOneFromVotes = db.mConnection.prepareStatement("INSERT INTO votes VALUES (default, ?, ?, ?)");
            db.mSelectAllFromVotes = db.mConnection.prepareStatement("SELECT * FROM votes");
            db.mSelectOneFromVotes = db.mConnection
                    .prepareStatement("SELECT * from votes WHERE vote_id=? and id = ? and user_id = ?");

            db.mDeleteOneFromFiles = db.mConnection
                    .prepareStatement("DELETE FROM files WHERE comment_id = ? and id = ? ");
            db.mInsertOneFromFiles = db.mConnection.prepareStatement("INSERT INTO files VALUES (?, ?, ?, ?, ?)");
            db.mSelectAllFromFiles = db.mConnection.prepareStatement("SELECT * FROM files");
            db.mSelectOneFromFiles = db.mConnection
                    .prepareStatement("SELECT * from files WHERE comment_id=? and id = ?");

            db.mInvalidateRowFromIdeas = db.mConnection
                    .prepareStatement("UPDATE ideas SET validity = ? Where id = ? and user_id = ?");
            db.mInvalidateRowFromUsers = db.mConnection
                    .prepareStatement("UPDATE users SET validity = ? Where user_id = ?");
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
     * Insert a row into Users
     * 
     * @param title The title for this new row
     * @param idea  The idea body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowIntoIdeas(String user_id, String title, String idea) {
        int count = 0;
        try {
            mInsertOneFromIdeas.setString(1, user_id);
            mInsertOneFromIdeas.setString(2, title);
            mInsertOneFromIdeas.setString(3, idea);
            count += mInsertOneFromIdeas.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    /**
     * Insert a row into Files
     * 
     * @param id           The id for the post new row
     * @param comment_id   comment_id for this new row
     * @param url
     * @param display_text
     * @return The number of rows that were inserted
     */
    int insertRowIntoFiles(String file_name, int id, int comment_id, String url, String display_text) {
        int count = 0;
        try {

            mInsertOneFromFiles.setString(1, file_name);
            mInsertOneFromFiles.setInt(2, id);
            mInsertOneFromFiles.setInt(3, comment_id);
            mInsertOneFromFiles.setString(4, url);
            mInsertOneFromFiles.setString(5, display_text);
            count += mInsertOneFromFiles.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    /**
     * Insert a row into Ideas
     * 
     * @param title The title for this new row
     * @param idea  The idea body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowIntoUsers(String user_id, String username, String email, String first_name, String last_name,
            String gender_identity, String orientation, String notes) {
        int count = 0;
        try {
            mInsertOneFromUsers.setString(1, user_id);
            mInsertOneFromUsers.setString(2, username);
            mInsertOneFromUsers.setString(3, email);
            mInsertOneFromUsers.setString(4, first_name);
            mInsertOneFromUsers.setString(5, last_name);
            mInsertOneFromUsers.setString(6, gender_identity);
            mInsertOneFromUsers.setString(7, orientation);
            mInsertOneFromUsers.setString(8, notes);
            count += mInsertOneFromUsers.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;

    }

    /**
     * Insert a row into Comments
     * 
     * @param title The title for this new row
     * @param idea  The idea body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowIntoComments(int id, String user_id, String comment) {
        int count = 0;
        try {
            mInsertOneFromComments.setInt(1, id);
            mInsertOneFromComments.setString(2, user_id);
            mInsertOneFromComments.setString(3, comment);
            count += mInsertOneFromComments.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    /**
     * Insert a row into Votes
     * 
     * @param title The title for this new row
     * @param idea  The idea body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowIntoVotes(int id, String user_id, int vote) {
        int count = 0;
        try {
            mInsertOneFromVotes.setInt(1, id);
            mInsertOneFromVotes.setString(2, user_id);
            mInsertOneFromVotes.setInt(3, vote);
            count += mInsertOneFromVotes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    IdeaRowData selectOneFromIdeas(int id, String user_id) {
        IdeaRowData res = null;
        try {
            mSelectOneFromIdeas.setInt(1, id);
            mSelectOneFromIdeas.setString(2, user_id);
            ResultSet rs = mSelectOneFromIdeas.executeQuery();
            if (rs.next()) {
                res = new IdeaRowData(rs.getInt("id"), rs.getString("user_id"), rs.getString("title"),
                        rs.getString("idea"), rs.getString("validity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    FileRowData selectoneFromFiles(int comment_id, int id) {
        FileRowData res = null;
        try {
            mSelectOneFromFiles.setInt(1, comment_id);
            mSelectOneFromFiles.setInt(2, id);
            ResultSet rs = mSelectOneFromFiles.executeQuery();
            if (rs.next()) {

                res = new FileRowData(rs.getInt("id"), rs.getString("file_name"), rs.getInt("comment_id"),
                        rs.getString("url"), rs.getString("display_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    UserRowData selectOneFromUsers(String user_id) {
        UserRowData res = null;
        try {
            mSelectOneFromUsers.setString(1, user_id);
            ResultSet rs = mSelectOneFromUsers.executeQuery();
            if (rs.next()) {
                res = new UserRowData(rs.getString("user_id"), rs.getString("username"), rs.getString("email"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender_identity"),
                        rs.getString("orientation"), rs.getString("notes"), rs.getString("validity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    CommentRowData selectOneFromComments(int id, String user_id, int comment_id) {
        CommentRowData res = null;
        try {
            mSelectOneFromComments.setInt(1, comment_id);
            mSelectOneFromComments.setInt(2, id);
            mSelectOneFromComments.setString(3, user_id);
            ResultSet rs = mSelectOneFromComments.executeQuery();
            if (rs.next()) {
                res = new CommentRowData(rs.getInt("comment_id"), rs.getInt("id"), rs.getString("user_id"),
                        rs.getString("comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    VoteRowData selectOneFromVotes(int vote_id, int id, String user_id) {
        VoteRowData res = null;
        try {
            mSelectOneFromVotes.setInt(1, vote_id);
            mSelectOneFromVotes.setInt(2, id);
            mSelectOneFromVotes.setString(3, user_id);
            ResultSet rs = mSelectOneFromVotes.executeQuery();
            if (rs.next()) {
                res = new VoteRowData(rs.getInt("vote_id"), rs.getInt("id"), rs.getString("user_id"),
                        rs.getInt("vote"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    /**
     * Query the database for a list of all titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<IdeaRowData> selectAllFromIdeas() {

        ArrayList<IdeaRowData> res = new ArrayList<IdeaRowData>();
        try {
            ResultSet rs = mSelectAllFromIdeas.executeQuery();
            while (rs.next()) {
                res.add(new IdeaRowData(rs.getInt("id"), rs.getString("user_id"), rs.getString("title"),
                        rs.getString("idea"), rs.getString("validity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    /**
     * Query the database for a list of all titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<UserRowData> selectAllFromUsers() {
        ArrayList<UserRowData> res = new ArrayList<UserRowData>();
        try {
            ResultSet rs = mSelectAllFromUsers.executeQuery();
            while (rs.next()) {
                res.add(new UserRowData(rs.getString("user_id"), rs.getString("username"), rs.getString("email"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender_identity"),
                        rs.getString("orientation"), rs.getString("notes"), rs.getString("validity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    /**
     * Query the database for a list of all Files
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<FileRowData> selectAllFromFiles() {
        ArrayList<FileRowData> res = new ArrayList<FileRowData>();
        try {
            ResultSet rs = mSelectAllFromFiles.executeQuery();
            while (rs.next()) {

                res.add(new FileRowData(rs.getInt("id"), rs.getString("file_name"), rs.getInt("comment_id"),
                        rs.getString("url"), rs.getString("display_text")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    /**
     * Query the database for a list of all titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<CommentRowData> selectAllFromComments() {
        ArrayList<CommentRowData> res = new ArrayList<CommentRowData>();
        try {
            ResultSet rs = mSelectAllFromComments.executeQuery();
            while (rs.next()) {
                res.add(new CommentRowData(rs.getInt("comment_id"), rs.getInt("id"), rs.getString("user_id"),
                        rs.getString("comment")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    /**
     * Query the database for a list of all titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<VoteRowData> selectAllFromVotes() {
        ArrayList<VoteRowData> res = new ArrayList<VoteRowData>();
        try {
            ResultSet rs = mSelectAllFromVotes.executeQuery();
            while (rs.next()) {
                res.add(new VoteRowData(rs.getInt("vote_id"), rs.getInt("id"), rs.getString("user_id"),
                        rs.getInt("vote")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    /**
     * Insert a row into Users
     * 
     * @param title The title for this new row
     * @param idea  The idea body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int deleteRowFromIdeas(String user_id, int id) {
        int count = 0;
        try {
            mDeleteOneFromIdeas.setString(2, user_id);
            mDeleteOneFromIdeas.setInt(1, id);
            count += mDeleteOneFromIdeas.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    /**
     * Insert a row into Ideas
     * 
     * @param title The title for this new row
     * @param idea  The idea body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int deleteRowFromUsers(String user_id) {
        int count = 0;
        try {
            mDeleteOneFromUsers.setString(1, user_id);
            count += mDeleteOneFromUsers.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;

    }

    int deleteRowFromComments(int id, String user_id, int comment_id) {
        int count = 0;
        try {
            mDeleteOneFromComments.setInt(1, comment_id);
            mDeleteOneFromComments.setInt(2, id);
            mDeleteOneFromComments.setString(3, user_id);
            count += mDeleteOneFromComments.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    int deleteRowFromVotes(int id, String user_id, int vote) {
        int count = 0;
        try {
            mDeleteOneFromVotes.setInt(1, vote);
            mDeleteOneFromVotes.setInt(2, id);
            mDeleteOneFromVotes.setString(3, user_id);
            count += mDeleteOneFromVotes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    int deleteRowFromFiles(int comment_id, int id) {
        int count = 0;
        try {
            mDeleteOneFromFiles.setInt(1, comment_id);
            mDeleteOneFromFiles.setInt(2, id);
            count += mDeleteOneFromFiles.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    int invalidateRowFromIdeas(int id, String user_id, String bit) {
        int count = 0;
        try {
            mInvalidateRowFromIdeas.setString(1, bit);
            mInvalidateRowFromIdeas.setInt(2, id);
            mInvalidateRowFromIdeas.setString(3, user_id);
            count += mInvalidateRowFromIdeas.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    int invalidateRowFromUsers(String user_id, String bit) {
        int count = 0;
        try {
            mInvalidateRowFromUsers.setString(1, bit);
            mInvalidateRowFromUsers.setString(2, user_id);
            count += mInvalidateRowFromUsers.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    /**
     * Create ideas. If it already exists, this will print an error
     */
    int createTable() {
        try {
            // mCreateTableUsers.execute();
            // mCreateTableIdeas.execute();
            // mCreateTableComments.execute();
            // mCreateTableVotes.execute();
            // mCreateTableFiles.execute();
            mCreateTableLink.execute();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Remove ideas from the database. If it does not exist, this will print
     * an error.
     */
    int dropTable() {
        try {
            // mDropTableVotes.execute();
            // mDropTableComments.execute();
            // mDropTableIdeas.execute();
            // mDropTableUsers.execute();
            mDropTableFiles.execute();
            // mDropTableLink.executeQuery();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}