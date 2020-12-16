package Symulation;

public class Main {
    public static void main(String[] args){
        int[] DNA1 = new int[32];
        int[] DNA2 = new int[32];
        for (int i = 0;i<32;i++)
        {
            DNA1[i]=1;
            DNA2[i]=2;
        }
        Animal zwierze1 = new Animal(100,new Vector2d(1,1),DNA1,(float)0.3);
        zwierze1.eatingGrass(10);
    }
}
