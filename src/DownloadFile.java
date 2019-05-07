import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class DownloadFile {

	private String link;
	private File outputFile;
	
	/**
	 * Static method that will return a hashmap of datasets in the format
	 * Name => URL for use in fetching the datasets to be analyzed.
	 * @return
	 */
	public static HashMap<String, String> getDatasets() {
		HashMap<String, String> datasets = new HashMap<>();
		
		// Add in the URLs for use in the 
		datasets.put("District Employees and Finance - Full Time Employees", 
				"https://cdn.philasd.org/offices/performance/Open_Data/Budget_Staff/FTE/fte_information.csv");
		datasets.put("District Employees and Finance - District Budget", 
				"https://cdn.philasd.org/offices/performance/Open_Data/Budget_Staff/District_Budget/budget_information.csv");
		datasets.put("School Performance - Out-of-School Suspensions", 
				"https://cdn.philasd.org/offices/performance/Open_Data/School_Performance/Suspensions/School%20Profiles%20Suspensions%202016-2017.csv");
		datasets.put("School Information - Pre-School Information", 
				"https://cdn.philasd.org/offices/performance/Open_Data/School_Information/Pre_K/PREK_INFORMATION_2013_2014.csv");
		
		return datasets;
	}

	
	/**
	 * Constructor. Will take two parameters: first parameter would be the URL link to data source/file.
	 * Second parameter would be the name and location where we will save the data/file.
	 * @param link
	 * @param outputFile
	 */
	public DownloadFile(String link, File outputFile) {
		this.link = link;
		this.outputFile = outputFile;
	}
	
	
	/**
	 * This method will read data from specified URL and save the file locally
	 */
	public void saveFile() {
		if (outputFile.exists()) {
			return;
		}
		try {
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			FileOutputStream fos = new FileOutputStream(this.outputFile);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] buffer = new byte[1024];
			int read = 0;

			while ((read = in.read(buffer, 0, 1024)) >= 0) {
				bout.write(buffer, 0, read);
			}
			bout.close();
			in.close();

		} catch (IOException e) {
			System.out.println("Something is wrong with the URL.");
		}

	}

	
//	public static void main(String[] args) {
//		String link = "http://data.phl.opendata.arcgis.com/datasets/d46a7e59e2c246c891fbee778759717e_0.csv";
//		File outputFile = new File("data/downloadFile.txt"); // Will figure later how to save it to the "data" folder
//		
//		DownloadFile df = new DownloadFile(link, outputFile);
//		df.saveFile();
//	}
}
