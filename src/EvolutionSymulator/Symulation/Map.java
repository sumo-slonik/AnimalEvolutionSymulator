package Symulation;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Map {
    private final int defaultEnergy;
    int Height;
    int Width;
    float JungleHeight;
    float JungleWidth;
    Vector2d LeftBottomCorner = new Vector2d(0, 0);
    Vector2d RightTopCorner;
    Vector2d LeftBottomCornerJungle;
    Vector2d RightTopCornerJungle;
    Pane World;
    int OneDayCost;
    int GrassEnergy;
    Rectangle jungle;
    public java.util.Map<Vector2d, ArrayList<Animal>> Animals = new HashMap<>();
    public java.util.Map<Vector2d, Grass> Grasses = new HashMap<>();
    AnimalObserver Observer;
    Integer ActualDay;

    public Map(Pane world, int height, int width, float jungleHeight, float jungleWidth, int numberOfGrass, int numberOfAnimals, int defaultEnergy, int GrassEnergy,int oneDayCost,AnimalObserver observer) {
        this.Observer = observer;
        this.Observer.reset();
        this.ActualDay =0;
        this.OneDayCost = oneDayCost;
        this.GrassEnergy = GrassEnergy;
        this.Height = height;
        this.defaultEnergy = defaultEnergy;
        this.Width = width;
        this.World = world;
        this.JungleHeight = jungleHeight;
        this.JungleWidth = jungleWidth;
        this.RightTopCorner = new Vector2d(Width, Height);
        this.LeftBottomCornerJungle = new Vector2d((int) ((this.Width - this.Width * this.JungleWidth) / 2), (int) ((this.Height - this.Height * this.JungleHeight) / 2));
        this.RightTopCornerJungle = this.LeftBottomCornerJungle.add(new Vector2d((int) (this.Width * this.JungleWidth), (int) (this.Height * this.JungleHeight)));
        jungle = new Rectangle();
        jungle.setX(LeftBottomCornerJungle.x*this.World.getWidth()/this.Width);
        jungle.setY(LeftBottomCornerJungle.y*this.World.getHeight()/this.Height);
        jungle.setWidth((RightTopCornerJungle.x-LeftBottomCornerJungle.x)*this.World.getWidth()/this.Width);
        jungle.setHeight((RightTopCornerJungle.y-LeftBottomCornerJungle.y)*this.World.getHeight()/this.Height);
        jungle.setFill(Color.DARKGREEN);
        world.getChildren().add(jungle);
        spawnGrass(numberOfGrass / 2, this.LeftBottomCorner, this.RightTopCorner);
        spawnGrass(numberOfGrass / 2, LeftBottomCornerJungle, RightTopCornerJungle);
        spawnAnimals(numberOfAnimals);
    }
    // umieszczanie zwierząt na mapie

    public void spawnAnimals(int numberOfAnimals) {
        Random generator = new Random();
        int counter = 0;
        for (int i = 0; i < numberOfAnimals; i++) {
            int height = this.RightTopCornerJungle.y - this.LeftBottomCornerJungle.y;
            int width = this.RightTopCornerJungle.x - this.LeftBottomCornerJungle.x;
            Vector2d position = new Vector2d(generator.nextInt(width) + this.LeftBottomCornerJungle.x,
                    generator.nextInt(height) + this.LeftBottomCornerJungle.y);
            if (!this.isOccupied(position)) {
                counter = 0;
                int[] DNA = new int[32];
                for (int index = 0; index < 32; index++) {
                    DNA[index] = ThreadLocalRandom.current().nextInt(0, 9);
                }
                Animal actualAnimal = new Animal(defaultEnergy, position, DNA, 0, this.World,this,this.Observer);
                ArrayList<Animal> animals = new ArrayList<>();
                animals.add(actualAnimal);
                this.Animals.put(position, animals);
                actualAnimal.born(this.ActualDay);
            }
            else
            {
                if (counter < 10)
                {
                    i--;
                }
                counter += 1;
            }
        }
    }
    public boolean placeGrass(Vector2d leftBottomCorner, Vector2d rightTopCorner) {
        Random generator = new Random();
        int x = generator.nextInt(rightTopCorner.x - leftBottomCorner.x) + leftBottomCorner.x;
        int y = generator.nextInt(rightTopCorner.y - leftBottomCorner.y) + leftBottomCorner.y;
        Vector2d spawnPosition = new Vector2d(x, y);
        if (!isOccupied(spawnPosition)) {
            Grass actualGrass = new Grass(spawnPosition, this.World,this);
            this.Grasses.put(spawnPosition, actualGrass);
            return true;
        }
        return false;
    }

    public void spawnGrass(int numberOfGrass, Vector2d leftBottomCorner, Vector2d rightTopCorner) {
        int i = 0;
        int counter =0;
        while (i < numberOfGrass && counter < 50) {
            if (placeGrass(leftBottomCorner, rightTopCorner)) {
                i++;
            }else {
                counter++;
            }
        }
    }

    public void dailyGrassSpawn() {
        spawnGrass(1, this.LeftBottomCorner, this.RightTopCorner);
        spawnGrass(1, this.LeftBottomCornerJungle, this.RightTopCornerJungle);
    }

    public boolean isOccupiedByGrass(Vector2d position) {
        return this.Grasses.get(position) != null;
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return this.Animals.get(position) != null;
    }

    public boolean isOccupied(Vector2d position) {
        return isOccupiedByAnimal(position) || isOccupiedByGrass(position);
    }

    public void eating() {
        for (Vector2d positions : this.Animals.keySet()) {
            if (isOccupiedByGrass(positions)) {
                this.World.getChildren().remove(this.Grasses.get(positions).representation);
                this.Grasses.remove(positions);
                ArrayList<Animal> animalsAtPostion = this.Animals.get(positions);
                ArrayList<Integer> strongestAnimalsIndex = choseStrongestAnimals(animalsAtPostion);
                for (Integer strongAnimal : strongestAnimalsIndex) {
                    animalsAtPostion.get(strongAnimal).eatingGrass(this.GrassEnergy / animalsAtPostion.size());
                }
            }
        }
    }

    public ArrayList<Integer> choseStrongestAnimals(ArrayList<Animal> animals) {
        ArrayList<Integer> results = new ArrayList<>();
        Animal actualStrongest = animals.get(0);
        for (Animal actual : animals) {
            if (actual.getEnergy() > actualStrongest.getEnergy()) {
                actualStrongest = actual;
            }
        }
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() == actualStrongest.getEnergy()) {
                results.add(i);
            }
        }
        return results;
    }

    public void reaper() {
        ArrayList<Animal> Animals = new ArrayList<>();
        for (ArrayList<Animal> animals : this.Animals.values()) {
            Animals.addAll(animals);
        }
        for (Animal actual : Animals) {
            if (actual.getEnergy() < this.OneDayCost) {
                actual.kill(this.ActualDay);
                if (this.Animals.get(actual.getPosition()).size() == 1) {
                    this.Animals.remove(actual.getPosition());
                } else {
                    this.Animals.get(actual.getPosition()).remove(actual);
                }
                this.World.getChildren().remove(actual.representation);
            }
        }
    }

    public void moveAll() {

        ArrayList<Animal> Animals = new ArrayList<>();
        for (ArrayList<Animal> animals : this.Animals.values()) {
            Animals.addAll(animals);
        }
        float actualEnergySum=0;
        int actualChildrenSum=0;
        for (Animal animal : Animals) {
            actualChildrenSum+= animal.getChildren();
            actualEnergySum+= animal.getEnergy();
            if (this.Animals.get(animal.getPosition()).size() > 1) {
                this.Animals.get(animal.getPosition()).remove(animal);
            } else {
                this.Animals.remove(animal.getPosition());
            }
            animal.Move();
            this.Animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<Animal>());
            this.Animals.get(animal.getPosition()).add(animal);
        }
        this.Observer.setActualChildrenSum(actualChildrenSum);
        this.Observer.setActualEnergySum(actualEnergySum);
    }
    public void reproduction() {
        ArrayList<Animal> children = new ArrayList<>();
        for (ArrayList<Animal> onePosition : this.Animals.values()) {
            if (onePosition.size() > 1) {
                AnimalComparator comparator = new AnimalComparator();
                onePosition.sort(comparator);
                Animal firsParent = onePosition.get(0);
                Animal secondParent = onePosition.get(1);
                if (firsParent.getEnergy()>this.defaultEnergy/2 && secondParent.getEnergy()>this.defaultEnergy/2) {
                    Animal child = firsParent.demagging(secondParent);
                    children.add(child);
                }
            }
        }
        for (Animal actual : children) {
            if (this.Animals.get(actual.getPosition()) != null) {
                this.Animals.get(actual.getPosition()).add(actual);
            } else {
                ArrayList<Animal> onePosition = new ArrayList<>();
                onePosition.add(actual);
                this.Animals.put(actual.getPosition(), onePosition);
            }
        }
        System.out.println(this.ActualDay+" "+children.size());
    }

    public void draw() {
        for (Grass actual : this.Grasses.values()) {
            actual.draw();
        }
        for (ArrayList<Animal> onePosition : this.Animals.values()) {
            for (Animal actual : onePosition) {
                actual.draw();
            }
        }
    }
    public void liveCost()
    {
        ArrayList<Animal> animals = new ArrayList<Animal>();
        for (ArrayList<Animal> onePosition : this.Animals.values()) {
            animals.addAll(onePosition);
            }
        for (Animal actual : animals)
        {
            actual.livingCost(this.OneDayCost);
        }
    }
    public void oneDayPass() {
        dailyGrassSpawn();
        reaper();
        eating();
        reproduction();
        moveAll();
        liveCost();
        this.ActualDay++;

    }
    public void changeParameters(int height,int width,int dayEnergy,int grassEnergy)
    {
        this.Height = height;
        this.Width = width;
        this.OneDayCost = dayEnergy;
        this.GrassEnergy = grassEnergy;
    }
    public int getDate()
    {
        return this.ActualDay;
    }
    public int getWidth() {return this.Width;}
    public int getHeight(){return this.Height;}
    public Animal getAnimalAtPosition(Vector2d pose)
    {
        if( this.Animals.get(pose) == null)
        {
            return null;
        }else
        {
            this.Animals.get(pose).get(0).isTheChosenOne = true;
            return this.Animals.get(pose).get(0);
        }
    }
}
