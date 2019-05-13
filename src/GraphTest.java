import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.Test;

class GraphTest {
	
	/**
	 * Test export of pie chart
	 */
	@Test
	void testExportGraphPie() {
		String filepath = "test/testGraphPie";	
		File f = new File(filepath + ".jpg");
		
		// Delete the file if it already exists
		if (f.exists()) {
			f.delete();
		}
		
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");		
		HashMap<String, Double> map = dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7);
		Graph g = new Graph("More than three suspensions", dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7));
		
		g.generatePieChart();
		g.exportGraph(filepath);
		
		f = new File(filepath + ".jpg");		
		assertTrue(f.exists());
	}
	
	/**
	 * Test export of bar chart
	 */
	@Test
	void testExportGraphBar() {
		String filepath = "test/testGraphBar";	
		File f = new File(filepath + ".jpg");
		
		// Delete the file if it already exists
		if (f.exists()) {
			f.delete();
		}
		
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");		
		HashMap<String, Double> map = dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7);
		Graph g = new Graph("More than three suspensions", dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7));
		
		g.generatePieChart();
		g.exportGraph(filepath);
		
		f = new File(filepath + ".jpg");		
		assertTrue(f.exists());
	}
}