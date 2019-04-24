import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.HashMap;

import javax.swing.*;

public class UserInterface implements Runnable {
	
	Analyzer analysis = new Analyzer(null);

	private JFrame frame = new JFrame("PhillyOpenData Analyzer");
	
	/**
	 * This will be the place where the file to be analyzed is selected.
	 * This should have a dropdown menu with the various data sets. Selection
	 * of one set should trigger a refresh of the slicer area. This will be contained 
	 * at the top of the topLevel panel
	 * @return
	 */
	public JPanel fileSelection() {
		
		String[] test = {"this", "is", "a", "test"};
		
		JComboBox datasetSelection = new JComboBox(test);
		datasetSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selection made: " + e.getSource().toString());
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(datasetSelection);
		return panel;
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
	public JPanel slicerArea(String dataSet) {
		JPanel panel = new JPanel();
		
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
		
		
		
//		JButton button2 = new JButton("test");
//		panel.add(button2);
		
		panel.add(inputsPanel);
		panel.add(slicerPanel);
				
		JButton button1 = new JButton("test");
		panel.add(button1);
		panel.add(fileSelection());
		
		return panel;
	};
	
	// Run method. This will be called to generate the GUI
	public void run() {
		frame.add(topLevel());
		
//		frame.setPreferredSize(new Dimension(745, 375));
//		frame.setMaximumSize(frame.getPreferredSize());
//		frame.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Main method. Funtion TBD 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new UserInterface());
	}

}
