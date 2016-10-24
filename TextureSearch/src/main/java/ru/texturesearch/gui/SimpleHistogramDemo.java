package ru.texturesearch.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

/**
* A histogram with dynamically added data.
*/
public class SimpleHistogramDemo extends JFrame implements ActionListener {
    
    private SimpleHistogramDataset dataset;
    
    public SimpleHistogramDemo(String title) {
        super(title);
        
        // create the dataset with appropriate bins and some initial data
        this.dataset = new SimpleHistogramDataset("Key");
        dataset.addBin(new SimpleHistogramBin(-0.5, 0.5, true, false));
        dataset.addBin(new SimpleHistogramBin(0.5, 1.5, true, false));
        dataset.addBin(new SimpleHistogramBin(1.5, 2.5, true, false));
        //dataset.addBin(new SimpleHistogramBin(2.5, 3.5, true, false));
        dataset.addBin(new SimpleHistogramBin(2.9, 3.1, true, false)) ;
        dataset.addObservations(new double[] {1.0, 2.0, 1.0, 1.0, 2.0, 3.0, 
            0.0, 0.0, 2.0, 2.0, 3.0, 0.0, 2.0, 2.0});
        
        // create the chart with integer axis labels
        JFreeChart chart = ChartFactory.createHistogram("My Histogram", 
                "Covers Passed", "Count", dataset, PlotOrientation.VERTICAL, 
                false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainZeroBaselineVisible(false);
        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        // set up the UI
        ChartPanel panel = new ChartPanel(chart);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel);
        JButton button = new JButton("Add one more observation");
        button.addActionListener(this);
        getContentPane().add(button, BorderLayout.SOUTH);
    }
    
    public void actionPerformed(ActionEvent e) {
        // add a random observation in the range 0 to 3
        this.dataset.addObservation((int) (Math.random() * 4.0));
    }

    /**
     * Starting point for the demo.
     * 
     * @param args  ignored 
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimpleHistogramDemo demo = new SimpleHistogramDemo(
                        "JFreeChart: SimpleHistogramDemo");
                demo.pack();
                demo.setVisible(true);
            }
        });
    }
}