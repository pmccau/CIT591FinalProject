import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


/*
 * This will serve as the output for some functions in the Data Processor
 * Any graph with pairs of values (Bar Chart, Line Graph, Scatter Plot, etc)
 */

public class Graph {
	
	String graphName;
	HashMap <String, Double> categoryValues = new HashMap <String, Double>();
	JFreeChart finalChart;
		
	/**
	 * Constructor for a Graph. This takes in the name and the dataset
	 * @param graphName The name of the graph
	 * @param data The dataset to be shown
	 */
	public Graph (String graphName, HashMap<String, Double> data) { 
		this.graphName = graphName;
		this.categoryValues = data;
	}
		
	/**
	 * This method will generate a pie chart from the data that's been passed to
	 * the constructor
	 * @return A JPanel housing a pie chart
	 */
	public JPanel generatePieChart() {
		
		// Add the values to a DefaultKeyedValues data structure
		DefaultKeyedValues kvPairs = new DefaultKeyedValues();
		for (String str : categoryValues.keySet()) {
			kvPairs.addValue(str, categoryValues.get(str));
		}
		DefaultPieDataset finalData = new DefaultPieDataset(kvPairs);
		Plot p = new PiePlot(finalData);
		
		// This gets nested in a ChartPanel below
		JFreeChart chart = ChartFactory.createPieChart(graphName, finalData, false, false, false);
				
		// Finally, create the panel and add the chart
		JPanel output = new JPanel();
		output.add(new ChartPanel(chart));
		finalChart = chart;
		return output;
	}
	
	/**
	 * This method will export a graph as a JPEG
	 * @param fullFilepath The full filepath (do not include file extension)
	 */
	public void exportGraph(String fullFilepath) {
		try {
			ChartUtilities.saveChartAsJPEG(new File(fullFilepath + ".jpg"), finalChart, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method will generate a bar chart using the dataset and title provided
	 * @return
	 */
	public JPanel generateBarChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String str : categoryValues.keySet()) {
			dataset.addValue(categoryValues.get(str), str, graphName);
		}
		JFreeChart chart = ChartFactory.createBarChart(graphName, "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel panel = new ChartPanel(chart);
		finalChart = chart;
		return panel;
	}	
}
