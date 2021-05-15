package sample;

import javafx.scene.chart.XYChart;

public class AstroidGraph extends Graph {
    public AstroidGraph(Controller controller) {
        super(controller);
        name = "Astroid";
        showA = true;
        aDef = 1;
        aStart = -1000;
        aStop = 1000;
        aStep = 0.2;

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
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return;
        double delta = (finish - start) / samples;
        for (double phi = start; phi <= finish + delta; phi += delta) {
            double r = a;
            double cos = Math.cos(phi), sin = Math.sin(phi);
            series.getData().add(new XYChart.Data<>(r * cos * cos * cos, r * sin * sin * sin));
        }

    }

    @Override
    public String description() {
        double a = controller.spinnerA.getValue();
        double start = Controller.parseDouble(controller.start.getText());
        double finish = Controller.parseDouble(controller.finish.getText());
        if (start + 1e-8 > finish) return "min should be less than max";
        return String.format("""
                x = acos³(φ)
                y = asin³(φ)
                a = %.1f
                φ ∈ [%.1f;%.1f]
                """, a, start, finish);
    }
}
