package Symulation;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Chart {
    private final LineChart<?, ?> target;
    private final CategoryAxis oX;
    private final NumberAxis oY;
    private final String[] seriesNames;
    int dataCounter;
    int clearInterval;

    public Chart (LineChart<?, ?> targetChart, CategoryAxis oX, NumberAxis oY,String [] seriesNames,int clearInterval)
    {
        this.clearInterval = clearInterval;
        this.seriesNames = seriesNames;
        this.target = targetChart;
        this.oX = oX;
        this.oY = oY;
    }
    public void addData(String[] names,Number[] values)
    {
        if (this.dataCounter%this.clearInterval == 0)
        {
            this.clear();
        }
        dataCounter++;
        int n = names.length;
        for (int i =0;i<n;i++)
        {
            this.target.getData().get(i).getData().add(new XYChart.Data(names[i],values[i]));
        }
    }
    public void reset(String[] seriesNames)
    {
        dataCounter = 0;
        this.target.getData().clear();
        for(String seriesName : seriesNames)
        {
            XYChart.Series newSeries = new XYChart.Series();
            newSeries.setName(seriesName);
            this.target.getData().add(newSeries);
        }
    }
    public void clear()
    {
        this.reset(this.seriesNames);
    }

}
