package GuiTest;

import Symulation.Map;
import Symulation.Simulator;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Controler {
    @FXML
    Pane world;

    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button resetButton;




    Simulator sim;
    private Movment clock;
    private class Movment extends AnimationTimer
    {
        private long last = 0;
        @Override
        public void handle(long now)
        {
            long framesPerSecond = 30L;
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
        clock = new Movment();
        world.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
    }

    @FXML
    public void reset()
    {
        stop();
        world.getChildren().clear();
        sim = new Simulator(world,300);
    }
    @FXML
    public void step()
    {
        sim.move();
        sim.draw();
        sim.SpawnGrass();
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
