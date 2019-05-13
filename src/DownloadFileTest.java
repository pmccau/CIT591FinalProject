import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class DownloadFileTest {

	@Test
	void testSaveFile() {
		HashMap<String, String> downloadUrls = DownloadFile.getDatasets();
		String tempUrl = "";
		
		for (String str : downloadUrls.keySet()) {
			if (tempUrl.equals("")) {
				tempUrl = str;
			} else {
				break;
			}
		}
		File f = new File("test/" + tempUrl + ".csv");
		
		if (f.exists()) {
			f.delete();
		}		
		
		DownloadFile df = new DownloadFile(downloadUrls.get(tempUrl), f);
		df.saveFile();
		
		assertTrue(f.exists());
	}

}
