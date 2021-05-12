package sample;

import javafx.scene.chart.NumberAxis;
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

        start = -11;
        finish = 11;
        startLabel = "xMin:";
        finishLabel = "xMax:";

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
        super.updateDataset(series);
        double a = controller.spinnerA.getValue();
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return;
        double delta = (finish - start) / samples;
        for (double x = start; x <= finish + delta; x += delta) {
            series.getData().add(new XYChart.Data<>(x, 8 * (a * a * a) / (x * x + 4 * a * a)));
        }
    }

    @Override
    public String description() {
        double a = controller.spinnerA.getValue();
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return "min should be less than max";
        return String.format("""
                        y = 8a³/(x²+4a²) = 8*%.1f/(x²+4*%.1f)
                        a = %.1f
                        x ∈ [%.1f;%.1f]
                        """,
                a * a * a, a * a, a, start, finish);
    }
}
