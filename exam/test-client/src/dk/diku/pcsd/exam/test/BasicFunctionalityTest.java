package dk.diku.pcsd.exam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.exam.proxy.AccountManagerProxyImpl;
import dk.diku.pcsd.exam.proxy.AccountManagerProxyImplService;
import dk.diku.pcsd.exam.proxy.InexistentBranchException_Exception;

/**
 * Verifies that all basic operations do what they should do.
 * @author marco
 *
 */
public class BasicFunctionalityTest {
	private static AccountManagerProxyImpl am;
	
	@BeforeClass
	public static void initialize(){
		AccountManagerProxyImplService ams = new AccountManagerProxyImplService();
		am = ams.getAccountManagerProxyImplPort();
	}
	
	@Test
	public void exposureTest(){
		try {
			assertEquals(100.0, am.calculateExposure(11), 0.01);
			assertEquals(200.0, am.calculateExposure(12), 0.01);
			assertEquals(400.0, am.calculateExposure(13), 0.01);
			assertEquals(0.0, am.calculateExposure(14), 0.01);
		} catch (InexistentBranchException_Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	
	@Test
	public void creditTest(){
		try {
			am.credit(1, 1, 50.0);
			assertEquals(50.0, am.calculateExposure(1), 0.01);
			
			am.credit(2, 1, 100.0);
			assertEquals(150.0, am.calculateExposure(2), 0.01);
			
			
			double current = am.calculateExposure(5);
			for (int i=0; i<10; i++){
				double amount = Math.round(Math.random()*400);
				am.credit(5, 1, amount);
				double newBal = am.calculateExposure(5);
				assertEquals(current-amount, newBal, 0.01);
				current = newBal;				
			}
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}
	
	@Test
	public void debitTest(){
		try {
			am.debit(4, 1, 90.0);
			assertEquals(50.0, am.calculateExposure(4), 0.01);
			
			am.debit(3, 2, 50.1);
			assertEquals(450.1, am.calculateExposure(3), 0.01);
			
			double current = am.calculateExposure(5);
			for (int i=0; i<10; i++){
				double amount = Math.round(Math.random()*400);
				am.debit(5, 1, amount);
				double newBal = am.calculateExposure(5);
				assertEquals(current+amount, newBal, 0.01);
				current = newBal;				
			}
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}
	
	@Test
	public void transferTest(){
		try {
			am.transfer(6, 1, 2, 1000.0);
			assertEquals(5000.0, am.calculateExposure(6), 0.01);
			
			am.transfer(7, 2, 1, 1000.0);
			assertEquals(7000.0, am.calculateExposure(7), 0.01);
			
			am.transfer(8, 1, 1, 1000.0);
			assertEquals(6000.0, am.calculateExposure(8), 0.01);
			
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}
	
	@Test
	public void combinationTest(){
		try {
			am.transfer(10, 2, 1, 40.0);
			assertEquals(140.0, am.calculateExposure(10), 0.01);
			
			am.credit(10, 3, 20.0);
			assertEquals(120.0, am.calculateExposure(10), 0.01);
			
			am.debit(10, 3, 40.0);
			assertEquals(160.0, am.calculateExposure(10), 0.01);
			
			am.transfer(10, 2, 1, 200.0);
			assertEquals(180.0, am.calculateExposure(10), 0.01);
			
			am.credit(10, 1, 300.0);
			assertEquals(180.0, am.calculateExposure(10), 0.01);
			
			am.debit(10, 3, 35.0);
			assertEquals(215.0, am.calculateExposure(10), 0.01);
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}
	
	
}
