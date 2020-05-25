package Testing;

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
        super("Scatter Testing.Plot");
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
        XYSeries series1 = new XYSeries("Data");
        for (ArrayList<Double> datum : data) {
            series1.add(datum.get(0), datum.get(1));
        }
        dataset.addSeries(series1);
        return dataset;
    }
}