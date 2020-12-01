package GuiTest;

import Symulation.Simulator;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Controler {
    @FXML
    Pane world;

    Simulator sim;
    private Movment clock;
    private class Movment extends AnimationTimer
    {
        private long FramesPerSecond =30L;
        private long Interval = 1000000000L / FramesPerSecond;
        private long last = 0;
        @Override
        public void handle(long now)
        {
            if(now-last>Interval)
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

    @FXML
    public void start()
    {
        clock.start();
    }

    @FXML
    public void stop()
    {
        clock.stop();
    }

}
