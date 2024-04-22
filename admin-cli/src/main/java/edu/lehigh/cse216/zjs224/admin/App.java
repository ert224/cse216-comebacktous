package edu.lehigh.cse216.zjs224.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serial;
import java.io.IOException;
import java.util.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import java.util.ArrayList;
import java.util.Map;

import com.google.api.client.util.Lists;
import com.google.api.gax.paging.Page;

import javax.xml.stream.events.Comment;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create ideas table");
        System.out.println("  [D] Drop ideas table");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Invalidate an Item");
        System.out.println("  [V] View Content");
        System.out.println("  [L] Delete least used Content");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~VLq?";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    ArrayList<String> leastUsed = new ArrayList<>();

    static char promptTable(BufferedReader in) {
        // The valid actions:
        String actions = "civuf";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    static char promptTable2(BufferedReader in) {
        // The valid actions:
        String actions = "iu";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String idea
     * 
     * @param in   A BufferedReader, for reading from the keyboard
     * @param idea A idea to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String idea) {
        String s;
        try {
            System.out.print(idea + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in   A BufferedReader, for reading from the keyboard
     * @param idea A idea to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String idea) {
        int i = -1;
        try {
            System.out.print(idea + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options. Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        // Create an un-configured Database object
        String db_url = "postgres://nrhaggnqicsbno:1ffebc514f79d9ba02032a2e174a8f37972c793cb2f9dbc856eba4f9a88d5630@ec2-3-220-207-90.compute-1.amazonaws.com:5432/dam4fg1iidcoa9";
        String projectID = "speedy-vim-368820";
        String bucketName = "comebacktous";
        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            // function call
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                if (db.createTable() == -1) {
                    System.out.println("Error building tables.");
                }
            } else if (action == 'V') {
                listObjects(projectID, bucketName);
            } else if (action == 'D') {
                if (db.dropTable() == -1) {
                    System.out.println("Error dropping tables.");
                }
            } else if (action == 'L') {
                deleteObject(projectID, bucketName);
            } else if (action == '1') {
                System.out.println("Enter the table where the row will be added");
                System.out.println("Enter 'i' for ideas");
                System.out.println("Enter 'u' for users");
                System.out.println("Enter 'c' for comments");
                System.out.println("Enter 'v' for votes");
                System.out.println("Enter 'f' for Files");
                char table = promptTable(in);
                if (table == 'i') {
                    selectIdea(in, db);
                } else if (table == 'u') {
                    selectUser(in, db);
                } else if (table == 'c') {
                    selectComment(in, db);
                } else if (table == 'v') {
                    selectVote(in, db);
                } else if (table == 'f') {
                    selectFile(in, db);
                }

            } else if (action == '*') {
                System.out.println("Enter the table where the row will be added");
                System.out.println("Enter 'i' for ideas");
                System.out.println("Enter 'u' for users");
                System.out.println("Enter 'c' for comments");
                System.out.println("Enter 'v' for votes");
                System.out.println("Enter 'f' for Files");
                char table = promptTable(in);
                if (table == 'i') {
                    selectIdeas(in, db);
                } else if (table == 'u') {
                    selectUsers(in, db);
                } else if (table == 'c') {
                    selectComments(in, db);
                } else if (table == 'v') {
                    selectVotes(in, db);
                } else if (table == 'f') {
                    selectFile(in, db);
                }

            } else if (action == '-') {
                System.out.println("Enter the table where the row will be added");
                System.out.println("Enter 'i' for ideas");
                System.out.println("Enter 'u' for users");
                System.out.println("Enter 'c' for comments");
                System.out.println("Enter 'v' for votes");
                System.out.println("Enter 'f' for Files");
                char table = promptTable(in);
                if (table == 'i') {
                    deleteIdea(in, db);
                } else if (table == 'u') {
                    deleteUser(in, db);
                } else if (table == 'c') {
                    deleteComment(in, db);
                } else if (table == 'v') {
                    deleteVote(in, db);
                } else if (table == 'f') {
                    deleteFile(in, db);
                }

            } else if (action == '+') {
                System.out.println("Enter the table where the row will be added");
                System.out.println("Enter 'i' for ideas");
                System.out.println("Enter 'u' for users");
                System.out.println("Enter 'c' for comments");
                System.out.println("Enter 'v' for votes");
                System.out.println("Enter 'f' for Files");
                char table = promptTable(in);
                if (table == 'i') {
                    int res = insertIdea(in, db);
                    if (res == -1)
                        continue;
                } else if (table == 'u') {
                    int res = insertUser(in, db);
                    if (res == -1)
                        continue;
                } else if (table == 'c') {
                    int res = insertComment(in, db);
                    if (res == -1) {
                        continue;
                    }
                } else if (table == 'v') {
                    int res = insertVote(in, db);
                    if (res == -1) {
                        continue;
                    }
                } else if (table == 'f') {
                    int res = insertFile(in, db);
                    if (res == -1) {
                        continue;
                    }

                }

            } else if (action == '~') {
                System.out.println("Enter the table where the row will be added");
                System.out.println("Enter 'i' for ideas");
                System.out.println("Enter 'u' for users");
                char table = promptTable2(in);
                if (table == 'i') {
                    int res = invalidateIdea(in, db);
                } else if (table == 'u') {
                    int res = invalidateUser(in, db);
                }
            }
        }
        // Always remember to disconnect from the database when the program
        // exits
        db.disconnect();
    }

    public static int insertIdea(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        String title = getString(in, "Enter the title");
        String idea = getString(in, "Enter the idea");

        int res = db.insertRowIntoIdeas(user_id, title, idea);
        System.out.println(res + " rows added");
        return res;
    }

    public static int insertFile(BufferedReader in, Database db) {
        int id = getInt(in, "Enter the associated idea id");
        int comment_id = getInt(in, "Enter the comment_id");
        String file_name = getString(in, "Enter the file_name");
        String url = getString(in, "Enter the url");
        String display_text = getString(in, "Enter the diplay text");

        int res = db.insertRowIntoFiles(file_name, id, comment_id, url, display_text);
        System.out.println(res + " rows added");
        return res;
    }

    public static int insertUser(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the user_id");
        String username = getString(in, "Enter the username");
        String email = getString(in, "Enter the email");
        String first_name = getString(in, "Enter your first name");
        String last_name = getString(in, "Enter your last name");
        String gender = getString(in, "Enter preferred gender identity");
        String orientation = getString(in, "Enter preferred sexual orientation");
        String notes = getString(in, "Enter preferred additional note");

        int res = db.insertRowIntoUsers(user_id, username, email, first_name, last_name, gender, orientation, notes);
        System.out.println(res + " rows added");
        return res;
    }

    public static int insertComment(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        String comment = getString(in, "Enter the comment");

        int res = db.insertRowIntoComments(id, user_id, comment);
        System.out.println(res + " rows added");
        return res;
    }

    public static int insertVote(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        int vote = getInt(in, "Enter the vote");

        int res = db.insertRowIntoVotes(id, user_id, vote);
        System.out.println(res + " rows added");
        return res;
    }

    public static int selectIdea(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");

        Database.IdeaRowData row = db.selectOneFromIdeas(id, user_id);
        if (row != null) {
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", "Post_Id", "User_Id", "Title", "Idea", "Valid");
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", row.mId, row.mUserID, row.mtitle, row.midea, row.mValid);
            return 0;
        } else {
            System.out.println("Idea not found.");
            return -1;
        }
    }

    public static int selectFile(BufferedReader in, Database db) {
        int comment_id = getInt(in, "Enter the associated ccomment id");
        int id = getInt(in, "Enter the associated idea id");

        Database.FileRowData row = db.selectoneFromFiles(comment_id, id);
        if (row != null) {
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", "comment_id", "id", "comment_id", "url", "display_text");
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", row.mFile_Name, row.mID, row.mComment_ID, row.mUrl,
                    row.mDisplay_text);
            return 0;
        } else {
            System.out.println("Idea not found.");
            return -1;
        }
    }

    public static int selectUser(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        Database.UserRowData row = db.selectOneFromUsers(user_id);
        if (row != null) {
            System.out.printf("%-10s%-30s%-30s%-30s%-30s\n", "User_Id", "Username", "Email", "First Name", "Last Name");
            System.out.printf("%-10s%-30s%-30s%-30s%-30s\n", row.mUserID, row.mUsername, row.mEmail, row.mFirstName,
                    row.mLastName);
            System.out.printf("%-30s%-30s%-30s%-30s\n", "Gender Preference", "Orientation", "Note", "Valid");
            System.out.printf("%-30s%-30s%-30s%-30s\n", row.mGenIden, row.mOrientation, row.mNote, row.mValid);
            return 0;
        } else {
            System.out.println("User not found");
            return -1;
        }
    }

    public static int selectComment(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        int comment_id = getInt(in, "Enter the associated comment id");

        Database.CommentRowData row = db.selectOneFromComments(id, user_id, comment_id);
        if (row != null) {
            System.out.printf("%-10s%-10s%-10s%-100s\n", "Comment_id", "User_Id", "Post ID", "Comment");
            System.out.printf("%-10s%-10s%-10s%-100s\n", row.mCommentId, row.mUserID, row.mId, row.mComment);
            return 0;
        } else {
            System.out.println("Comment not found");
            return -1;
        }
    }

    public static int selectVote(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        int vote_id = getInt(in, "Enter the associated vote id");

        Database.VoteRowData row = db.selectOneFromVotes(vote_id, id, user_id);
        if (row != null) {
            System.out.printf("%-10s%-10s%-10s%-100s\n", "Vote_id", "User_Id", "Post ID", "Votes");
            System.out.printf("%-10s%-10s%-10s%-100s\n", row.mVoteId, row.mUserID, row.mId, row.mVotes);
            return 0;
        } else {
            System.out.println("Vote not found");
            return 0;
        }
    }

    public static int selectIdeas(BufferedReader in, Database db) {

        ArrayList<Database.IdeaRowData> row = db.selectAllFromIdeas();
        System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", "Post_Id", "User_Id", "Title", "Idea", "Valid");
        for (int i = 0; i < row.size(); i++) {
            Database.IdeaRowData print = row.get(i);
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", print.mId, print.mUserID, print.mtitle, print.midea,
                    print.mValid);

        }

        return 0;
    }

    public static int selectUsers(BufferedReader in, Database db) {
        ArrayList<Database.UserRowData> print = db.selectAllFromUsers();
        for (int i = 0; i < print.size(); i++) {
            Database.UserRowData row = print.get(i);
            System.out.printf("%-50s%-30s%-30s%-30s%-30s\n", "User_Id", "Username", "Email", "First Name", "Last Name");
            System.out.printf("%-50s%-30s%-30s%-30s%-30s\n", row.mUserID, row.mUsername, row.mEmail, row.mFirstName,
                    row.mLastName);
            System.out.printf("%-30s%-30s%-30s%-30s\n", "Gender Preference", "Orientation", "Note", "Valid");
            System.out.printf("%-30s%-30s%-30s%-30s\n", row.mGenIden, row.mOrientation, row.mNote, row.mValid);
            System.out.println("");
        }
        return 0;
    }

    public static int selectFiles(BufferedReader in, Database db) {
        ArrayList<Database.FileRowData> print = db.selectAllFromFiles();
        for (int i = 0; i < print.size(); i++) {
            Database.FileRowData row = print.get(i);
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", "File_name", "id", "comment_id", "url", "display_text");
            System.out.printf("%-10s%-10s%-15s%-100s%-7s\n", row.mFile_Name, row.mID, row.mComment_ID, row.mUrl,
                    row.mDisplay_text);
            System.out.println("");
        }
        return 0;
    }

    public static int selectComments(BufferedReader in, Database db) {
        ArrayList<Database.CommentRowData> print = db.selectAllFromComments();
        System.out.printf("%-15s%-10s%-10s%-100s\n", "Comment_id", "User_Id", "Post ID", "Comment");
        for (int i = 0; i < print.size(); i++) {
            Database.CommentRowData row = print.get(i);
            System.out.printf("%-15s%-10s%-10s%-100s\n", row.mCommentId, row.mUserID, row.mId, row.mComment);
        }
        return 0;
    }

    public static int selectVotes(BufferedReader in, Database db) {
        ArrayList<Database.VoteRowData> print = db.selectAllFromVotes();
        System.out.printf("%-10s%-10s%-10s%-100s\n", "Vote_id", "User_Id", "Post ID", "Votes");
        for (int i = 0; i < print.size(); i++) {
            Database.VoteRowData row = print.get(i);
            System.out.printf("%-10s%-10s%-10s%-100s\n", row.mVoteId, row.mUserID, row.mId, row.mVotes);
        }
        return 0;
    }

    public static int deleteIdea(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");

        int res = db.deleteRowFromIdeas(user_id, id);
        System.out.println(res + " rows deleted");

        return 0;
    }

    public static int deleteFile(BufferedReader in, Database db) {
        int comment_id = getInt(in, "Enter the associated comment id");
        int id = getInt(in, "Enter the associated idea id");

        int res = db.deleteRowFromFiles(comment_id, id);
        System.out.println(res + " rows deleted");

        return 0;
    }

    public static int deleteUser(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");

        int res = db.deleteRowFromUsers(user_id);
        System.out.println(res + " rows deleted");
        return 0;
    }

    public static int deleteComment(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        int comment_id = getInt(in, "Enter the associated comment id");

        int res = db.deleteRowFromComments(id, user_id, comment_id);
        System.out.println(res + " rows deleted");
        return 0;
    }

    public static int deleteVote(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        int vote_id = getInt(in, "Enter the associated vote id");

        int res = db.deleteRowFromVotes(id, user_id, vote_id);
        System.out.println(res + " rows deleted");
        return 0;
    }

    public static int invalidateIdea(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        int id = getInt(in, "Enter the associated idea id");
        String validate = getString(in, "Enter v to validate or i to invalidate");
        int count = 0;
        if (validate.equals("v")) {
            count += db.invalidateRowFromIdeas(id, user_id, "v");
        } else {
            count += db.invalidateRowFromIdeas(id, user_id, "i");
        }
        System.out.println(count + " rows invalidated");
        return 0;
    }

    public static int invalidateUser(BufferedReader in, Database db) {
        String user_id = getString(in, "Enter the associated user id");
        String validate = getString(in, "Enter v to validate or i to invalidate");
        int count = 0;

        if (validate.equals("v")) {
            count += db.invalidateRowFromUsers(user_id, "v");
        } else {
            count += db.invalidateRowFromUsers(user_id, "i");
        }
        System.out.println(count + " rows invalidated");
        return 0;
    }

    // Find the list of all files created from the cloud
    // Using com.google.api

    public static void listObjects(String projectId, String bucketName) {
        int count = 1;
        // our projectID: speedy-vim-368820
        // our bucket name: comebacktous
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Page<Blob> blobs = storage.list(bucketName);

        for (Blob blob : blobs.iterateAll()) {
            System.out.println();
            System.out.println("File-" + count + ": " + blob.getName());
            getObjectMetadata(projectId, bucketName, blob.getName());
            count++;
        }
        System.out.println();
    }

    public static void getObjectMetadata(String projectId, String bucketName, String blobName)
            throws StorageException {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        // Select all fields
        // Fields can be selected individually e.g. Storage.BlobField.CACHE_CONTROL
        Blob blob = storage.get(bucketName, blobName, Storage.BlobGetOption.fields(Storage.BlobField.values()));

        // Print blob metadata
        // System.out.println("Bucket: " + blob.getBucket());
        // System.out.println("CacheControl: " + blob.getCacheControl());
        // System.out.println("ComponentCount: " + blob.getComponentCount());
        // System.out.println("ContentDisposition: " + blob.getContentDisposition());
        // System.out.println("ContentEncoding: " + blob.getContentEncoding());
        // System.out.println("ContentLanguage: " + blob.getContentLanguage());
        // System.out.println("ContentType: " + blob.getContentType());
        // System.out.println("CustomTime: " + blob.getCustomTime());
        // System.out.println("Crc32c: " + blob.getCrc32c());
        // System.out.println("Crc32cHexString: " + blob.getCrc32cToHexString());
        // System.out.println("ETag: " + blob.getEtag());
        // System.out.println("Generation: " + blob.getGeneration());
        // System.out.println("Id: " + blob.getBlobId());
        // System.out.println("KmsKeyName: " + blob.getKmsKeyName());
        // System.out.println("Md5Hash: " + blob.getMd5());
        // System.out.println("Md5HexString: " + blob.getMd5ToHexString());
        // System.out.println("MediaLink: " + blob.getMediaLink());
        // System.out.println("Metageneration: " + blob.getMetageneration());
        // System.out.println("Name: " + blob.getName());
        // System.out.println("Size: " + blob.getSize());
        // System.out.println("StorageClass: " + blob.getStorageClass());
        System.out.println("Created by: ebb224");
        System.out.println("TimeCreated: " + new Date(blob.getCreateTime()));
        System.out.println("Last Metadata Update: " + new Date(blob.getUpdateTime()));
        Boolean temporaryHoldIsEnabled = (blob.getTemporaryHold() != null && blob.getTemporaryHold());
        // System.out.println("temporaryHold: " + (temporaryHoldIsEnabled ? "enabled" :
        // "disabled"));
        Boolean eventBasedHoldIsEnabled = (blob.getEventBasedHold() != null && blob.getEventBasedHold());
        // System.out.println("eventBasedHold: " + (eventBasedHoldIsEnabled ? "enabled"
        // : "disabled"));
        // if (blob.getRetentionExpirationTime() != null) {
        // System.out.println("retentionExpirationTime: " + new
        // Date(blob.getRetentionExpirationTime()));
        // }
        // if (blob.getMetadata() != null) {
        // System.out.println("\n\n\nUser metadata:");
        // for (Map.Entry<String, String> userMetadata : blob.getMetadata().entrySet())
        // {
        // System.out.println(userMetadata.getKey() + "=" + userMetadata.getValue());
        // }
        // }
    }

    // deleting folder
    public static void deleteObject(String projectId, String bucketName) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";
        Storage storage1 = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Page<Blob> blobs1 = storage1.list(bucketName);
        blobs1.iterateAll();

        for (Blob blob1 : blobs1.iterateAll()) {
            String use = blob1.toString();

            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            Blob blob = storage.get(bucketName, blob1.getName());
            if (blob == null) {
                System.out.println("The object " + blob1.getName() + " wasn't found in " + bucketName);
                return;
            }

            // Optional: set a generation-match precondition to avoid potential race
            // conditions and data corruptions. The request to upload returns a 412 error if
            // the object's generation number does not match your precondition.
            Storage.BlobSourceOption precondition = Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            storage.delete(bucketName, blob1.getName(), precondition);

            System.out.println("Object " + blob1.getName() + " was deleted from " + bucketName);
            return;
        }
    }

}