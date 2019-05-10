# CIT591FinalProject

The overarching concept of this project is to create a desktop application that would allow users to analyze data from <a href="https://www.opendataphilly.org/dataset?organization=school-district-of-philadelphia">Open Data Philly's school district collection.</a>

<b>Basic Functionality</b><ul>
  <li>Take in data from locally saved data sets. If files are not already saved locally, fetch them from the website <b>(File i/o)</b></li>
  <li>Analyze data using parameters specified by a user in the interface such that the user can generate a graph by a key value pair, where key is a text column and value is a summed numerical column. <b>(GUI, Data Analysis)</b></li>
  <li>Generate analyzed data in graph form and export to JPEG <b>(File i/o)</b></li>
  </ul>

<b>External Libraries</b><ul>
  <li>GUI using Swing library</li>
  <li>Data visualization using <a href="http://www.jfree.org/jfreechart/">JFreeChart</a></li></ul>

<h1>Instructions for Use</h1>
<ol>
  <li>Clone the repository locally.</li>
  <li>Add the JFreeChart JARs to your build path. You will need to add: jfreechart-1.0.1\lib\<b>jcommon-1.0.0</b> & <b>jfreechart-1.0.1</b></li>
  <li>Navigate to the UserInterface.java file and run it</li>
  <li>If you have any datasets saved locally, they will appear in the dropdown menu at the top. If not, click the 'Fetch Datasets' button. <i>Note: if you are on a Mac, this may present an issue as the filepath is set to include a backslash - it should save to a 'data' folder in your project folder.</i></li>
  <li>Choose a text field and a numerical field and press 'Visualize' to see the graph preview, 'Export' to save it locally</li>
  </ol>
