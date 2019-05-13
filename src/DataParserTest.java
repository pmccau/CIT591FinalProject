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
		schools.add("KING;MARTIN LUTHER HIGH SCH.");
		schools.add("ROOSEVELT ELEMENTARY SCHOOL");
		schools.add("LINCOLN;ABRAHAM HIGH SCHOOL");
		schools.add("RHOADS;JAMES SCHOOL");
		schools.add("MEADE;GEN. GEORGE G. SCHOOL");
		schools.add("STRAWBERRY MANSION HIGH SCHOOL");
		boolean contained = true;
		
		HashMap<String, Double> map = dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7);
		
		for (String str : schools) {
			if (!map.containsKey(str)) {
				contained = false;
			}
		}
		assertTrue(contained);
	}
	
	/**
	 * Check to make sure that the number of total suspensions matches
	 */
	@Test
	void testSuspensionsBySchoolTwoTime() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		ArrayList<String> schools = new ArrayList<>();
		schools.add("NORTHEAST HIGH SCHOOL");
		schools.add("LINCOLN;ABRAHAM HIGH SCHOOL");
		schools.add("KING;MARTIN LUTHER HIGH SCH.");
		schools.add("ROOSEVELT ELEMENTARY SCHOOL");
		schools.add("OVERBROOK HIGH SCHOOL");
		schools.add("HARDING;WARREN G. MIDDLE SCH");
		schools.add("FRANKLIN;BENJAMIN HIGH SCHOOL");
		boolean contained = true;
		
		HashMap<String, Double> map = dp.pivotDataBy("School_name", "School_two_time_Suspension", false, 7);
		
		for (String str : schools) {
			if (!map.containsKey(str)) {
				contained = false;
			}
		}
		assertTrue(contained);
	}
	
	/**
	 * Check to make sure that the All Others (Avg.) field is coming through
	 */
	@Test
	void testSuspensionsAllOthers() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		ArrayList<String> schools = new ArrayList<>();
		HashMap<String, Double> map = dp.pivotDataBy("School_name", "School_two_time_Suspension", false, 7);
				
		assertTrue(map.containsKey("All Others (Avg.)"));
	}
	
	/**
	 * Check to make sure that the All Others (Avg.) field does not come through when below
	 * threshold
	 */
	@Test
	void testBudgetRunDate() {
		DataParser dp = new DataParser("District Employees and Finance - District Budget");
		ArrayList<String> schools = new ArrayList<>();
		HashMap<String, Double> map = dp.pivotDataBy("RUN_DATE", "GRANT_Year_Estimate_LumpSum_Amount", false, 7);
				
		assertTrue(!map.containsKey("All Others (Avg.)"));
	}
	
	/**
	 * Test to see data type of one time suspension (double)
	 */
	@Test
	void testDataTypesDoubleOneTimeSuspend() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		assertEquals(dp.getDataType("School_one_time_Suspension"), "double");
	}
	
	/**
	 * Test to see the data type of school_year
	 */
	@Test
	void testDataTypesStringSchoolYear() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		assertEquals(dp.getDataType("School_year"), "String");
	}
	
	/**
	 * Test to see the data type of Function_Class
	 */
	@Test
	void testDataTypesStringFunctionClass() {
		DataParser dp = new DataParser("District Employees and Finance - District Budget");
		assertEquals(dp.getDataType("FUNCTION_CLASS"), "String");
	}
	
	/**
	 * Edge case where the numerical column has some text fields - should be reclassed as String
	 */
	@Test
	void testActivityCode() {
		DataParser dp = new DataParser("District Employees and Finance - District Budget");
		assertEquals(dp.getDataType("Active_CODE"), "String");
	}
	
	/**
	 * Test that the name translator works
	 */
	@Test
	void testNumFieldsEnglish() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		assertEquals(dp.numFieldsEnglish("SCH_CYEST_TOT"), "School_Year_Estimate_Total");
	}
		
	/**
	 * Additional test on the name translation
	 */
	@Test
	void testSuspensionEnglish() {
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");
		assertEquals(dp.numFieldsEnglish("school_more_than_three_susp"), "School_more_than_three_Suspension");
	}
	
	/**
	 * Spot checking some of the values in the graph
	 */
	@Test
	void testValueSpotCheckOne() {
		DataParser dp = new DataParser("District Employees and Finance - Full Time Employees");
		HashMap<String, Double> map = dp.pivotDataBy("FUNCTION_NAME", "Active_Full-Time_Employee_Total", false, 7);
		assertEquals(Math.round(map.get("Special Ed High Incidence")), 962);
	}
	
	/**
	 * Spot checking some of the values in the graph
	 */
	@Test
	void testValueSpotCheckTwo() {
		DataParser dp = new DataParser("District Employees and Finance - Full Time Employees");
		HashMap<String, Double> map = dp.pivotDataBy("FUNCTION_NAME", "Active_Full-Time_Employee_Total", false, 7);
		assertEquals(Math.round(map.get("Food Service")), 839);
	}
	
}
