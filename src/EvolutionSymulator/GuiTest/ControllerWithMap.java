package GuiTest;

import Symulation.*;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ControllerWithMap {
    int Height;
    int Width;
    float JungleProportion = (float) 0.5;
    int StartEnergy;
    int DayEnergy;
    int GrassEnergy;
    int AnimalNumber;
    int GrassNumber;
    int Days;
    int posX;
    int posY;
    int[] ArchivalPopulation = new int[100];
    AnimalObserver observer = new AnimalObserver();
    boolean firstTime = true;
    public static long FPS = 30L;
    @FXML
    Slider fpsSlider;
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
    @FXML
    TextField NumberOfAnimals;
    @FXML
    TextField NumberOfDedAnimals;
    @FXML
    Pane DnaStats;
    @FXML
    private LineChart<?, ?> Linechart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private LineChart<?, ?> LifeTimeChart;
    @FXML
    private CategoryAxis LifeTimeX;
    @FXML
    private NumberAxis LifeTimeY;
    @FXML
    private LineChart<?, ?> AverageEnergyChart;
    @FXML
    private CategoryAxis EnergyX;
    @FXML
    private NumberAxis EnergyY;
    @FXML
    private LineChart<?, ?> ChildrenChart;
    @FXML
    private CategoryAxis ChildrenX;
    @FXML
    private NumberAxis ChildrenY;
    @FXML
    TextField Children;
    @FXML
    TextField BirthDay;
    @FXML
    TextField DedDay;
    Animal Selected;
    @FXML
    Pane AnimalEnergy;
    @FXML
    TextField AnimalEnergyPrompt;
    @FXML
    CheckBox secondMap;
    @FXML
    TextField AnimalDNA;
    boolean isChange = false;
    @FXML
    CheckBox colorMostPopularDNA;
    Map newMap;
    private Movement clock;
    Chart populationChart;
    Chart lifeTimeChart;
    Chart childrenChart;
    Chart energyChart;

    private class Movement extends AnimationTimer {
        private long last = 0;

        @Override
        public void handle(long now) {
            long framesPerSecond = FPS;
            long interval = 1000000000L / framesPerSecond;
            if (now - last > interval) {
                step();
                last = now;
            }
        }
    }

    @FXML
    public void initialize() {
        Selected = null;
        populationChart = new Chart(Linechart, x, y, new String[]{"Zwierzątka", "Roślinki"}, 1000);
        lifeTimeChart = new Chart(LifeTimeChart, LifeTimeX, LifeTimeY, new String[]{"średnia długość życia"}, 500);
        childrenChart = new Chart(ChildrenChart, ChildrenX, ChildrenY, new String[]{"średnia liczba dzieci"}, 500);
        energyChart = new Chart(AverageEnergyChart, EnergyX, EnergyY, new String[]{"średnia poziom energi"}, 500);
        world.addEventFilter(MouseEvent.MOUSE_CLICKED, choseAnimalPose);
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
        fpsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setFPS();
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
        clock = new Movement();
        world.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        setHeight();
        setWidth();
        setAnimalNumber();
        setDayEnergy();
        setGrassEnergy();
        setGrassNumber();
        setJungleProportion();
        setStartEnergy();
        reset();
        dayEnergyText.setText("0");
    }

    public void setFPS() {
        FPS = (long) fpsSlider.getValue();
    }

    public void setHeight() {
        this.Height = (int) heightSlider.getValue();
        heightText.setText("" + this.Height);
        setWidth();
    }

    public void setWidth() {
        this.Width = (int) widthSlider.getValue();
        widthText.setText("" + this.Width);
        reset();
    }

    public void setJungleProportion() {
        reset();
        this.JungleProportion = (float) jungleSlider.getValue();
        jungleText.setText("" + this.JungleProportion);
        isChange = true;
    }

    public void setStartEnergy() {
        reset();
        this.StartEnergy = (int) startEnergySlider.getValue();
        startEnergyText.setText("" + this.StartEnergy);
        isChange = true;
    }

    public void setDayEnergy() {
        this.DayEnergy = (int) dayEnergySlider.getValue();
        dayEnergyText.setText("" + this.DayEnergy);
        isChange = true;
    }

    public void setGrassEnergy() {
        this.GrassEnergy = (int) grassEnergySlider.getValue();
        grassEnergyText.setText("" + this.GrassEnergy);
        isChange = true;
    }

    public void setAnimalNumber() {
        int proportionToSlider = (int) Math.ceil(animalsSlider.getMax() / (this.newMap.getHeight() * newMap.getWidth() * this.JungleProportion));
        this.AnimalNumber = (int) animalsSlider.getValue() / proportionToSlider;
        reset();
        animalNumberText.setText("" + this.newMap.getNumberOfAnimals());
        isChange = true;
    }

    public void setGrassNumber() {
        reset();
        this.GrassNumber = (int) grassSlider.getValue();
        grassNumberText.setText("" + newMap.Grasses.size());
        isChange = true;
    }

    @FXML
    public void reset() {
        stop();
        resetCharts();
        world.getChildren().clear();
        newMap = new Map(world, Height, Width, JungleProportion, JungleProportion, GrassNumber, AnimalNumber, StartEnergy, GrassEnergy, DayEnergy, observer);
        step();
        Days = 0;
        daysText.setText("" + Days);
    }

    @FXML
    public void step() {
        if (colorMostPopularDNA.isSelected())
        {
            newMap.findingPopularAnimalDna();
        }
        else
        {
            newMap.unsetAllAsPopularAnimal();
        }
        if (observer.getActualPopulation() == 0) {
            stop();
            return;
        }
        if (secondMap.isSelected()) {
            world.setMaxHeight(50);
        }
        drawAnimalStats();
        drawDnaStats();
        drawChart(newMap.getDate());
        if (isChange) {
            isChange = false;
            newMap.changeParameters(DayEnergy, GrassEnergy);
        }
        Days += 1;
        NumberOfAnimals.setText("" + observer.getActualPopulation());
        NumberOfDedAnimals.setText("" + observer.getDedPopulation());
        daysText.setText("" + Days);
        newMap.oneDayPass();
        newMap.draw();
    }

    public void disableButtons(boolean start, boolean stop, boolean reset) {
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        resetButton.setDisable(reset);
    }

    @FXML
    public void start() {
        disableButtons(true, false, false);
        clock.start();
    }

    @FXML
    public void stop() {
        disableButtons(false, true, false);
        clock.stop();
    }

    public EventHandler choseAnimalPose = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            posX = (int) event.getX();
            posX = (int) event.getX();
            posY = (int) event.getY();
            System.out.println(posX + "  " + posY);
            Selected = newMap.getAnimalAtPosition(getPoseFromPixels(posX, posY));
            if (Selected != null) {
                Selected.draw();
                drawAnimalStats();
                System.out.println("___________________");
                System.out.println(Selected.getEnergy());
                System.out.println("___________________");
            } else {
                System.out.println("___________________");
                System.out.println("fail");
                System.out.println("___________________");
            }
        }
    };

    public void drawDnaStats() {
        DnaStats.getChildren().clear();
        int sum = 1;
        for (int i : observer.getGenCounter()) {
            sum += i;
        }
        int startX = 0;
        Color[] colorOfDna = new Color[]{Color.GREEN, Color.ROSYBROWN, Color.ROYALBLUE, Color.DARKGREEN, Color.LAVENDER, Color.BURLYWOOD, Color.AQUAMARINE, Color.INDIANRED, Color.CHARTREUSE};
        for (int i = 0; i < 9; i++) {
            Rectangle oneDna = new Rectangle(startX, 0, 15, observer.getGenCounter()[i] * 400 / sum);
            oneDna.setFill(Color.rgb(Math.min(Math.round(252 * ((float) observer.getGenCounter()[i] / sum)) * 8, 252), Math.min(Math.round(50 * ((float) observer.getGenCounter()[i] / sum)) * 8, 50), Math.min(Math.round(100 * ((float) observer.getGenCounter()[i] / sum)) * 8, 100)));
            DnaStats.getChildren().add(oneDna);
            startX += 25;
        }
    }

    public void drawChart(Integer date) {
        if (date % 50 == 0) {
            populationChart.addData(new String[]{date.toString(), date.toString()}, new Integer[]{observer.getActualPopulation(), newMap.Grasses.size()});
            lifeTimeChart.addData(new String[]{date.toString()}, new Float[]{observer.getAverageLifeTime()});
            childrenChart.addData(new String[]{date.toString()}, new Float[]{observer.getAverageChildren()});
            energyChart.addData(new String[]{date.toString()}, new Float[]{observer.getAverageEnergy()});
            System.out.println(observer.getAverageLifeTime());
        }
    }

    public void resetCharts() {
        populationChart.clear();
        lifeTimeChart.clear();
        childrenChart.clear();
        energyChart.clear();
    }

    public Vector2d getPoseFromPixels(int x, int y) {
        int rectangleSizeX = (int) this.world.getWidth() / newMap.getWidth();
        int rectangleSizeY = (int) this.world.getHeight() / newMap.getHeight();
        return new Vector2d(x / rectangleSizeX, y / rectangleSizeY - 1);
    }

    public void drawAnimalStats() {
        if (Selected != null) {
            Children.setText("" + Selected.getChildren());
            BirthDay.setText("" + Selected.getBirthDay());
            Rectangle energy;
            if (Selected.isDeath()) {
                DedDay.setText("" + Selected.getDeathDate());
                energy = new Rectangle(10, 10, 0, 20);

            } else {
                if (Selected.getTiredness() < 0.5) {
                    DedDay.setText("Mam się dobrze");
                } else {
                    DedDay.setText("Mam się średnio, ale żyję");
                }
                energy = new Rectangle(10, 10, (int) 125 * (1 - Selected.getTiredness()), 20);
            }
            AnimalEnergyPrompt.setText("" + (int) (((1 - Selected.getTiredness()) * 100) % 100) + "%");
            AnimalEnergy.getChildren().clear();
            energy.setFill(Color.rgb(Math.round(255 * (Selected.getTiredness())), Math.round(222 * (1 - this.Selected.getTiredness())), 50));
            AnimalEnergy.getChildren().add(energy);
            AnimalDNA.setText(Selected.getDnaAsString());
        }
    }
}

