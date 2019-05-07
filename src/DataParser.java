import java.io.*;
import java.util.*;

public class DataParser {
	
	private HashMap<String, String> recordTypes = new HashMap<>();
	private HashMap<Integer, HashMap<String, String>> records = new HashMap<>();	
	String dataFolder = "data/";
	
	/**
	 * Constructor for the DataParser class. This will go through and parse the data. It will then
	 * pass to the DataProcessor class to create a graph
	 * @param dataset The dataset to be analyzed. Must be stored in the 'data' folder
	 * and be a .csv file
	 * @param recordValues
	 */
	public DataParser(String dataset) {
		
		if (dataset != null) {
			try {
				Scanner in = new Scanner(new File(dataFolder + dataset + ".csv"));
				String[] recordKeys = null;
				String[] recordValues = null;
				
				// Quick check to make sure there's data...
				if (in.hasNextLine()) {
					recordKeys = in.nextLine().split(",");
				} else {
					System.out.println("Did not find any lines");
					return;
				}
				
				// Build out the records data structure
				int recordPrimaryKey = 0;
				while (in.hasNextLine()) {
					String temp = in.nextLine().replaceAll(", ", ";");
					recordValues = temp.split(",");
					HashMap<String, String> tempRecord = new HashMap<>();
					for (int i = 0; i < Math.min(recordKeys.length, recordValues.length); i++) {
						
						// Add in the record types to the HashMap. All numbers will be cast to double
						// Everything else will be a String
						if (!recordTypes.containsKey(recordKeys[i])) {
							try {
								Double.parseDouble(recordValues[i]);
								recordTypes.put(recordKeys[i], "double");
							} catch (NumberFormatException e) {
								recordTypes.put(recordKeys[i], "String");
							}						
						} else {
							// This is here because there are some fields where the first record is numerical,
							// but not the whole column. They need to be caught and set to Strings
							if (recordTypes.get(recordKeys[i]).equals("double")) {
								try {
									Double.parseDouble(recordValues[i]);
								} catch (NumberFormatException e) {
									System.out.println("Caught an error at " + recordKeys[i] +"\tValue: " + recordValues[i]);
									recordTypes.put(recordKeys[i], "String");
								}	
							}	
						}						
						tempRecord.put(recordKeys[i], recordValues[i]);
					}
					records.put(recordPrimaryKey, tempRecord);
					recordPrimaryKey++;
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * This method will return the available fields that can be used to pivot.
	 * This will be simply those that are not numerical
	 * @return ArrayList containing the pivot fields
	 */
	public ArrayList<String> getGroupByFields() {
		ArrayList<String> output = new ArrayList<>();
		for (String str : recordTypes.keySet()) {
			if (getDataType(str).equals("String")) {
				output.add(str);
			}
		}
		return output;
	}
	
	/**
	 * This method will return all of the available numerical fields
	 * @return ArrayList containing the numerical fields
	 */
	public ArrayList<String> getNumericalFields() {
		ArrayList<String> output = new ArrayList<>();
		for (String str : recordTypes.keySet()) {
			if (getDataType(str).equals("double")) {
				output.add(str);
			}
		}
		return output;
	}
	
	/**
	 * This method will return the data type of a given record. All numerical
	 * records will be represented as doubles; all others as strings
	 * @param field The exact name of the field to be queried
	 * @return 'String' or 'Double', depending on the data type
	 */
	public String getDataType(String field) {
		return recordTypes.get(field);
	}
	
	/**
	 * Stub for the pivot by method. This will be where the data gets sliced up. This will
	 * be where most of the heavy lifting takes place in this class 
	 * @param fields The fields (in order) to be pivoted by
	 * @return TBD. Perhaps this could write directly to the output? Or pass whatever is needed
	 * to the visualizer
	 */
	public HashMap<String, Double> pivotDataBy(String field, String values, boolean showAsPercentage, int limitResults) {
		// Used to sum the data
		HashMap<String, Double> summedDataset = new HashMap<>();
		double total = 0;
				
		// Iterate over all records, summing the numerical field by the descriptive field
		for (int i : records.keySet()) {
			String recordDescriptor = records.get(i).get(field).trim();
			double recordValue = 0;
			if (records.get(i).get(values) != null) {
				try {
					recordValue = Double.parseDouble(records.get(i).get(values));
				} catch (NumberFormatException e) {
					// Do nothing
				}
			}
			total += recordValue; // Increase the total in case it is shown by percentage
			
			// If the record is already in the dataset, add it to what's there. Otherwise, create it
			if (summedDataset.containsKey(recordDescriptor)) {
				summedDataset.put(recordDescriptor, summedDataset.get(recordDescriptor) + recordValue);
			} else {
				summedDataset.put(recordDescriptor, recordValue);
			}			
		}
		
		// Convert to percentage if needed
		if (showAsPercentage) {
			for (String str : summedDataset.keySet()) {
				summedDataset.put(str, summedDataset.get(str) / total);
			}
		}
				
		
		// If the results should be limited to a specific number, this will kick in
		if (limitResults < summedDataset.size()) {
			double subTotal = 0;
			
			HashMap<String, Double> temp = new HashMap<>();
			
			// Convert to arrayList to sort
			ArrayList<Double> vals = new ArrayList<>();
			for (double d : summedDataset.values()) {
				vals.add(d);
			}
			
			Collections.sort(vals);; // Sort the values, pick the last one that's under threshold
			double cutoffVal = vals.get(vals.size() - (limitResults - 1));
			
			for (String str : summedDataset.keySet()) {
				if (summedDataset.get(str) >= cutoffVal && temp.size() < limitResults) {
					temp.put(str, summedDataset.get(str));
					subTotal += summedDataset.get(str);
				}
			}
			temp.put("Other", total - subTotal);
			return temp;
		}
		
		return summedDataset;
	}
		
	public static void main(String[] args) {
//		String[] columns = {"this", "is", "a", "column"};
//		String[] values = {"50.0", "40", "String val", "string"};
//		
		DataParser newRecord = new DataParser("District Employees and Finance - District Budget");
//		newRecord.pivotDataBy("ACTIVITY_NAME", "OPERATING_CYEST_LUMPSUM_AMT", true);
	}
}