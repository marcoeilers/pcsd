package dk.diku.pcsd.exam.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BasicFunctionalityTest.class, AtomicityTest.class, ErrorTest.class})
public class AllBlackboxTests {
}
