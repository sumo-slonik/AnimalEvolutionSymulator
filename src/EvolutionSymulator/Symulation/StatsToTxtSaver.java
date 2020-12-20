package Symulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class StatsToTxtSaver {
    public StatsToTxtSaver()
    {
    }
    public void saveStats(Map source) throws FileNotFoundException {
        String actualDay = source.ActualDay.toString();
        try {
            File myObj = new File("Day"+actualDay+"Stats.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        PrintWriter result = new PrintWriter("Day"+actualDay+"Stats.txt");
        result.println("Dane z dnia "+ actualDay);
        result.println("Liczba zwierząt: "+ source.Observer.getActualPopulation());
        result.println("Liczba traw: "+ source.Grasses.size());
        result.println("średnia energia żyjących zwierząt: "+ source.Observer.getAverageEnergy());
        result.println("średnia wiek martwych zwierząt: "+ source.Observer.getAverageLifeTime());
        result.println("średnia liczba dzieci żyjących zwierząt: "+ source.Observer.getAverageChildren());
        System.out.println("Stats successful saved.");
        result.close();
    }
}
