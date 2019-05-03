import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserInterface implements Runnable {
	
	private Analyzer analyzer = new Analyzer(null);
	private String dataFolder = "data/";
	private String selectedDataset;
	private JPanel slicerArea, fileSelection, topLevel, buttonArea;
	private JFrame frame = new JFrame("PhillyOpenData Analyzer");
	private String exportLocation = "";
	
	/**
	 * This will be the place where the file to be analyzed is selected.
	 * This should have a dropdown menu with the various data sets. Selection
	 * of one set should trigger a refresh of the slicer area. This will be contained 
	 * at the top of the topLevel panel
	 * @return
	 */
	public JPanel fileSelection(JPanel topLevel) {
		
		File folder = new File(dataFolder);
		File[] listOfDatasets = folder.listFiles();
		String[] datasets = new String[listOfDatasets.length];
		
		for (int i = 0; i < datasets.length; i++) {
			String temp = listOfDatasets[i].toString();
			temp = temp.substring(dataFolder.length(), temp.indexOf("."));
			datasets[i] = temp;
		}
		
		// This will set the selectedDataset value to the selected value in the
		// ComboBox
		JComboBox datasetSelection = new JComboBox(datasets);
		datasetSelection.setBackground(Color.WHITE);
		datasetSelection.setSelectedIndex(-1);
		
		JPanel gridPanel = new JPanel(new GridLayout(3, 1));
		gridPanel.add(new JPanel());
		
		// Add the panel
		JPanel panel = new JPanel(new FlowLayout());
		JLabel selectDatasetLabel = new JLabel("Select dataset:");
		selectDatasetLabel.setFont(new Font("SelectDataset", Font.BOLD, 24));
		panel.add(new JPanel().add(selectDatasetLabel));
		panel.add(datasetSelection);
		panel.add(new JButton("Fetch Datasets"));
		
		// Add the panel with the label to select fields
		gridPanel.add(panel);
		JLabel selectSlicersLabel = new JLabel("Select numerical fields to include");
		selectSlicersLabel.setFont(new Font("Slicers", Font.PLAIN, 24));;
		gridPanel.add(selectSlicersLabel);
		return gridPanel;
	}
	
	/**
	 * This method will take in the dataset that is being analyzed. From there,
	 * it will generate checkboxes / slicers on how to pivot the data. This will
	 * be contained in the middle of the topLevel panel. This will need to interact
	 * with the I/O methods in order to loop through and generate the possible fields.
	 * We could hardcode this, but it might get unwieldy
	 * @param dataSet The dataset being analyzed
	 * @return
	 */
	public JPanel slicerArea(JPanel topLevel) {
		ArrayList<String> fields = analyzer.getNumericalFields();
		
		// Create the panel that will house the checkboxes
		JPanel panel = new JPanel(new GridLayout((fields.size() / 3 + 1), 3));
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
		panel.setPreferredSize(new Dimension(fields.size() / 3 * 30, 200));
		panel.setBackground(Color.WHITE);
		
		// Add in the checkboxes
		for (String str : fields) {
			JCheckBox cb = new JCheckBox(str);
			cb.setPreferredSize(new Dimension(10, 25));
			cb.setBackground(Color.WHITE);
			panel.add(cb);
		}
		
		// This is the outside border that will give a small buffer to the panel
		JPanel outerPanel = new JPanel(new BorderLayout());
		outerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
		
		// Add the internal panel (panel) to the outerPanel (white border)
		outerPanel.add(panel);
		return outerPanel;
	}
	
	/**
	 * This method will be responsible for generating and housing the buttons:
	 *  - Export to CSV
	 *  - Visualize
	 *  - Clear selections
	 * It will need to interact with the visualization module and the I/O. Everything
	 * that is decided in the UI will also need to be passed to the analysis module
	 * @return
	 */
	public JPanel buttonArea() {
		// The outer panel to serve as a buffer/spacing device
		JPanel outerPanel = new JPanel(new GridLayout(3,1));
		outerPanel.add(new JPanel());
				
		// The inner panel to house the buttons
		JPanel panel = new JPanel(new GridLayout(1, 5));
		panel.add(new JPanel());
		
		// Create the buttons
		JButton visualizeButton = new JButton("Visualize");
		JButton exportButton = new JButton("Export");
		JButton clearButton = new JButton("Clear");
		
		visualizeButton.setFont(new Font("Font", Font.PLAIN, 24));
		exportButton.setFont(new Font("Font", Font.PLAIN, 24));
		clearButton.setFont(new Font("Font", Font.PLAIN, 24));
		
		// Add the buttons and a blank panel for spacing
		panel.add(exportButton);
		panel.add(visualizeButton);
		panel.add(clearButton);
		panel.add(new JPanel());
		
		// Add the inner panel (panel) to the outerpanel
		outerPanel.add(panel);
		outerPanel.add(new JPanel());
		return outerPanel;
	}
	
	/**
	 * This will be the top-level container for the GUI. 
	 * This panel will have multiple sub-panels and buttons embedded
	 * within it, but will serve as the main housing for all those
	 * that will drive functionality
	 * @return
	 */
	public JPanel topLevel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		
		// Could store the buttons/event listeners in a HashMap. 
		HashMap<String, EventListener> slicers = new HashMap<>();
		
		slicerArea = slicerArea(panel);
		fileSelection = fileSelection(panel);
		buttonArea = buttonArea();
		
		//----------------------------------------------------------------------------------------
		//                                   EVENT LISTENERS SECTION
		
		// Add an event listener to the comboBox so that selection values appear when dataset chosen
		JComboBox selectionBox = (JComboBox) ((JPanel) fileSelection.getComponent(1)).getComponent(1);
		selectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				selectedDataset = (String) cb.getSelectedItem(); 
				analyzer = new Analyzer(selectedDataset);
				slicerArea = slicerArea(panel);
				panel.remove(1);
				panel.add(slicerArea, 1);
				panel.revalidate();
				panel.repaint();
			}
		});
		
		// Fetch the datasets if they're not already populated
		JButton fetchDatasets = (JButton) ((JPanel) fileSelection.getComponent(1)).getComponent(2);
		fetchDatasets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String, String> datasets = DownloadFile.getDatasets();
				for (String str : datasets.keySet()) {
					DownloadFile df = new DownloadFile(datasets.get(str), new File("data\\" + str + ".csv"));
					df.saveFile();
				}
				fileSelection = fileSelection(panel);
				
				// The listener for the selection box needs to be re-added
				JComboBox sb = (JComboBox) ((JPanel) fileSelection.getComponent(1)).getComponent(1);
				sb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox cb = (JComboBox) e.getSource();
						selectedDataset = (String) cb.getSelectedItem(); 
						analyzer = new Analyzer(selectedDataset);
						slicerArea = slicerArea(panel);
						panel.remove(1);
						panel.add(slicerArea, 1);
						panel.revalidate();
						panel.repaint();
					}
				});
				
				panel.remove(0);
				panel.add(fileSelection, 0);
				panel.revalidate();
				panel.repaint();
			}
		});
		
		// This is the event listener for the "Clear" button - it doesn't need to do much work,
		// can out source it to the listener on the ComboBox itself as setting that to blank will
		// clear the slicer values
		JButton clearButton = (JButton) ((JPanel) buttonArea.getComponent(1)).getComponent(3);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectionBox.setSelectedIndex(-1);
			}
		});
		
		// This is the event listener for the "Export" button. It will trigger a save dialog box,
		// then export a csv to that location
		JButton exportButton = (JButton) ((JPanel) buttonArea.getComponent(1)).getComponent(1);
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename, directory;
				
				// This is the file chooser
				JFileChooser fc = new JFileChooser();
				int rVal = fc.showSaveDialog(exportButton);
			      if (rVal == JFileChooser.APPROVE_OPTION) {
			        filename = fc.getSelectedFile().getName();
			        directory = fc.getCurrentDirectory().toString();
			        exportLocation = directory.toString() + "\\" + filename.toString();
			      }
			      if (rVal == JFileChooser.CANCEL_OPTION) {
			    	  // Do nothing
			      }
			}
		});
						
		//                                   END OF EVENT LISTENERS   
		// ______________________________________________________________________________________
		
		// These are the actual panels that will be added
		panel.add(fileSelection);
		panel.add(slicerArea);
		panel.add(buttonArea);						
		panel.setPreferredSize(new Dimension(800, 500));
		return panel;
	};
	
	// Run method. This will be called to generate the GUI
	public void run() {
		topLevel = topLevel();
		frame.add(topLevel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Main method. Function TBD 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new UserInterface());
	}

}
