package sample;

import javafx.scene.chart.XYChart;

public class WitchGraph extends Graph {
    public WitchGraph(Controller controller) {
        super(controller);
        name = "Witch of Agnesi";
        showA = true;
        aDef = 1;
        aStart = -1000;
        aStop = 1000;
        aStep = 0.5;

        showB = false;

        xMin = -11;
        xMax = 11;

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
        xMin = Controller.parseDouble(controller.xMin.getText());
        xMax = Controller.parseDouble(controller.xMax.getText());
        double a = controller.spinnerA.getValue();
        double delta = (xMax - xMin) / 100;
        for (double i = xMin; i <= xMax; i += delta) {
            series.getData().add(new XYChart.Data<>(i, 8 * (a * a * a) / (i * i + 4 * a * a)));
        }
    }
}
