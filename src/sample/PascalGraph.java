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
        bDef = 1;
        bStart = -1000;
        bStop = 1000;
        bStep = 0.2;

        start = 0;
        finish = 3.14 * 2;
        startLabel = "φMin:";
        finishLabel = "φMax:";

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
        double b = controller.spinnerB.getValue();
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return;
        double delta = (finish - start) / samples;
        for (double phi = start; phi <= finish + delta; phi += delta) {
            double r = b + a * Math.cos(phi);
            series.getData().add(new XYChart.Data<>(r * Math.cos(phi), r * Math.sin(phi)));
        }
    }

    @Override
    public String description() {
        double a = controller.spinnerA.getValue();
        double b = controller.spinnerB.getValue();
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return "min should be less than max";

        return String.format("""
                        r = b+αcos(φ) = %.1f+%.1f*cos(φ)
                        x = rcos(φ)
                        y = rsin(φ)
                        α = %.1f
                        b = %.1f
                        φ ∈ [%.1f;%.1f]
                        """,
                b, a, a, b, start, finish);
    }
}
