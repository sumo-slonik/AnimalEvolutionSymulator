package Symulation;

//Klasa ta reprezenuję mape na której znajdują się rośliny i zwierząta
//odpowiada też za ich przemieszczanie, rozmnażanie oraz ryuswoanie
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    //zwierze wybrane do obserwacji
    Animal Selected;
    Vector2d LeftBottomCorner = new Vector2d(0, 0);
    Vector2d RightTopCorner;
    Vector2d LeftBottomCornerJungle;
    Vector2d RightTopCornerJungle;
    Pane World;
    int OneDayCost;
    int GrassEnergy;
    Rectangle jungle;
    //zbiór zwierząt na mapie
    public java.util.Map<Vector2d, ArrayList<Animal>> Animals = new HashMap<>();
    //zbiór traw na mapie
    public java.util.Map<Vector2d, Grass> Grasses = new HashMap<>();
    AnimalObserver Observer;
    Integer ActualDay;

    public Map(Pane world, int height, int width, float jungleHeight, float jungleWidth, int numberOfGrass, int numberOfAnimals, int defaultEnergy, int GrassEnergy,int oneDayCost,AnimalObserver observer) {
        //obserwator śledzący parametry zwierząt
        this.Observer = observer;
        this.Observer.reset();
        //ustalenie parametrów startowych zgodnie z wejściem od urztkownika
        this.ActualDay =0;
        this.OneDayCost = oneDayCost;
        this.GrassEnergy = GrassEnergy;
        this.Height = height;
        this.defaultEnergy = defaultEnergy;
        this.Width = width;
        //world to obiekt typu Pane w GUI na którym rysowane są zwierzęta
        this.World = world;
        //położenie dżungl;i reprezentowane jest za pomocą dwóch wektoróe opisujących jej lewy dolny i prawy górny róg
        this.JungleHeight = jungleHeight;
        this.JungleWidth = jungleWidth;
        this.RightTopCorner = new Vector2d(Width, Height);
        this.LeftBottomCornerJungle = new Vector2d((int) ((this.Width - this.Width * this.JungleWidth) / 2), (int) ((this.Height - this.Height * this.JungleHeight) / 2));
        this.RightTopCornerJungle = this.LeftBottomCornerJungle.add(new Vector2d((int) (this.Width * this.JungleWidth), (int) (this.Height * this.JungleHeight)));
        // rysowanie ciemniejszego prostokąta reprezentujuącego dżunglę
        jungle = new Rectangle();
        jungle.setX(LeftBottomCornerJungle.x*this.World.getWidth()/this.Width);
        jungle.setY(LeftBottomCornerJungle.y*this.World.getHeight()/this.Height);
        jungle.setWidth((RightTopCornerJungle.x-LeftBottomCornerJungle.x)*this.World.getWidth()/this.Width);
        jungle.setHeight((RightTopCornerJungle.y-LeftBottomCornerJungle.y)*this.World.getHeight()/this.Height);
        jungle.setFill(Color.DARKGREEN);
        world.getChildren().add(jungle);
        //dodanie zwierząt
        spawnAnimals(numberOfAnimals);
        //Dodanie połowy traw startowych w dżungli i drugiej połowy na całej mapie
        spawnGrass(numberOfGrass / 2, this.LeftBottomCorner, this.RightTopCorner);
        spawnGrass(numberOfGrass / 2, LeftBottomCornerJungle, RightTopCornerJungle);

    }

    //funckja tworząca nowe zwierząta
    public void spawnAnimals(int numberOfAnimals) {
        Random generator = new Random();
        //generowanie losowej pozycji startowej w dżungli od której zaczynamy poszukiwania mejsca dla zwierzaka
        //licznik nieudanych prób postawienia zwierzaka, jeśli dżungla jest mocno pełna szuklanie miejsca dla zwierzaka może być trudne dlatego
        //żeby uniknąć zapętl;aniea się programu w tym miejscu, po kilku nieudanych próbach przechodzimy dalej
        int counter = 0;
        for (int i = 0; i < numberOfAnimals; i++) {
            int height = this.RightTopCornerJungle.y - this.LeftBottomCornerJungle.y-2;
            int width = this.RightTopCornerJungle.x - this.LeftBottomCornerJungle.x-2;
            Vector2d position = new Vector2d(generator.nextInt(width) + this.LeftBottomCornerJungle.x+1,
                    generator.nextInt(height) + this.LeftBottomCornerJungle.y+1);
            //jeśli miejsce wybrane jest zajęte sprawdzamy miejsca niedaleko wylosowanego, jednak tylkote będące w obrębie dżungli
            for (int dy =0;dy<16 && isOccupied(position);dy++)
            {
                for (int dx = 0;dx<16 && isOccupied(position);dx++)
                {
                    Vector2d delta = new Vector2d(dx,dy);
                    if (position.add(delta).precedes(RightTopCornerJungle.add(new Vector2d(-2,-2))) && position.add(delta).follows(LeftBottomCornerJungle))
                    {
                        position=position.add(delta);
                    }
                }
            }
            //jeśli udało nam się znaleźć wolne miejsce umieszczamy tam zwierzaka
            if (!this.isOccupied(position)) {
                counter = 0;
                int[] DNA = new int[32];
                for (int index = 0; index < 32; index++) {
                    DNA[index] = ThreadLocalRandom.current().nextInt(0, 8);
                }
                Animal actualAnimal = new Animal(defaultEnergy, position, DNA, 0, this.World,this,this.Observer);
                ArrayList<Animal> animals = new ArrayList<>();
                animals.add(actualAnimal);
                this.Animals.put(position, animals);
                actualAnimal.born(this.ActualDay);
            }
            else
            {
                // w przeciwnym wypadku powtarzamy swzukanie miejsca dla i-tego zwierzaka 10 razy
                //w praktyce nawet dla dużej lczby zwierząt nie ma to znaczącego wpływu na szybkość inicjalizacji mapy
                if (counter < 10)
                {
                    i--;
                }
                else
                {
                    counter = -1;
                }
                //zliczamy nieudane prtóby
                counter += 1;
            }
        }
    }
    //funkcja umieszczająca trawy na podanym zakresie mapy w losowym miejscu, zwraca true jeśli spawn
    //trawy zakończył się powodzeniem
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
    //dodanie określonej liczby traw na wskazanym obszarze
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
    //funckcja obsługująca codzienne wyrastanie traw
    public void dailyGrassSpawn() {
        spawnGrass(1, this.LeftBottomCorner, this.RightTopCorner);
        spawnGrass(1, this.LeftBottomCornerJungle, this.RightTopCornerJungle);
    }
    //sprawdzenie czy na danej pozycji jest trawa
    public boolean isOccupiedByGrass(Vector2d position) {
        return this.Grasses.get(position) != null;
    }
    //sprawdzenie czy na danej pozycji jest zwierze

    public boolean isOccupiedByAnimal(Vector2d position) {
        return this.Animals.get(position) != null;
    }
    //sprawdzenie czy na danej pozycji jest cokolwiek
    public boolean isOccupied(Vector2d position) {
        return isOccupiedByAnimal(position) || isOccupiedByGrass(position);
    }
    //osbługa jedzenia traw przez zwierzęta
    public void eating() {
        //iterujemy się po pozycjach zajmowanych przez zwierzęta
        for (Vector2d positions : this.Animals.keySet()) {
            //jeśli na jakiejś pozycji jest zwierzę i trawa to zwierzę zjada trawę
            if (isOccupiedByGrass(positions)) {
                this.World.getChildren().remove(this.Grasses.get(positions).representation);
                this.Grasses.remove(positions);
                ArrayList<Animal> animalsAtPostion = this.Animals.get(positions);
                //wybieremy zwierzęta z najwiękrzą il;ością energi na danym polu w szczegulności jedno zwierzę
                ArrayList<Integer> strongestAnimalsIndex = choseStrongestAnimals(animalsAtPostion);
                //rozdzielamy energię z trawy pomiędzy najsilniejsze zwierzęta
                for (Integer strongAnimal : strongestAnimalsIndex) {
                    animalsAtPostion.get(strongAnimal).eatingGrass(this.GrassEnergy / animalsAtPostion.size());
                }
            }
        }
    }
    //funkcja wybierająca zwierzęta z najwiękrzą ilością energi na danym polu (zwraca ich indexy)
    public ArrayList<Integer> choseStrongestAnimals(ArrayList<Animal> animals) {
        ArrayList<Integer> results = new ArrayList<>();
        //szukamy wartości maksymalnej energi zwierząt na polu
        Animal actualStrongest = animals.get(0);
        for (Animal actual : animals) {
            if (actual.getEnergy() > actualStrongest.getEnergy()) {
                actualStrongest = actual;
            }
        }
        // zapisujemy wszystkie zwierzęta które mają maksymalną energię
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() == actualStrongest.getEnergy()) {
                results.add(i);
            }
        }
        return results;
    }
    //funkcja zabijająca zwierząta z energią nie wystarczającą do życia
    //i usuwająca martwe zwierzęta z mapy
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
    //funckja odpowiadająca za poruszanie się zwierząt
    //przy okazji ruszania się zwierząt zbierane są informacje o ich średniej energi
    //i średniej liczbie dzieci przez obserwatora
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
            //aktualizacja słownika przechowującego informacje o zwierzętach
            //narpierw usuwamy zwierzę ze słownika pod adresem jego poprzedniej pozycji
            if (this.Animals.get(animal.getPosition()).size() > 1) {
                this.Animals.get(animal.getPosition()).remove(animal);
            } else {
                this.Animals.remove(animal.getPosition());
            }
            //wykonujemy ruch zwierzęcia
            animal.Move();
            //dodajmy zwierzę pod nową pozycją do słownika zwierząt
            this.Animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<Animal>());
            this.Animals.get(animal.getPosition()).add(animal);
        }
        //przekazanie informacji o aktualnym stanie zwierząt do obserwatora
        this.Observer.setActualChildrenSum(actualChildrenSum);
        this.Observer.setActualEnergySum(actualEnergySum);
    }
    //funckcja odpowiadająca za rozmnażanie
    public void reproduction() {
        ArrayList<Animal> children = new ArrayList<>();

        for (ArrayList<Animal> onePosition : this.Animals.values()) {
            //sprawdzamy czy na jednej pozycji jest więcej niż dwa zwierzaki
            if (onePosition.size() > 1) {
                //szukamy zwierząt o dwuch najwiękrzych energiach
                AnimalComparator comparator = new AnimalComparator();
                onePosition.sort(comparator);
                Animal firsParent = onePosition.get(0);
                Animal secondParent = onePosition.get(1);
                //sprawdzamy czy warunek na rozmnażanie jest spełniony przez oboje rodzoiców
                if (firsParent.getEnergy()>this.defaultEnergy/2 && secondParent.getEnergy()>this.defaultEnergy/2) {
                    //jeśli tak dochodzi do rozmnożenia
                    Animal child = firsParent.demagging(secondParent);
                    //dodajemy dzieci do tablicy dzieci
                    children.add(child);
                }
            }
        }
        //dzieci dodajemy do mapy
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
    //rysowanie zwierząt i traw znajdujących się na mapie
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
    // zwiękrzenie poziomu zmęczenia zwierząt wynikającego ze stałego kosztu
    //jednego dnia życia
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
    //funckja odpowiadająca za upływ jednego dnia
    public void oneDayPass() {
        dailyGrassSpawn();
        reaper();
        moveAll();
        eating();
        reproduction();
        liveCost();
        this.ActualDay++;

    }
    //dynamiczna zamiana parametrów mapy
    //zmiana energi z jednej trawy, zmiana kosztu życia
    public void changeParameters(int dayEnergy,int grassEnergy)
    {
        this.OneDayCost = dayEnergy;
        this.GrassEnergy = grassEnergy;
    }
    //pobranie aktualnego dnia na mapie
    public int getDate()
    {
        return this.ActualDay;
    }
    //pobrania zwierzęcia na ustalonej pozycji i ustawienie go jako wybranego do obserwacji
    //ze względu na małą precyzje kliknięcia konkretnego zwierzaka sprawdzane są pozycje także do okoła tej konkrentnej
    public Animal getAnimalAtPosition(Vector2d pose)
    {
        int [] dx = new int[]{0,0,-1,-1,-1,0,1,1,1};
        int [] dy = new int[]{0,1,1,0,-1,-1,-1,0,1};
        for (int i =0;i<9;i++)
        {
            if (this.Animals.get(pose.add(new Vector2d(dx[i],dy[i])))!= null)
            {
                if (this.Selected != null)
                {
                    this.Selected.isTheChosenOne = false;
                    this.Selected.draw();
                }
                this.Animals.get(pose.add(new Vector2d(dx[i],dy[i]))).get(0).isTheChosenOne = true;
                this.Selected = this.Animals.get(pose.add(new Vector2d(dx[i],dy[i]))).get(0);
                return this.Animals.get(pose.add(new Vector2d(dx[i],dy[i]))).get(0);
            }
        }
        return null;
    }
    //zwracanie konkretnych wartości mapy
    public int getWidth() {return this.Width;}
    public int getHeight(){return this.Height;}
    public Animal getSelected(){ return this.Selected; }
    public int getNumberOfAnimals() {return this.Observer.actualPopulation;}
}
