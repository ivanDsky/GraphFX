package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

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
    public TextField xMin;
    @FXML
    public TextField xMax;
    @FXML
    public Button saveButton;
    @FXML
    public ComboBox<Graph> comboBoxGraph;

    public void initialize() {
        spinnerA.getEditor().setTextFormatter(new TextFormatter<>(filter));
        spinnerB.getEditor().setTextFormatter(new TextFormatter<>(filter));

        ObservableList<Graph> list = FXCollections.observableList(List.of(
                new PascalGraph(this),
                new WitchGraph(this)
        ));

        comboBoxGraph.getItems().addAll(list);
        comboBoxGraph.valueProperty().addListener((observableValue, graph, t1) -> {
            t1.init();
            updateBorders();
        });
        comboBoxGraph.valueProperty().set(list.get(0));

        xMin.setTextFormatter(new TextFormatter<>(filter));
        xMax.setTextFormatter(new TextFormatter<>(filter));

        xMin.setOnAction(actionEvent -> updateBorders());
        xMin.focusedProperty().addListener((observableValue, aBoolean, t1) -> updateBorders());

        xMax.setOnAction(actionEvent -> updateBorders());
        xMax.focusedProperty().addListener((observableValue, aBoolean, t1) -> updateBorders());

        graphChart.setAnimated(false);
        graphChart.setCreateSymbols(false);

        xAxis.setAutoRanging(false);

        spinnerA.valueProperty().addListener(((observableValue, number, t1) ->
                updateDataset.accept(seriesFromData.apply(graphChart.getData()))));

        spinnerB.valueProperty().addListener(((observableValue, number, t1) ->
                updateDataset.accept(seriesFromData.apply(graphChart.getData()))));

        saveButton.setOnAction(actionEvent -> saveAsPng(graphChart));

        updateBorders();

    }

    private void updateBorders() {
        double min = parseDouble(xMin.getCharacters().toString());
        xAxis.setLowerBound(min);
        double max = parseDouble(xMax.getCharacters().toString());
        xAxis.setUpperBound(max);

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
