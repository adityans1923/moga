import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Plot extends JFrame {
    private static final long serialVersionUID = 6294689542092367723L;

    public Plot(String title, String xlabel, String ylabel, ArrayList<ArrayList<Double> > data) {
        super("Scatter Plot");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 400);
        this.setLocationRelativeTo(null);
        XYDataset dataset = createDataset(data);
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, xlabel, ylabel, dataset);
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));
        ChartPanel panel = new ChartPanel(chart);
        this.setContentPane(panel);
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    private XYDataset createDataset(ArrayList<ArrayList<Double> > data) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Boys");
        for (ArrayList<Double> datum : data) {
            series1.add(datum.get(1), datum.get(0));
        }

//        series1.add(1, 72.9);
//        series1.add(2, 81.6);
//        series1.add(3, 88.9);
//        series1.add(4, 96);
//        series1.add(5, 102.1);
//        series1.add(6, 108.5);
//        series1.add(7, 113.9);
//        series1.add(8, 119.3);
//        series1.add(9, 123.8);
//        series1.add(10, 124.4);
        dataset.addSeries(series1);
        return dataset;
    }
}