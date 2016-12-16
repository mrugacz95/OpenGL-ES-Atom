package example.com.projekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class WczytajDaneZPliku

{
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Czytam dane.");
        File plik = new File ("plik.xyz");
        Scanner odczyt = new Scanner(plik);
        while (odczyt.hasNextLine())
            System.out.println(odczyt.nextLine());
    }

}
