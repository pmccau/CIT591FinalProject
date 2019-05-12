import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.jfree.ui.RefineryUtilities;

public class UserInterface implements Runnable {

	private DataParser dataparser = new DataParser(null);
	private int graphLimit = 7;
	private String dataFolder = "data/";
	private String selectedDataset, selectedNumericalValue, selectedGraphType, selectedDescriptor;
	private JPanel slicerArea, fileSelection, topLevel, buttonArea, fieldSelection;
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
		JLabel selectSlicersLabel = new JLabel("Select descriptive field");
		selectSlicersLabel.setFont(new Font("Slicers", Font.PLAIN, 24));;
		gridPanel.add(selectSlicersLabel);
		return gridPanel;
	}

	/**
	 * This method generates the graph type panel (pie vs. bar)
	 * @return
	 */
	public JPanel graphType() {
		String[] graphType = {"Bar", "Pie"};
		JPanel radioPanel = new JPanel(new GridLayout(graphType.length + 2, 1));
		JPanel panel = new JPanel(new GridLayout(1, 3));
		JLabel label = new JLabel("Graph type");
		label.setFont(new Font("radio", Font.BOLD, 15));
		radioPanel.add(label);
		selectedGraphType = "Bar";

		ButtonGroup bg = new ButtonGroup();
		int btnCounter = 0;

		// Add in radio buttons
		for (String str : graphType) {
			JRadioButton temp = new JRadioButton(str, btnCounter == 0);

			btnCounter++;
			bg.add(temp);
			radioPanel.add(temp);

			// Action listener for graph type
			temp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectedGraphType = str;
				}
			});
		}

		radioPanel.add(new JPanel());
		panel.add(new JPanel());
		panel.add(radioPanel);
		panel.add(new JPanel());

		return panel;
	}

	/**
	 * This will return the selection area for the field to be pivoted by
	 * @return
	 */
	public JPanel fieldSelectionArea() {
		JPanel panel = new JPanel(new GridLayout(3, 1));
		ArrayList<String> fields = dataparser.getGroupByFields();
		selectedDescriptor = "n/a";

		// Combo Box for the field selection
		JComboBox jcb = new JComboBox(fields.toArray());
		jcb.setBackground(Color.WHITE);
		jcb.setPreferredSize(new Dimension(200, 30));
		jcb.setSelectedIndex(-1);

		// Listener for choice of field here
		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDescriptor = (String) jcb.getSelectedItem();
			}
		});		

		// Label for the area below
		JLabel label = new JLabel("Select numerical field");
		label.setFont(new Font("Field selection", Font.PLAIN, 24));

		panel.add(jcb);
		panel.add(new JPanel());
		panel.add(label);
		return panel;
	}


	/**
	 * This method builds the slicer area, which contains radio buttons
	 * for deciding which numerical field to be used in the graphing
	 * @return a JPanel containing the slicers
	 */
	public JPanel slicerArea() {
		ArrayList<String> fields = dataparser.getNumericalFields();

		// Create the panel that will house the checkboxes
		JPanel panel = new JPanel(new GridLayout((fields.size() / 3 + 1), 3));
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
		panel.setPreferredSize(new Dimension(fields.size() / 3 * 30, 200));
		panel.setBackground(Color.WHITE);

		// Add in the checkboxes
		int buttonCtr = 0;
		ButtonGroup bg = new ButtonGroup();
		for (String str : fields) {
			JRadioButton temp = new JRadioButton(str, buttonCtr == 0);
			if (buttonCtr == 0) {
				selectedNumericalValue = str;
			}

			buttonCtr++;

			// Action listener to flesh out the values that will be grouped 
			temp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectedNumericalValue = str;
				}
			});

			temp.setBackground(Color.WHITE);
			bg.add(temp);
			panel.add(temp);
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
	 *  - Export to JPEG
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

		// ----------------------------------------------------------------
		// ----------------- ACTION LISTENERS FOR BUTTONS -----------------
		// ---------------------------------------------------------------- 
		visualizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph g = new Graph(selectedNumericalValue + " by " + selectedDescriptor, 
						dataparser.pivotDataBy(selectedDescriptor, selectedNumericalValue, false, 8));
				JFrame graphFrame = new JFrame();			
				JPanel graphPanel = new JPanel();

				if (selectedGraphType.equals("Pie")) {
					graphPanel = g.generatePieChart();
				} else {
					graphPanel = g.generateBarChart();
				}								

				graphFrame.add(graphPanel);
				graphFrame.setSize(new Dimension(800, 600));
				RefineryUtilities.centerFrameOnScreen(graphFrame);    
				graphFrame.setVisible(true); 
			}
		});

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
		panel.setLayout(new GridLayout(4, 1));

		// Could store the buttons/event listeners in a HashMap. 
		HashMap<String, EventListener> slicers = new HashMap<>();

		slicerArea = slicerArea();
		fileSelection = fileSelection(panel);
		buttonArea = buttonArea();
		fieldSelection = fieldSelectionArea();

		//----------------------------------------------------------------------------------------
		//                                   EVENT LISTENERS SECTION

		// Add an event listener to the comboBox so that selection values appear when dataset chosen
		JComboBox selectionBox = (JComboBox) ((JPanel) fileSelection.getComponent(1)).getComponent(1);
		selectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				selectedDataset = (String) cb.getSelectedItem(); 
				dataparser = new DataParser(selectedDataset);
				slicerArea = slicerArea();
				fieldSelection = fieldSelectionArea();

				// Add and remove the group by panel
				JPanel temp = (JPanel) panel.getComponent(1);
				temp.remove(0);
				temp.add(fieldSelection, 0);
				temp.revalidate();
				temp.repaint();

				// Add and remove the slicer panel
				panel.remove(2);
				panel.add(slicerArea, 2);

				// Revalidate everything
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
					DownloadFile df = new DownloadFile(datasets.get(str), new File("data/" + str + ".csv"));
					df.saveFile();
				}
				fileSelection = fileSelection(panel);

				// The listener for the selection box needs to be re-added
				JComboBox sb = (JComboBox) ((JPanel) fileSelection.getComponent(1)).getComponent(1);
				sb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox cb = (JComboBox) e.getSource();
						selectedDataset = (String) cb.getSelectedItem(); 
						dataparser = new DataParser(selectedDataset);
						slicerArea = slicerArea();
						fieldSelection = fieldSelectionArea();
						
						// Add and remove the group by panel
						JPanel temp = (JPanel) panel.getComponent(1);
						temp.remove(0);
						temp.add(fieldSelection, 0);
						temp.revalidate();
						temp.repaint();

						// Add and remove the slicer panel
						panel.remove(2);
						panel.add(slicerArea, 2);

						// Revalidate everything
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

					Graph g = new Graph(selectedNumericalValue + " by " + selectedDescriptor, 
							dataparser.pivotDataBy(selectedDescriptor, selectedNumericalValue, false, 8));
					JFrame graphFrame = new JFrame();			
					JPanel graphPanel = new JPanel();

					if (selectedGraphType.equals("Pie")) {
						g.generatePieChart();
					} else {
						g.generateBarChart();
					}
					g.exportGraph(exportLocation);
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

		JPanel jp = new JPanel(new GridLayout(1, 2));
		jp.add(fieldSelection);
		jp.add(graphType());

		panel.add(jp);

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
