package dk.diku.pcsd.exam.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.exam.proxy.AccountManagerProxyImpl;
import dk.diku.pcsd.exam.proxy.AccountManagerProxyImplService;
import dk.diku.pcsd.exam.proxy.InexistentAccountException_Exception;
import dk.diku.pcsd.exam.proxy.InexistentBranchException_Exception;
import dk.diku.pcsd.exam.proxy.NegativeAmountException_Exception;

/**
 * Verifies that the service throws appropriate exceptions, and that part of the
 * service continues to work correctly when single partitions are down
 * (fail-soft).
 * 
 * @author marco
 * 
 */
public class ErrorTest {
	private static String secondTomcatPath = "/home/marco/tomcat2";

	private static AccountManagerProxyImpl am;

	@BeforeClass
	public static void initialize() {
		if (!secondTomcatPath.endsWith(File.separator))
			secondTomcatPath += File.separator;

		AccountManagerProxyImplService ams = new AccountManagerProxyImplService();
		am = ams.getAccountManagerProxyImplPort();
	}

	@Test
	public void exceptionTest() {
		try {
			am.calculateExposure(7000);
			fail();
		} catch (InexistentBranchException_Exception e) {
		}

		try {
			am.credit(1, 2, -20.0);
			fail();
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (InexistentBranchException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
		}

		try {
			am.debit(300, 2, 42.0);
			fail();
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (InexistentBranchException_Exception e) {
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.credit(2, 200, 20.1);
			fail();
		} catch (InexistentAccountException_Exception e) {
		} catch (InexistentBranchException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.transfer(3, 3, 201, -33.0);
			fail();
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (InexistentBranchException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
		}

		try {
			am.transfer(3, 22, 3, 21.0);
			fail();
		} catch (InexistentAccountException_Exception e) {
		} catch (InexistentBranchException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.transfer(3, 2, 31, 21.0);
			fail();
		} catch (InexistentAccountException_Exception e) {
		} catch (InexistentBranchException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

	}

	@Test
	public void failSoftTest() {
		// make sure all branches are initially online
		try {
			am.calculateExposure(1);
			am.calculateExposure(2);
			am.calculateExposure(3);
			am.calculateExposure(4);
		} catch (InexistentBranchException_Exception e) {
			fail();
		}

		stopServer();

		// check fail soft for calculateExposure

		int successFulRequests = 0;
		int correctExceptions = 0;

		try {
			am.calculateExposure(1);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		}

		try {
			am.calculateExposure(2);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		}

		try {
			am.calculateExposure(3);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		}

		try {
			am.calculateExposure(4);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		}

		assertTrue(correctExceptions > 0);
		assertTrue(successFulRequests > 0);
		assertTrue(successFulRequests + correctExceptions == 4);

		// same with credit
		successFulRequests = 0;
		correctExceptions = 0;
		try {
			am.credit(1, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.credit(2, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.credit(3, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.credit(4, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		assertTrue(correctExceptions > 0);
		assertTrue(successFulRequests > 0);
		assertTrue(successFulRequests + correctExceptions == 4);

		// same with debit

		successFulRequests = 0;
		correctExceptions = 0;
		try {
			am.debit(1, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.debit(2, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.debit(3, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.debit(4, 1, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		assertTrue(correctExceptions > 0);
		assertTrue(successFulRequests > 0);
		assertTrue(successFulRequests + correctExceptions == 4);

		// same with transfer

		successFulRequests = 0;
		correctExceptions = 0;
		try {
			am.transfer(1, 1, 2, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.transfer(2, 1, 2, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.transfer(3, 1, 2, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		try {
			am.transfer(4, 1, 2, 90.0);
			successFulRequests++;
		} catch (InexistentBranchException_Exception e) {
			correctExceptions++;
		} catch (InexistentAccountException_Exception e) {
			fail();
		} catch (NegativeAmountException_Exception e) {
			fail();
		}

		assertTrue(correctExceptions > 0);
		assertTrue(successFulRequests > 0);
		assertTrue(successFulRequests + correctExceptions == 4);
	}

	private void stopServer() {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(
					secondTomcatPath + "bin/shutdown.sh");
			waitForProc(proc);
			Thread.sleep(2000);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}

	}

	private static void waitForProc(Process p) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		try {
			while ((reader.readLine()) != null) {
			}
			p.waitFor();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}

	}
}
