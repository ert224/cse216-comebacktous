package edu.lehigh.cse216.comebacktous.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import java.lang.InterruptedException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

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
    String db_url =
      "postgres://nrhaggnqicsbno:1ffebc514f79d9ba02032a2e174a8f37972c793cb2f9dbc856eba4f9a88d5630@ec2-3-220-207-90.compute-1.amazonaws.com:5432/dam4fg1iidcoa9";
    final Database dataBase = Database.getDatabase(db_url);
    assert (dataBase.login("46129492", "test@gmail.com") != -1);
    assert (
      dataBase.insertUser(
        "1",
        "alreadyExists",
        "a@mail.com",
        "first",
        "last",
        "m",
        "s",
        "Cool!"
      ) !=
      1
    ); // user should already exist
    assert (dataBase.insertIdea("Title", "Good Idea", "1") == 1);
    assert (dataBase.insertComment("com", 8, "1") == 1);
    //assert (dataBase.updateLikes(8, "1", 1, 1) == 1);
    assert (dataBase.selectOneUser("1") != null);

    //assert(System.getenv("MEMCACHIER_MAUVE_SERVERS").equals("mc4.dev.ec2.memcachier.com:11211"));
    List<InetSocketAddress> servers = AddrUtil.getAddresses("924650.heroku.prod.memcachier.com:11211".replace(",", " "));
    assert(servers.isEmpty() == false);
    AuthInfo authInfo = AuthInfo.plain("594FBC", "9E4D5F172437DE508E4FFBE0FF936372");
    assert(authInfo != null);
    MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);

    // Configure SASL auth for each server
    for(InetSocketAddress server : servers) {
      builder.addAuthInfo(server, authInfo);
    }

    // Use binary protocol
    builder.setCommandFactory(new BinaryCommandFactory());
    // Connection timeout in milliseconds (default: )
    builder.setConnectTimeout(1000);
    // Reconnect to servers (default: true)
    builder.setEnableHealSession(false);
    String testShutdown = "";

    try {
      MemcachedClient mc = builder.build();
      try {
        mc.set("foo", 20, "bar");
        String val = mc.get("foo");
        System.out.println(val);
        assert(val.equals("bar"));
        mc.delete("foo");
      } catch (TimeoutException te) {
        System.err.println("Timeout during set or get: " +
                           te.getMessage());
      } catch (InterruptedException ie) {
        System.err.println("Interrupt during set or get: " +
                           ie.getMessage());
      } catch (MemcachedException me) {
        System.err.println("Memcached error during get or set: " +
                           me.getMessage());
      }
    } catch (IOException ioe) {
      testShutdown = ioe.getMessage();
    }
    assert(testShutdown.equals(""));
  }



}
