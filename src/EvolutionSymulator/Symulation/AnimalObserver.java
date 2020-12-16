package Symulation;

public class AnimalObserver {
    float averageEnergy =0;
    int actualEnergySum;
    int [] genCounter = new int [8];
    float averageLifeTime=0;
    int liveTimeSum = 0;
    int actualPopulation=0;
    int dedPopulation=0;
    void animalSpawn(Animal newAnimal)
    {
        actualPopulation++;
        for (int i:newAnimal.getDna())
        {
           genCounter[i]++;
        }
        actualEnergySum+=newAnimal.getEnergy();
        averageEnergy = (float)actualEnergySum/actualPopulation;
    }
    void animalKill(Animal killedAnimal,int actualDay)
    {
        actualPopulation--;
        dedPopulation++;
        liveTimeSum+=killedAnimal.bornDay;
        averageLifeTime = (float) liveTimeSum/dedPopulation;
    }
    void setActualEnergySum(int sum)
    {
        this.actualEnergySum = sum;
    }
    public float getAverageEnergy (){return this.averageEnergy;}
    public int [] getGenCounter (){return this.genCounter;}
    public float getAverageLifeTime(){return this.averageLifeTime;}
    public int getActualPopulation(){return this.actualPopulation;}
    public int getDedPopulation(){return this.dedPopulation;}
}
