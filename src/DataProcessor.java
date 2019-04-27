import java.util.*;

public class DataProcessor {

	private HashMap<String, ArrayList<String>> fullDataSet;
	private HashMap<String, ArrayList<String>> relevantData;
	
	public DataProcessor (HashMap<String, ArrayList<String>> dataSetIn) {
		fullDataSet = dataSetIn;
	}
	
	
	
	// SEARCHING METHODS
	
	/**
	 * Sets the values of 'relevantData' based on one search criteria
	 * 
	 * @param column
	 * @param exactValue
	 */
	private void SearchParameter(String column, String comparison, String Value, String AndOr) {
		//Add values to fullDataSet if they match the search criteria
	
	
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
		for (String School : relevantData.keySet()) {
			average += Double.parseDouble(relevantData.get(School).get(column));
			setCount++;
		}
		return average/setCount;
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
		
	}
	
	/**
	 * Returns a hashmap of each unique value in a column, and the corresponding percent
	 * @return
	 */
	public Graph BarChartInfo (String X_column, String Y_column) {
		
	}
	
}
