package sample;

import javafx.scene.chart.XYChart;

public class ArchimedGraph extends Graph {
    public ArchimedGraph(Controller controller) {
        super(controller);
        name = "Archimed Spiral";
        showA = true;
        aDef = 1;
        aStart = -1000;
        aStop = 1000;
        aStep = 0.2;

        start = 0;
        finish = Math.PI * 4;
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
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return;
        double delta = (finish - start) / samples;
        for (double phi = start; phi <= finish + delta; phi += delta) {
            double r = a * phi;
            double cos = Math.cos(phi), sin = Math.sin(phi);
            series.getData().add(new XYChart.Data<>(r * cos, r * sin));
        }
    }

    @Override
    public String description() {
        double a = controller.spinnerA.getValue();
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return "min should be less than max";
        return String.format("""
                r = αφ = %.1f*φ
                x = rcos(φ)
                y = rsin(φ)
                α = %.1f
                φ ∈ [%.1f;%.1f]
                """, a, a, start, finish);
    }
}
