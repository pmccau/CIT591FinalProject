import java.io.*;
import java.util.*;

public class Analyzer {
	
	private HashMap<String, String> recordTypes = new HashMap<>();
	private HashMap<String, String> records = new HashMap<>();	
	String dataFolder = "data/";
	
	/**
	 * Constructor for the Analyzer class.
	 * @param dataset The dataset to be analyzed. Must be stored in the 'data' folder
	 * and be a .csv file
	 * @param recordValues
	 */
	public Analyzer(String dataset) {
		
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
				while (in.hasNextLine()) {
					String temp = in.nextLine().replaceAll(", ", ";");
					recordValues = temp.split(",");
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
						records.put(recordKeys[i], recordValues[i]);
					}				
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
	public double pivotDataBy(ArrayList<String> fields) {
		return 0.0;
	}
		
	public static void main(String[] args) {
		String[] columns = {"this", "is", "a", "column"};
		String[] values = {"50.0", "40", "String val", "string"};
		
		Analyzer newRecord = new Analyzer("District Employees and Finance - District Budget");
		System.out.println(newRecord.getGroupByFields());
	}
}