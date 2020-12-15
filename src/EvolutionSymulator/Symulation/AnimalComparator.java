package Symulation;
import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        if(o1.getEnergy() > o2.getEnergy())
            return -1;
        else if(o1.getEnergy() < o2.getEnergy())
            return 1;
        return 0;
    }
}