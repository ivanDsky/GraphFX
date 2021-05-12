package sample;

import javafx.scene.chart.XYChart;

public class PascalGraph extends Graph {
    public PascalGraph(Controller controller) {
        super(controller);
        name = "Pascal";
        showA = true;
        aDef = 2;
        aStart = -1000;
        aStop = 1000;
        aStep = 0.2;

        showB = true;
        bDef = 2;
        bStart = -1000;
        bStop = 1000;
        bStep = 0.2;

        xMin = -1;
        xMax = 5;

        init();
    }

    @Override
    public void init() {
        super.init();
        controller.updateDataset = this::updateDataset;
        controller.seriesFromData = this::seriesFromData;
    }

    @Override
    protected void updateDataset(XYChart.Series<Double, Double> series) {
        double a = controller.spinnerA.getValue();
        double b = controller.spinnerB.getValue();
        double delta = (Math.PI * 2) / 100;
        for (double phi = 0; phi < Math.PI * 2; phi += delta) {
            double r = b + a * Math.cos(phi);
            series.getData().add(new XYChart.Data<>(r * Math.cos(phi), r * Math.sin(phi)));
        }
    }
}
