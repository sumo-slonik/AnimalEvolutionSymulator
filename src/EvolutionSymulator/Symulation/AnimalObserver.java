package Symulation;

public class AnimalObserver {
    float averageEnergy;
    float actualEnergySum;
    int[] genCounter;
    float averageLifeTime;
    int liveTimeSum;
    int actualPopulation;
    int dedPopulation;
    int actualChildrenSum;

    public AnimalObserver() {
        this.averageEnergy = 0;
        this.actualEnergySum = 0;
        this.genCounter = new int[9];
        this.averageLifeTime = 0;
        this.liveTimeSum = 0;
        this.actualPopulation = 0;
        this.dedPopulation = 0;
        this.actualChildrenSum = 0;
    }

    void animalSpawn(Animal newAnimal) {
        actualPopulation++;
        for (int i : newAnimal.getDna()) {
            genCounter[i]++;
        }
        actualEnergySum += newAnimal.getEnergy();
        averageEnergy = (float) actualEnergySum / actualPopulation;
    }

    void animalKill(Animal killedAnimal, int actualDay) {
        actualPopulation--;
        dedPopulation++;
        for (int i : killedAnimal.getDna()) {
            genCounter[i]--;
        }
        liveTimeSum += killedAnimal.bornDay;
        averageLifeTime = (float) liveTimeSum / dedPopulation;
    }

    void setActualEnergySum(float sum) {
        this.actualEnergySum = sum;
    }

    void setActualChildrenSum(int sum) {
        this.actualChildrenSum = sum;
    }

    public int[] getGenCounter() {
        return this.genCounter;
    }

    public float getAverageLifeTime() {
        return this.averageLifeTime;
    }

    public int getActualPopulation() {
        return this.actualPopulation;
    }

    public int getDedPopulation() {
        return this.dedPopulation;
    }

    public void reset() {
        this.averageEnergy = 0;
        this.actualEnergySum = 0;
        this.genCounter = new int[9];
        this.averageLifeTime = 0;
        this.liveTimeSum = 0;
        this.actualPopulation = 0;
        this.dedPopulation = 0;
        this.actualChildrenSum = 0;
    }

    public float getAverageEnergy() {
        return this.actualEnergySum / this.actualPopulation;
    }

    public float getAverageChildren()
    {
        return (float)this.actualChildrenSum/this.actualPopulation;
    }
}
