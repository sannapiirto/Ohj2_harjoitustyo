package kappalekirjasto.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import kappalekirjasto.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2018.07.26 08:55:17 // Generated by ComTest
 *
 */
public class KappaleTest {



  // Generated by ComTest BEGIN
  /** testRekisteroi157 */
  @Test
  public void testRekisteroi157() {    // Kappale: 157
    Kappale a = new Kappale(); 
    assertEquals("From: Kappale line: 159", 0, a.getID()); 
    a.rekisteroi(); 
    Kappale b = new Kappale(); 
    b.rekisteroi(); 
    a.rekisteroi(); 
    int n1 = a.getID(); 
    int n2 = b.getID(); 
    assertEquals("From: Kappale line: 166", n2 - 1, n1); 
  } // Generated by ComTest END
}