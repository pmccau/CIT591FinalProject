import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventListener;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class UserInterface implements Runnable {
	
	Analyzer analyzer = new Analyzer(null);
	String dataFolder = "data/";
	String selectedDataset;
	JPanel slicerArea, fileSelection, topLevel, buttonArea;

	private JFrame frame = new JFrame("PhillyOpenData Analyzer");
	
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
		
		JPanel gridPanel = new JPanel(new GridLayout(3, 1));
		gridPanel.add(new JPanel());
		
		// Add the panel
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JPanel().add(new Label("Select dataset:")));
		panel.add(datasetSelection);
		panel.add(new JPanel());
		
		gridPanel.add(panel);
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
		
		String[] fields = analyzer.getGroupByFields().toString().split(",");
//		String[] fields = analyzer.getGroupByFields().toArray();
		
		JPanel panel = new JPanel(new GridLayout((fields.length / 3 + 1), 3));
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
		panel.setPreferredSize(new Dimension(fields.length / 3 * 30, 200));
		
		for (String str : fields) {
			panel.add(new JCheckBox(str));
		}
		
		return panel;
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
		JPanel panel = new JPanel();
		
		return panel;
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
		
		
		JPanel inputsPanel = new JPanel();
		inputsPanel.setLayout(new BorderLayout());
		
		JPanel slicerPanel = new JPanel();
		slicerPanel.setLayout(new GridLayout());
		
		// Could store the buttons/event listeners in a HashMap. 
		HashMap<String, EventListener> slicers = new HashMap<>();
		
		for (int i = 0; i < 4; i++) {
			slicers.put("test " + i, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("hit button: test!");
				}
			});
		}
		
		// Adding in the actual stuff from the HashMap
		for (String str : slicers.keySet()) {
			JButton temp = new JButton(str);
			slicerPanel.add(temp);
		}
		
		slicerArea = slicerArea(panel);
		fileSelection = fileSelection(panel);
		
		// Add an event listener to the comboBox
		JComboBox selectionBox = (JComboBox) ((JPanel) fileSelection.getComponent(1)).getComponent(1);
		selectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				selectedDataset = (String) cb.getSelectedItem(); 
				System.out.println("Setting the analyzer to : " + selectedDataset);
				analyzer = new Analyzer(selectedDataset);
				slicerArea = slicerArea(panel);
				panel.remove(1);
				panel.add(slicerArea, 1);
				panel.revalidate();
				panel.repaint();
			}
		});
		
		panel.add(fileSelection);
		panel.add(slicerArea);
		panel.add(inputsPanel);						
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
