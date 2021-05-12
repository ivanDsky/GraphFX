package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Controller {
    @FXML
    public Spinner<Double> spinnerA;
    @FXML
    public Spinner<Double> spinnerB;
    @FXML
    public LineChart<Double, Double> graphChart;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public Label startLabel;
    @FXML
    public TextField start;
    @FXML
    public Label finishLabel;
    @FXML
    public TextField finish;
    @FXML
    public Button saveButton;
    @FXML
    public ComboBox<Graph> comboBoxGraph;
    @FXML
    public Label description;
    @FXML
    public Slider density;

    public void initialize() {
        spinnerA.getEditor().setTextFormatter(new TextFormatter<>(filter));
        spinnerB.getEditor().setTextFormatter(new TextFormatter<>(filter));

        ObservableList<Graph> list = FXCollections.observableList(List.of(
                new AstroidGraph(this),
                new ArchimedGraph(this),
                new PascalGraph(this),
                new WitchGraph(this)
        ));

        comboBoxGraph.getItems().addAll(list);
        comboBoxGraph.valueProperty().addListener((observableValue, graph, t1) -> {
            t1.init();
            updateData();
            setAutoRanging(true);
        });

        ChartPanManager panManager = new ChartPanManager(graphChart);
        panManager.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                //let it through
            } else {
                mouseEvent.consume();
            }
        });
        panManager.start();
        JFXChartUtil.setupZooming(graphChart, Event::consume);
        JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(graphChart);
        comboBoxGraph.valueProperty().set(list.get(0));

        start.setTextFormatter(new TextFormatter<>(filter));
        finish.setTextFormatter(new TextFormatter<>(filter));

        start.setOnAction(actionEvent -> updateData());
        start.focusedProperty().addListener((observableValue, aBoolean, t1) -> updateData());

        finish.setOnAction(actionEvent -> updateData());
        finish.focusedProperty().addListener((observableValue, aBoolean, t1) -> updateData());

        graphChart.setAnimated(false);
        graphChart.setCreateSymbols(false);

        spinnerA.valueProperty().addListener(((observableValue, number, t1) -> updateData()));
        spinnerB.valueProperty().addListener(((observableValue, number, t1) -> updateData()));
        density.valueProperty().addListener((observableValue, number, t1) -> updateData());

        saveButton.setOnAction(actionEvent -> saveAsPng(graphChart));

        updateData();
        setAutoRanging(true);

    }

    private void setAutoRanging(boolean range) {
        xAxis.setAutoRanging(range);
        yAxis.setAutoRanging(range);
    }

    private void updateData() {
        description.setText(comboBoxGraph.getValue().description());
        updateDataset.accept(seriesFromData.apply(graphChart.getData()));
    }

    public Consumer<XYChart.Series<Double, Double>> updateDataset;
    public Function<ObservableList<XYChart.Series<Double, Double>>, XYChart.Series<Double, Double>> seriesFromData;

    UnaryOperator<TextFormatter.Change> filter = t -> {
        if (t.getControlNewText().equals("-")) return t;
        try {
            Double.parseDouble(t.getControlNewText());
        } catch (Exception e) {
            t.setText("");
        }
        return t;
    };

    public static double parseDouble(String t) {
        try {
            return Double.parseDouble(t);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static FileChooser chooser;

    public static void saveAsPng(LineChart<Double, Double> chart) {
        if (chooser == null) {
            chooser = new FileChooser();
            chooser.setTitle("Choose save folder");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        }

        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
        File file = chooser.showSaveDialog(null);
        if (file != null) {
            chooser.setInitialDirectory(file.getParentFile());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Graph saved");
                alert.setContentText("Graph saved to " + file.getAbsolutePath());
                alert.setHeaderText(null);
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Graph didn't save");
                alert.setContentText("Error occurred");
                alert.setHeaderText(null);
                alert.show();
                e.printStackTrace();
            }
        }
    }

}
