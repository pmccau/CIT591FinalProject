import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class AnalyzerTest {

	@Test
	void test() {
		Analyzer testAnalyzer = new Analyzer("District Employees and Finance - District Budget");
		
		assertEquals(testAnalyzer.getDataType("OPERATING_CYEST_LUMPSUM_AMT"), "double");
		
		String[] tempStrArr = {"FUNCTION_CLASS", "FUNCTION_CLASS_NAME", "FUNCTION_GROUP", "FUNCTION_GROUP_NAME", "FUNCTION", 
				"FUNCTION_NAME", "ACTIVITY_CODE", "ACTIVITY_NAME", "RUN_DATE"};
		ArrayList<String> temp = new ArrayList<>(Arrays.asList(tempStrArr));
		assertEquals(testAnalyzer.getGroupByFields(), temp);
	}

}
