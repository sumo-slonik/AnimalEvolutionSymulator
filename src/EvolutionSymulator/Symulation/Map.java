package Symulation;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Map {
    int Height;
    int Width;
    Pane world;
    float JungleHeight;
    float JungleWidth;
    Vector2d LeftBottomCorner = new Vector2d(0,0);
    Vector2d RightTopCorner;
    Vector2d LeftBottomCornerJungle;
    Vector2d RightTopCornerJungle;
    private final ArrayList<Grass> grassList = new ArrayList<Grass>(0);
    private final ArrayList<Animal> animalsList = new ArrayList<Animal>(0);

    public Map (int height,int width,float jungleHeight,float jungleWidth,int numberOfAnimals,int defaultEnergy)
    {
        this.Height = height;
        this.Width = width;
        this.JungleHeight = jungleHeight;
        this.JungleWidth = jungleWidth;
        this.RightTopCorner=new Vector2d(Width,Height);
        this.LeftBottomCornerJungle = new Vector2d((int)((this.Width-this.Width*this.JungleWidth)/2),(int)((this.Height-this.Height*this.JungleHeight)/2));
        this.RightTopCornerJungle = this.LeftBottomCornerJungle.add(new Vector2d((int)(this.Width*this.JungleWidth),(int)(this.Height*this.JungleHeight)));
        int i = 0;
        while (i < numberOfAnimals)
        {
            Vector2d position =new Vector2d(ThreadLocalRandom.current().nextInt(0, this.Height),ThreadLocalRandom.current().nextInt(0, this.Width));
            if (! isOccupiedByAnimal(position))
            {
                int [] DNA = new int[32];
                for (int index =0;index<32;index++)
                {
                    DNA[index]= ThreadLocalRandom.current().nextInt(0, 9);
                }

                addAnimal(new Animal(defaultEnergy,position,DNA,0,world));
                i++;
            }

        }
    }
    boolean isOccupiedByAnimal(Vector2d position)
    {
        for (Animal actual : animalsList)
        {
            if (actual.getPosition().equals(position))
            {
                return true;
            }
        }
        return false;
    }
    boolean isOccupiedByGrass(Vector2d position)
    {
        for (Grass actual : grassList)
        {
            if (actual.getPosition().equals(position))
            {
                return true;
            }
        }
        return false;
    }

    boolean isOccupied(Vector2d position)
    {
        return isOccupiedByGrass(position) || isOccupiedByAnimal(position);
    }
    void addAnimal(Animal animal)
    {
        this.animalsList.add(animal);
    }


}
