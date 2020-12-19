package Symulation;

import java.util.Arrays;

public class DNA {
    int [] DNACounter;
    public DNA(int[] DNACounter)
    {
        this.DNACounter=DNACounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNA dna = (DNA) o;
        return Arrays.equals(DNACounter, dna.DNACounter);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(DNACounter);
    }
}
