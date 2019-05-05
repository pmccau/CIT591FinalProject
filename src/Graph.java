import java.util.*;

/*
 * This will serve as the output for some functions in the Data Processor
 * Any graph with pairs of values (Bar Chart, Line Graph, Scatter Plot, etc)
 */

public class Graph {

	String graphType;	// Bar chart, pie chart, scatter plot
	double x_axis_min;
	double y_axis_min;

	// Points are saved to either 
	HashMap <String, Double> CategoryValues = new HashMap <String, Double>();
	ArrayList <double[]> PointValues = new ArrayList <double[]>(); // Each item is a [x,y] list

	
	
	public String getGraphType() {
		return graphType;
	}
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
	public double getX_axis_min() {
		return x_axis_min;
	}
	public void setX_axis_min(double x_axis_min) {
		this.x_axis_min = x_axis_min;
	}
	public double getY_axis_min() {
		return y_axis_min;
	}
	public void setY_axis_min(double y_axis_min) {
		this.y_axis_min = y_axis_min;
	}
	
	// Getting and setting values
	public ArrayList<double[]> getPointValues() {
		return PointValues; //ADD
	}
	public HashMap<String, Double> getCategoryValues() {
		return CategoryValues; // ADD
	}
	public void setAGraphValue(String category, double y) {
		CategoryValues.put(category, y);
	}
	public void setAGraphValue(double x, double y) {
		double[] point = {x,y};
		PointValues.add(point);
	}

	
}
