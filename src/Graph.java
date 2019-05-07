import java.awt.Dimension;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;


/*
 * This will serve as the output for some functions in the Data Processor
 * Any graph with pairs of values (Bar Chart, Line Graph, Scatter Plot, etc)
 */

public class Graph {
	
	String graphType;	// Bar chart, pie chart, scatter plot
	double x_axis_min;
	double x_axis_max;
	double y_axis_min;
	double y_axis_max;
	// Points are saved to either 
	HashMap <String, Double> CategoryValues = new HashMap <String, Double>();
	ArrayList <double[]> PointValues = new ArrayList <double[]>(); // Each item is a [x,y] list
 
	
	public Graph (String type, ArrayList<String> x_axis_data, ArrayList<String> y_axis_data) {
		graphType = type;
		int counter = 0;
		x_axis_min = 1000;
		x_axis_max = -1000;
		y_axis_min = 1000;
		y_axis_max = -1000;
		for (String X_Value : x_axis_data) {
			if (!X_Value.equals("") && !y_axis_data.get(counter).equals("")) {
				double x = Double.parseDouble(X_Value);
				double y = Double.parseDouble(y_axis_data.get(counter));
				double[] point = new double[2];
				point[0]=x;
				point[1]=y;
				PointValues.add(point);
				if (x < x_axis_min) {
					x_axis_min = x;
				}
				if (x > x_axis_max) {
					x_axis_max = x;
				}
				if (y < y_axis_min) {
					y_axis_min = y;
				}
				if (y > y_axis_max) {
					y_axis_max = y;
				}
			}
			counter++;
		}
	}
	
	public Graph (String type, HashMap<String, Double> data) {
		graphType = type;
		CategoryValues = data;
		double max = -100000;
		double min = 100000;
		for (String category: data.keySet()) {
			if (data.get(category) > max) {
				max = data.get(category);
			}
			if (data.get(category) < min) {
				min = data.get(category);
			}
		}
		y_axis_min = min;
		y_axis_max = max;
	}
	
	
	
	
	public String getGraphType() {
		return graphType;
	}
	public double getX_axis_min() {
		return x_axis_min;
	}
	public double getY_axis_min() {
		return y_axis_min;
	}
	public double getX_axis_max() {
		return x_axis_max;
	}
	public double getY_axis_max() {
		return y_axis_max;
	}
	
	
	// Getting and setting graph values
	public ArrayList<double[]> getPointValues() {
		return PointValues; //ADD
	}
	public HashMap<String, Double> getCategoryValues() {
		return CategoryValues; 
	}
	public void setAGraphValue(String category, double y) {
		CategoryValues.put(category, y);
	}
	public void setAGraphValue(double x, double y) {
		double[] point = {x,y};
		PointValues.add(point);
	}
	
	
	/**
	 * 	
	 * @param title
	 * @param records
	 * @return
	 */
	public static JPanel generatePieChart(String title, HashMap<String, Double> records) {
		
		// Add the values to a DefaultKeyedValues data structure
		DefaultKeyedValues kvPairs = new DefaultKeyedValues();
		for (String str : records.keySet()) {
			kvPairs.addValue(str, records.get(str));
		}
		DefaultPieDataset finalData = new DefaultPieDataset(kvPairs);
		Plot p = new PiePlot(finalData);
		
		// This gets nested in a ChartPanel below
//		JFreeChart chart = new JFreeChart(title, p);
		JFreeChart chart = ChartFactory.createPieChart(title, finalData, false, false, false);
		
		
		// Finally, create the panel and add the chart
		JPanel output = new JPanel();
		output.add(new ChartPanel(chart));
		return output;
	}
	
	
	public static void main(String[] args) {
		
		DataParser newRecord = new DataParser("District Employees and Finance - District Budget");
		HashMap<String, Double> dataset = newRecord.pivotDataBy("ACTIVITY_NAME", "OPERATING_CYEST_LUMPSUM_AMT", false, 7);
		
				
		JFrame demo = new JFrame();
		demo.setSize(new Dimension(800, 600));
		
		JPanel temp = generatePieChart("ACTIVITY_NAME", dataset);
		
		
		demo.add(temp);
		
		RefineryUtilities.centerFrameOnScreen( demo );    
	    demo.setVisible( true ); 
	}
	
}
