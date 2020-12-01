package Symulation;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Simulator {
    private ArrayList<Animal> animals;
    public Simulator(Pane world,int popSize)
    {
        animals = new ArrayList<Animal>();
        for (int i =0;i<popSize;i++)
        {
            int [] DNA = new int[32];
            for (int tmp =0; tmp<32;tmp++)
            {
                DNA[tmp]=ThreadLocalRandom.current().nextInt(0, 9);
            }

            Vector2d pose = new Vector2d(ThreadLocalRandom.current().nextInt(0, (int)world.getWidth()),ThreadLocalRandom.current().nextInt(0, (int)world.getHeight()));
            animals.add(new Animal(10,pose,DNA,(float)(0.0),world));
            draw();
        }
    }
    public ArrayList<Animal> getAnimals()
    {
        return animals;
    }
    public void draw()
    {
        for (Animal a : animals)
        {
            a.draw();
        }
    }
    public void move()
    {
        for (Animal a : animals)
        {
            a.Move();
        }
    }

}
