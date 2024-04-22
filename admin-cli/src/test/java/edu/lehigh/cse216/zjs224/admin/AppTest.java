package edu.lehigh.cse216.zjs224.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.sql.*;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        // Create an un-configured Database object
        String db_url = "postgres://nrhaggnqicsbno:1ffebc514f79d9ba02032a2e174a8f37972c793cb2f9dbc856eba4f9a88d5630@ec2-3-220-207-90.compute-1.amazonaws.com:5432/dam4fg1iidcoa9";

        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);

        

        assert(db.insertRowIntoUsers("user1", "username", "email", "first_name", "last_name", "gender", "orientation", "notes") == 1); 
        assert(db.insertRowIntoIdeas("user1", "title", "idea") == 1); 
        assert(db.insertRowIntoComments(1, "user1", "comment") == 1);
        assert(db.insertRowIntoVotes(1, "user1", 10) == 1); 

        assert(db.selectAllFromComments() != null); 
        assert(db.selectAllFromIdeas() != null); 
        assert(db.selectAllFromUsers() != null); 
        assert(db.selectAllFromVotes() != null); 


        
        



        
       
    }
}
