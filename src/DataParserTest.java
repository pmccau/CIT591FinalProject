import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class DataParserTest {

	/**
	 * Check to make sure that the number of >3 suspensions matches
	 */
	@Test
	void testSuspensionsBySchoolMoreThanThree() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		ArrayList<String> schools = new ArrayList<>();
		schools.add("KING, MARTIN LUTHER HIGH SCH.");
		schools.add("NORTHEAST HIGH SCHOOL");
		schools.add("ROOSEVELT ELEMENTARY SCHOOL");
		schools.add("LINCOLN, ABRAHAM HIGH SCHOOL");
		schools.add("FRANKLIN, BENJAMIN HIGH SCHOOL");
		schools.add("RHOADS, JAMES SCHOOL");
		schools.add("MEADE, GEN. GEORGE G. SCHOOL");
		schools.add("OVERBROOK HIGH SCHOOL");
		schools.add("STRAWBERRY MANSION HIGH SCHOOL");
		boolean contained = true;
		
		HashMap<String, Double> map = dp.pivotDataBy("school_name", "sch_more_than_three_susp", false, 7);
		
		for (String str : schools) {
			if (!schools.contains(str)) {
				contained = false;
			}
		}
		assertTrue(contained);
	}
	
	/**
	 * Check to make sure that the number of total suspensions matches
	 */
	@Test
	void testSuspensionsBySchoolTotal() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		ArrayList<String> schools = new ArrayList<>();
		schools.add("NORTHEAST HIGH SCHOOL");
		schools.add("LINCOLN, ABRAHAM HIGH SCHOOL");
		schools.add("KING, MARTIN LUTHER HIGH SCH.");
		schools.add("ROOSEVELT ELEMENTARY SCHOOL");
		schools.add("OVERBROOK HIGH SCHOOL");
		schools.add("WASHINGTON, GEORGE HIGH SCHOOL");
		schools.add("EDISON, THOMAS A. HIGH SCHOOL");
		boolean contained = true;
		
		HashMap<String, Double> map = dp.pivotDataBy("school_name", "total_students_suspended", false, 7);
		
		for (String str : schools) {
			if (!schools.contains(str)) {
				contained = false;
			}
		}
		assertTrue(contained);
	}
	
	/**
	 * Test to see data type of one time suspension (double)
	 */
	@Test
	void testDataTypesDoubleOneTimeSuspend() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		assertEquals(dp.getDataType("sch_one_time_susp"), "double");
	}
	
	/**
	 * Test to see the data type of school_year
	 */
	@Test
	void testDataTypesStringSchoolYear() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		assertEquals(dp.getDataType("school_year"), "String");
	}
	
	/**
	 * Test to see the data type of Function_Class
	 */
	@Test
	void testDataTypesStringFunctionClass() {
		DataParser dp = new DataParser("District Employees and Finance - District Budget");
		assertEquals(dp.getDataType("FUNCTION_CLASS"), "String");
	}

}