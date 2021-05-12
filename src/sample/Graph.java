package sample;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SpinnerValueFactory;

public abstract class Graph {
    public String name;
    public double xMin;
    public double xMax;

    public boolean showA;
    protected double aDef;
    protected double aStart;
    protected double aStop;
    protected double aStep;

    public boolean showB;
    protected double bDef;
    protected double bStart;
    protected double bStop;
    protected double bStep;

    protected final Controller controller;

    protected void updateDataset(XYChart.Series<Double, Double> series) {
    }

    protected XYChart.Series<Double, Double> seriesFromData(ObservableList<XYChart.Series<Double, Double>> data) {
        XYChart.Series<Double, Double> series;

        series = new XYChart.Series<>();
        series.setName(name);
        if (data.size() == 0) data.add(series);
        else data.set(0, series);

        return series;
    }

    public Graph(Controller controller) {
        this.controller = controller;
    }

    public void init() {
        controller.graphChart.setTitle(name);
        controller.spinnerA.getParent().setManaged(showA);
        controller.spinnerA.getParent().setVisible(showA);
        if (showA)
            controller.spinnerA.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(aStart, aStop, aDef, aStep));
        controller.spinnerB.getParent().setManaged(showB);
        controller.spinnerB.getParent().setVisible(showB);
        if (showB)
            controller.spinnerB.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(bStart, bStop, bDef, bStep));

        controller.xAxis.setLowerBound(xMin);
        controller.xMin.setText(Double.toString(xMin));
        controller.xAxis.setUpperBound(xMax);
        controller.xMax.setText(Double.toString(xMax));
    }

    @Override
    public String toString() {
        return name;
    }
}
