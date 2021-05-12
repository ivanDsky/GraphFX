package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Controller {
    @FXML
    private Slider sliderA;
    @FXML
    private LineChart<Double,Double> graphChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextField xMin;
    private double min;
    @FXML
    private TextField xMax;
    private double max;



    public void initialize(){
        min = Double.parseDouble(xMin.getCharacters().toString());
        max = Double.parseDouble(xMax.getCharacters().toString());

        xMin.setTextFormatter(new TextFormatter<>(filter));
        xMax.setTextFormatter(new TextFormatter<>(filter));

        xMin.setOnAction(actionEvent -> {
            updateBorders();
        });
        xMin.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            updateBorders();
        });

        xMax.setOnAction(actionEvent -> {
            updateBorders();
        });
        xMax.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            updateBorders();
        });

        graphChart.setAnimated(false);
        xAxis.setLowerBound(min);
        xAxis.setUpperBound(max);
        graphChart.setCreateSymbols(false);

        xAxis.setAutoRanging(false);
        sliderA.valueProperty().addListener(((observableValue, number, t1) -> {
            updateDataset(observableValue.getValue().doubleValue(),graphChart.getData());
        }));

    }

    private void updateBorders(){
        min = Double.parseDouble(xMin.getCharacters().toString());
        xAxis.setLowerBound(min);
        updateDataset(sliderA.getValue(),graphChart.getData());

        max = Double.parseDouble(xMax.getCharacters().toString());
        xAxis.setUpperBound(max);
        updateDataset(sliderA.getValue(),graphChart.getData());
    }

    public void updateDataset(double a, ObservableList<XYChart.Series<Double, Double>> data){
        XYChart.Series<Double,Double> series;

        series = new XYChart.Series<>();
        series.setName("Witch of Agnesi");
        if(data.size() == 0)data.add(series);
        else data.set(0,series);

        double delta = (max - min) / 100;

        for(double i = min;i <= max;i += delta){
            series.getData().add(new XYChart.Data<>(i,8 * (a * a * a) / (i * i + 4 * a * a)));
        }

    }

    UnaryOperator<TextFormatter.Change> filter = t -> {
        try{
            Double.parseDouble(t.getControlNewText());
        }catch (Exception e){
            t.setText("");
        }
        return t;
    };

}
