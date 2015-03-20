package de.tu_darmstadt.gdi1.gorillas.test.students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.ThrowTestGravity;

public class GorillasTestsuiteExtended3 {

	public static Test suite() {

		TestSuite suite = new TestSuite("Student tests for Gorillas - Extended 3");
		suite.addTest(new JUnit4TestAdapter(ThrowTestGravity.class));
		return suite;
	}
}
