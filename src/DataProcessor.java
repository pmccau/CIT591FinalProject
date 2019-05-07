import java.util.*;

public class DataProcessor {

	private HashMap<String, ArrayList<String>> fullDataSet;
	private HashMap<String, ArrayList<String>> relevantData; // Each school ID, points to list of data
	private ArrayList<String> SearchParameters;
	
	public DataProcessor (HashMap<String, ArrayList<String>> dataSetIn) {
		// CONFIRM WITH YU - Input will be HashMap with School IDs as key and full list as ArrayList???
		fullDataSet = dataSetIn;
		SearchParameters = new ArrayList<String>() ;
	}
	
	
	
	// INTERACTIONS: Input (searching) and Output (graphing, numerical analysis)
	
	/**
	 * Sets the values of 'relevantData' based on one search criteria
	 * 
	 * @param column
	 * @param comparison 
	 * @param Value 
	 * @param AndOr 
	 */	
	private void Search(String column, String comparison, String Value, String AndOr) {
		SearchParameters.add(column);
		
		// Add schools to relevantData if they match the search criteria **************
		// Eliminate from relevantData if AND, adding from fullDataSet if OR
		
	}
	
	/**
	 * Uses search parameters to decide which graphs to create
	 * Creates those graphs, and returns them (ID of map type, and graph)
	 * 
	 */
	private HashMap<String, Graph> GetGraphs() {	
	
		// Use search parameters to decide **************
		return new HashMap<String, Graph>(); // ----- PLACEHOLDER VAR -----
	}
	
	/**
	 * Uses search parameters to decide which summary analysis to complete
	 * Completes and returns that analysis (ID of analysis, then value)
	 * 
	 */
	private HashMap<String, String> GetSummary() {	
	
		// Use search parameters to decide **************
		return new HashMap<String, String>(); // ----- PLACEHOLDER VAR -----
	}
	
	
	
	
	// BASIC ANALYSIS METHODS
	
	/**
	 * Finds the average of all values in relevantData for a column
	 * @param column
	 * @return
	 */
	private double average (int column) {
		double average = 0;
		int setCount = 0;
		for (String school : relevantData.keySet()) {
			average += Double.parseDouble(relevantData.get(school).get(column));
			setCount++;
		}
		return average / setCount;
	}
	
	/**
	 * Finds the average of all values in relevantData, by one category
	 * @param column
	 * @return
	 */
	private double average (int column, int categoryColumn, String categoryValue) {
		double average = 0;
		int setCount = 0;
		for (String School : relevantData.keySet()) {
			if (relevantData.get(School).get(categoryColumn).equals(categoryValue)) {
				average += Double.parseDouble(relevantData.get(School).get(column));
				setCount++;
			}
		}
		return average/setCount;
	}
	
	
	
	// GRAPH SETUP METHODS
	
	/**
	 * Returns a hashmap of each unique value in a column, and the corresponding percent
	 * @return
	 */
	public Graph PieChartInfo (String column) {
		int columnInt = Integer.parseInt(column);
		HashMap<String, Double> data = new HashMap<String, Double>();
		double count = 0;
		for (String School : relevantData.keySet()) {
			for (String category : data.keySet()) {
				if (relevantData.get(School).get(columnInt).equals(category)) {
					data.replace(category, data.get(category)+1);
				}
				else {
					data.put(category, 1.0);
				}
				count = count +1;
			}
		}
		for (String category : data.keySet()) {
			data.replace(category, data.get(category)/count);
		}
		Graph pieChart = new Graph ("Pie Chart", data);
		return pieChart;
	}
	
	/**
	 * Returns a hashmap of each unique value in a column, and the corresponding percent
	 * @return
	 */
	public Graph BarChartInfo (String X_column, String Y_column) {
		int XColumnInt = Integer.parseInt(X_column);
		int YColumnInt = Integer.parseInt(Y_column);
		
		// If not a value, enter as empty string: ""
		HashMap<String, Double> data = new HashMap<>(); // ----- PLACEHOLDER VAR -----
		// Create graph ***********
		Graph barChart = new Graph ("Bar Chart", data);
		return barChart;
	}
	
	/**
	 * Returns a hashmap of each unique value in a column, and the corresponding percent
	 * @return
	 */
	public Graph ScatterPlotInfo (String X_column, String Y_column) {
		int XColumnInt = Integer.parseInt(X_column);
		int YColumnInt = Integer.parseInt(Y_column);
		
		// If not a value, enter as empty string: ""

		// Create graph *************** 
		HashMap<String, Double> data = new HashMap<>(); // ----- PLACEHOLDER VAR -----
		
		Graph scatterPlot = new Graph ("Scatter Plot", data);
		return scatterPlot;
	}
	
}
