package de.tu_darmstadt.gdi1.gorillas.test.students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.SunAstonishedTest;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.ThrowTestWind;

public class GorillasTestsuiteExtended2 {

	public static Test suite() {

		TestSuite suite = new TestSuite("Tutor tests for Gorillas - Extended 2");
		suite.addTest(new JUnit4TestAdapter(ThrowTestWind.class));
		suite.addTest(new JUnit4TestAdapter(SunAstonishedTest.class));
		return suite;
	}
}
