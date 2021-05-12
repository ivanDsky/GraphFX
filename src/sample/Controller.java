package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;

public class Controller {
    @FXML
    private Slider sliderA;
    @FXML
    private LineChart<Double,Double> graphChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    public void initialize(){
        graphChart.setAnimated(false);
        xAxis.setLowerBound(-11);
        xAxis.setUpperBound(11);
        graphChart.setCreateSymbols(false);

        xAxis.setAutoRanging(false);
        sliderA.valueProperty().addListener(((observableValue, number, t1) -> {
            System.out.println(observableValue.getValue());
            updateDataset(observableValue.getValue().doubleValue(),graphChart.getData());

        }));

    }

    public void updateDataset(double a, ObservableList<XYChart.Series<Double, Double>> data){
        XYChart.Series<Double,Double> series;

        series = new XYChart.Series<>();
        series.setName("Name ");
        if(data.size() == 0)data.add(series);
        else data.set(0,series);

        for(double i = -11;i <= 11;i += 0.2){
            series.getData().add(new XYChart.Data<>(i,8 * (a * a * a) / (i * i + 4 * a * a)));
        }

//        for(int i = 0;i < 90; ++i){
//            double x = Math.sin(i * Math.PI / 180.0);
//            double y = Math.cos(i * Math.PI / 180.0);
//            if(series.getData().size() <= i)series.getData().add(new XYChart.Data<>(x * radius,y * radius));
//            else series.getData().set(i,new XYChart.Data<>(x * radius,y * radius));
//        }
    }


//    public void setupSliderA(){
//        sliderA.setMin(0);
//        sliderA.setMax(100);
//        sliderA.setMax(1.0);
//        sliderA.setMinorTickCount(2);
//        sliderA.setMajorTickUnit(5);
//    }

    @FXML
    void dragSliderA(DragEvent event) {
        sliderA.setMin(-10);
    }
}
