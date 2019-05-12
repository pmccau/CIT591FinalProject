import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class GraphTest {
	
	/**
	 * Test export
	 */
	@Test
	void testExportGraph() {
		String filepath = "C:\\Users\\pmccau\\eclipse-workspace\\CIT591FinalProject\\test\\testGraph.jpeg";	
		
		DataParser dp = new DataParser("School Performance - Out-of-School Suspensions");		
		HashMap<String, Double> map = dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7);
		Graph g = new Graph("More than three suspensions", dp.pivotDataBy("School_name", "School_more_than_three_Suspension", false, 7));
		
		g.generatePieChart();
		g.exportGraph(filepath);
		
		File f = new File(filepath);
		assertTrue(f.exists());
	}
	
	public static void main(String[] args) {
		
	}
}
