import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class Visualizer {
	
	/**
	 * This will be the method that creates the bar chart using JFreeChart. It will take
	 * in the legend values and labels, as well as the values to be charted
	 * @param hLegend The horizontal legend values
	 * @param vLegend The vertical legend values
	 * @param hLabel The name of the horizontal axis
	 * @param vLabel The name of the vertical axis
	 * @param values The values to be charted
	 * @return A JPanel containing the chart for display
	 */
	public JPanel createBarChart(String hLegend, String vLegend, ArrayList<String> hLabel, 
			ArrayList<String> vLabel, ArrayList<Double> values) {
		return new JPanel();
	}
	
	/**
	 * This is the method that will create a bar chart. It will take in a hashmap of the
	 * categories and the values
	 * @param data The HashMap containing the labels and values of the data
	 * @return A JPanel containing the chart for display
	 */
	public JPanel createPieChart(HashMap<String, Double> data) {
		return new JPanel();
	}
	
}