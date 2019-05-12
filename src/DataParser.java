import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class DataParser {
	
	private HashMap<String, String> recordTypes = new HashMap<>();
	private HashMap<Integer, HashMap<String, String>> records = new HashMap<>();	
	String dataFolder = "data/";
	public static HashMap<Pattern, String> regexPatterns = new HashMap<Pattern, String>();

	/**
	 * Constructof for the class. This takes in a dataset to be used in analysis/graphing
	 * @param dataset The name of the dataset, not file location
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
		populateRegexPatterns();
		for (String str : recordTypes.keySet()) {
			if (getDataType(str).equals("double")) {
				output.add(str); 
				// output.add(numFieldsEnglish(str)); 
			}
		}
		return output;
	}
	
	/**
	 * This method populates the Regex Patterns hashmap with code interpretations
	 * @param name
	 * @return
	 */
	public void populateRegexPatterns () {
		Pattern term = Pattern.compile(".*sch.*");
		regexPatterns.put(term, "School");
		term = Pattern.compile(".*SCH.*");
		regexPatterns.put(term, "School");
		term = Pattern.compile(".*CYEST.*");
		regexPatterns.put(term, "Year_Estimate");
		term = Pattern.compile(".*AMT.*");
		regexPatterns.put(term, "Amount");
		term = Pattern.compile(".*TOT.*");
		regexPatterns.put(term, "Total");
		term = Pattern.compile(".*ACT.*");
		regexPatterns.put(term, "Active");
		term = Pattern.compile(".*FTE.*");
		regexPatterns.put(term, "Full-Time_Employee");
		term = Pattern.compile(".*LUMPSUM.*");
		regexPatterns.put(term, "LumpSum");
		term = Pattern.compile(".*susp.*");
		regexPatterns.put(term, "Suspension");
	}
	
	
	/**
	 * This method will take a numerical field's name and return the interpreted, English terms
	 * @param name
	 * @return
	 */
	public String numFieldsEnglish (String name) {
		String[] str = name.split("_");
		Matcher m;
		String toReturn = "";
		for (int i=0; i<str.length; i++) {
			for (Pattern ptn: regexPatterns.keySet()) {
				m = ptn.matcher(str[i]);
				if (m.find()) {
					str[i] = regexPatterns.get(ptn);
				}
				
			}
			toReturn = toReturn + str[i];
			if (i != str.length-1) {
				toReturn = toReturn + "_";
			}
		}
		return toReturn;
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
	 * This method generates a HashMap for use in a Graph object.
	 * @param field The field by which the data will be organized. ex: School Name
	 * @param values The value field by which the data will be summed
	 * @param showAsPercentage Whether the data should be shown as a percentage
	 * @param limitResults How many individual results should show before being grouped into 'Other'
	 * @return A HashMap<String, Double> that will contain Key, Value for categories
	 */
	public HashMap<String, Double> pivotDataBy(String field, String values,
											   boolean showAsPercentage, int limitResults) {
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
		
		// Remove zero values
		HashMap<String, Double> tempHash = new HashMap<>();
		tempHash = (HashMap<String, Double>) summedDataset.clone();
		for (String str : tempHash.keySet()) {
			if (summedDataset.get(str) <= 0) {
				summedDataset.remove(str);
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
			
			Collections.sort(vals, Collections.reverseOrder()); // Sort the values, pick the last one that's under threshold
			double cutoffVal = vals.get(limitResults - 1);
			
			for (String str : summedDataset.keySet()) {
				if (summedDataset.get(str) >= cutoffVal) {
					temp.put(str, summedDataset.get(str));
					subTotal += summedDataset.get(str);
				}
			}
			temp.put("All Others (Avg.)", (total - subTotal) / (summedDataset.size() - temp.size()));
			return temp;
		}
		
		return summedDataset;
	}
}
