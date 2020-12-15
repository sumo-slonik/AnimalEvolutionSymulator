package GuiTest;

import Symulation.Map;
import Symulation.Simulator;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class ControlerWithMap {
    int Height;
    int Width;
    float JungleProportion;
    int StartEnergy;
    int DayEnergy;
    int GrassEnergy;
    int AnimalNumber;
    int GrassNumber;
    int Days;
    @FXML
    Pane world;
    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button resetButton;
    @FXML
    Slider heightSlider;
    @FXML
    Slider widthSlider;
    @FXML
    Slider jungleSlider;
    @FXML
    Slider startEnergySlider;
    @FXML
    Slider dayEnergySlider;
    @FXML
    Slider grassEnergySlider;
    @FXML
    Slider animalsSlider;
    @FXML
    Slider grassSlider;
    @FXML
    TextField heightText;
    @FXML
    TextField widthText;
    @FXML
    TextField jungleText;
    @FXML
    TextField startEnergyText;
    @FXML
    TextField dayEnergyText;
    @FXML
    TextField grassEnergyText;
    @FXML
    TextField animalNumberText;
    @FXML
    TextField grassNumberText;
    @FXML
    TextField daysText;
    Simulator sim;

    Map newMap;
    private Movment clock;
    private class Movment extends AnimationTimer
    {
        private long last = 0;
        @Override
        public void handle(long now)
        {
            long framesPerSecond = 100L;
            long interval = 1000000000L / framesPerSecond;
            if(now-last> interval)
            {
                step();
                last = now;
            }
        }
    }

    @FXML
    public void initialize()
    {
        heightSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setHeight();
            }
        });
        widthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setWidth();
            }
        });
        jungleSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setJungleProportion();
            }
        });
        startEnergySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setStartEnergy();
            }
        });
        dayEnergySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setDayEnergy();
            }
        });
        grassEnergySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setGrassEnergy();
            }
        });
        animalsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setAnimalNumber();
            }
        });
        grassSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setGrassNumber();
            }
        });
        clock = new Movment();
        world.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
        setHeight();
        setWidth();
        setAnimalNumber();
        setDayEnergy();
        setGrassEnergy();
        setGrassNumber();
        setJungleProportion();
        setStartEnergy();
        dayEnergyText.setText("0");
    }
    public void setHeight()
    {
        this.Height = (int) heightSlider.getValue();
        heightText.setText(""+this.Height);
    }
    public void setWidth()
    {
        this.Width = (int) widthSlider.getValue();
        widthText.setText(""+this.Width);
    }
    public void setJungleProportion()
    {
        this.JungleProportion = (int) jungleSlider.getValue();
        jungleText.setText(""+this.JungleProportion);
    }
    public void setStartEnergy()
    {
        this.StartEnergy= (int) startEnergySlider.getValue();
        startEnergyText.setText(""+this.StartEnergy);
    }
    public void setDayEnergy()
    {
        this.DayEnergy = (int) dayEnergySlider.getValue();
        dayEnergyText.setText(""+this.DayEnergy);
    }
    public void setGrassEnergy()
    {
        this.GrassEnergy = (int) grassEnergySlider.getValue();
        grassEnergyText.setText(""+this.GrassEnergy);
    }
    public void setAnimalNumber()
    {
        this.AnimalNumber = (int) animalsSlider.getValue();
        animalNumberText.setText(""+this.AnimalNumber);
    }
    public void setGrassNumber()
    {
        this.GrassNumber = (int) grassSlider.getValue();
        grassNumberText.setText(""+this.GrassNumber);
    }

    @FXML
    public void reset()
    {
        stop();
        Days = 0;
        world.getChildren().clear();
        newMap = new Map(world,100,100,(float) 0.5,(float)0.5,1000,500,500,50,1);
        start();
        stop();
    }
    @FXML
    public void step()
    {
        Days+=1;
        daysText.setText(""+Days);
        System.out.println(this.Height);
        newMap.oneDayPass();
        newMap.draw();
    }

    public void disableButtons(boolean start,boolean stop,boolean reset)
    {
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        resetButton.setDisable(reset);
    }
    @FXML
    public void start()
    {
        disableButtons(true,false,false);
        clock.start();
    }

    @FXML
    public void stop()
    {
        disableButtons(false,true,false);
        clock.stop();
    }

}
