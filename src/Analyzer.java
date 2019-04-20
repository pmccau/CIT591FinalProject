import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Analyzer {
	
	private static HashMap<String, String> recordTypes = new HashMap<>();
	private HashMap<String, String> records = new HashMap<>();	
	
	/**
	 * 
	 * @param recordKeys
	 * @param recordValues
	 */
	public Analyzer(String dataset) {
		
		try {
			Scanner in = new Scanner(new File("data\\" + dataset + ".csv"));
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
				recordValues = in.nextLine().split(",");
				for (int i = 0; i < Math.min(recordKeys.length, recordValues.length); i++) {
					
					// Add in the record types to the HashMap. All numbers will be cast to double
					if (!recordTypes.containsKey(recordKeys[i])) {
						try {
							Double.parseDouble(recordValues[i]);
							recordTypes.put(recordKeys[i], "double");
							continue;
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						recordTypes.put(recordKeys[i], "String");
					}
				}
				
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}	
	
	/**
	 * This will clear the recordTypes data structure (static structure)
	 */
	public static void flushRecordTypes() {
		recordTypes = new HashMap<>();
	}
	
	public static void main(String[] args) {
		String[] columns = {"this", "is", "a", "column"};
		String[] values = {"50.0", "40", "String val", "string"};
		
		Analyzer newRecord = new Analyzer("District Employees and Finance - District Budget");
	}
}