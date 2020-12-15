package Symulation;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Map {
    private final int defaultEnergy;
    int EnergyPerDay;
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
    public java.util.Map<Vector2d, ArrayList<Animal>> Animals = new HashMap<>();
    public java.util.Map<Vector2d, Grass> Grasses = new HashMap<>();

    public Map(Pane world, int height, int width, float jungleHeight, float jungleWidth, int numberOfGrass, int numberOfAnimals, int defaultEnergy, int GrassEnergy,int oneDayCost) {
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
        int i = 0;
        spawnGrass(numberOfGrass / 2, this.LeftBottomCorner, this.RightTopCorner);
        spawnGrass(numberOfGrass / 2, LeftBottomCornerJungle, RightTopCornerJungle);
        spawnAnimals(numberOfAnimals);
    }
    // umieszczanie zwierząt na mapie

    public void spawnAnimals(int numberOfAnimals) {
        Random generator = new Random();
        for (int i = 0; i < numberOfAnimals; i++) {
            int height = this.RightTopCornerJungle.y - this.LeftBottomCornerJungle.y;
            int width = this.RightTopCornerJungle.x - this.LeftBottomCornerJungle.x;
            Vector2d position = new Vector2d(generator.nextInt(width) + this.LeftBottomCornerJungle.x,
                    generator.nextInt(height) + this.LeftBottomCornerJungle.y);
            if (!this.isOccupied(position)) {
                int[] DNA = new int[32];
                for (int index = 0; index < 32; index++) {
                    DNA[index] = ThreadLocalRandom.current().nextInt(0, 9);
                }
                Animal actualAnimal = new Animal(defaultEnergy, position, DNA, 0, this.World,this);
                ArrayList<Animal> animals = new ArrayList<>();
                animals.add(actualAnimal);
                this.Animals.put(position, animals);
            } else i--;
        }
    }

    public boolean placeGrass(Vector2d leftBottomCorner, Vector2d rightTopCorner) {
        Random generator = new Random();
        int x = generator.nextInt(rightTopCorner.x - leftBottomCorner.x) + leftBottomCorner.x;
        int y = generator.nextInt(rightTopCorner.y - leftBottomCorner.y) + leftBottomCorner.y;
        Vector2d spawnPosition = new Vector2d(x, y);
        if (!isOccupied(spawnPosition)) {
            Grass actualGrass = new Grass(spawnPosition, this.World);
            this.Grasses.put(spawnPosition, actualGrass);
            return true;
        }
        return false;
    }

    public void spawnGrass(int numberOfGrass, Vector2d leftBottomCorner, Vector2d rightTopCorner) {
        int i = 0;
        while (i < numberOfGrass) {
            if (placeGrass(leftBottomCorner, rightTopCorner)) {
                i++;
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
                System.out.println("tutaj");
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

        for (Animal animal : Animals) {
            if (this.Animals.get(animal.getPosition()).size() > 1) {
                this.Animals.get(animal.getPosition()).remove(animal);
            } else {
                this.Animals.remove(animal.getPosition());
            }
            animal.Move();
            this.Animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<Animal>());
            this.Animals.get(animal.getPosition()).add(animal);
        }
    }
    public void reproduction() {
        ArrayList<Animal> children = new ArrayList<>();
        for (ArrayList<Animal> onePosition : this.Animals.values()) {
            if (onePosition.size() > 1) {
                AnimalComparator comparator = new AnimalComparator();
                onePosition.sort(comparator);
                Animal firsParent = onePosition.get(0);
                Animal secondParent = onePosition.get(1);
                Animal child = firsParent.demagging(secondParent);
                children.add(child);
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

    }

}
