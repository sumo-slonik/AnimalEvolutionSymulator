package Symulation;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Animal extends AbstractWorldMapElement {
    //energy of the animal
    private final int Energy;
    private final int size = 5;
    //DNA determines the probability of movement in a given direction
    private final int[] DNA;
    private Vector2d Position;
    private MapDirection orientation;
    // means energy consumption, belong to the range [0,1]
    private float Tiredness = (float) 0.0;
    private Map map = null;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>(0);
    public Circle representation;
    private Pane world;
    private Color representationColor = Color.BROWN;

    public Animal(int Energy, Vector2d Position, int[] DNA, float tiredness, Pane world,Map map) {
        this.map = map;
        this.Energy = Energy;
        this.world = world;
        this.Position = Position;
        this.orientation = MapDirection.NORTH;
        this.DNA = DNA;
        this.Tiredness = tiredness;
        this.representation = new Circle(5, representationColor);
        representation.setStroke(Color.BLACK);
        world.getChildren().add(representation);
    }

    public Animal(int Energy, Vector2d Position, float tiredness, Pane world) {
        this.Energy = Energy;
        this.Position = Position;
        this.orientation = MapDirection.NORTH;
        this.Tiredness = tiredness;
        int[] DNA = new int[32];
        Random generator = new Random();
        for (int i = 0; i < DNA.length; i++) {
            DNA[i] = generator.nextInt(9);
        }
        this.DNA = this.DNACompleteTest(DNA);
    }

    public Animal(int Energy, Vector2d Position, int[] DNA, float tiredness) {
        this.Energy = Energy;
        this.Position = Position;
        this.orientation = MapDirection.NORTH;
        this.DNA = DNA;
        this.Tiredness = tiredness;
    }

    // process of recreating new animal witch mixed dna of two parents
    public Animal demagging(Animal secondParent) {
        int rand = ThreadLocalRandom.current().nextInt(0, 12 + 1);
        int[] FirstDNA = Arrays.copyOfRange(this.DNA, rand, rand + 20);
        rand = ThreadLocalRandom.current().nextInt(0, 19 + 1);
        int[] SecondDNA = Arrays.copyOfRange(secondParent.DNA, rand, rand + 12);
        int[] ChildDNA = new int[32];
        System.arraycopy(FirstDNA, 0, ChildDNA, 0, 20);
        System.arraycopy(SecondDNA, 0, ChildDNA, 20, 12);
        rand = ThreadLocalRandom.current().nextInt(0, 8);
        MapDirection spawnDirection;
        ChildDNA = this.DNACompleteTest(DNA);
        switch (rand) {
            case 0 -> spawnDirection = MapDirection.NORTH;
            case 1 -> spawnDirection = MapDirection.NorthEast;
            case 2 -> spawnDirection = MapDirection.EAST;
            case 3 -> spawnDirection = MapDirection.SouthEast;
            case 4 -> spawnDirection = MapDirection.SOUTH;
            case 5 -> spawnDirection = MapDirection.SouthWest;
            case 6 -> spawnDirection = MapDirection.WEST;
            case 7 -> spawnDirection = MapDirection.NorthWest;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        }
        this.Tiredness += 0.25;
        secondParent.Tiredness += 0.25;
        int ChildEnergy = this.Energy;
        if (this.world == null) {
            return new Animal(ChildEnergy, this.Position.add(spawnDirection.toOneStepVector()), ChildDNA, (float) 0.5);
        }
        return new Animal(ChildEnergy, this.Position.add(spawnDirection.toOneStepVector()), ChildDNA, (float) 0.5, this.world);
    }

    public void Move() {
        int rand = ThreadLocalRandom.current().nextInt(0, 32);
        rand = this.DNA[rand];
        for (int i = 0; i <= rand; i++) {
            this.orientation = this.orientation.next();
        }
        this.Position = this.Position.add(this.orientation.toOneStepVector());
        if (this.Position.x > this.world.getWidth() - size) {
            this.Position = new Vector2d(size, this.Position.y);
        }
        if (this.Position.y > this.world.getHeight() - size) {
            this.Position = new Vector2d(this.Position.x, size);
        }
        if (this.Position.x < size) {
            this.Position = new Vector2d(size, this.Position.y);
        }
        if (this.Position.y < size) {
            this.Position = new Vector2d(this.Position.x, size);
        }

    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    public void positionChanged(Vector2d old, Vector2d actual) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(old, actual);
        }
    }

    public int[] getDna() {
        return this.DNA;
    }

    public void draw() {

        representation.setRadius(this.world.getHeight()/this.);
        representation.setTranslateX(Position.x);
        representation.setTranslateY(Position.y);
    }

    public Vector2d getPosition() {
        return this.Position;
    }

    public float getTiredness() {
        return this.Tiredness;
    }

    public int[] DNACompleteTest(int[] DNA) {
        int[] counter = new int[9];
        for (int value : DNA) {
            counter[value] += 1;
        }
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] == 0) {
                int duplicateDNA = 0;
                counter[i]++;
                for (int j = 0; j < counter.length; j++) {
                    if (counter[j] > 1) {
                        counter[j]--;
                        duplicateDNA = j;
                    }
                }
                int index = 0;
                while (true) {
                    if (DNA[index] == duplicateDNA) {
                        DNA[index] = i;
                        break;
                    }
                    index++;
                }
            }
        }
        return DNA;
    }

    public float getEnergy() {
        return this.Energy*(1-this.Tiredness);
    }

    public void livingCost(int oneDayCost) {
        float deltaTiredness = (float) oneDayCost / this.Energy;
        this.Tiredness +=deltaTiredness;
        this.Tiredness = Math.min(1,this.Tiredness);
    }
    public void eatingGrass(int oneGrassEnergy)
    {
        float deltaTiredness = (float) oneGrassEnergy / this.Energy;
        this.Tiredness -= deltaTiredness;
        this.Tiredness = Math.max(0,this.Tiredness);
    }
}

