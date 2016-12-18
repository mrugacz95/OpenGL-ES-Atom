package example.com.projekt;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mruga on 18.12.2016.
 */

public class FileLoader {
    AnimationData animationData = null;
    public FileLoader(final Context context) {
        //Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File file = new File(sdcard,"in.xyz"); //pobranie pliku z karty sd
//Read text from file
        AnimationData animationData = new AnimationData();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) { //czytanie do kończa pliku
                animationData.setTimeStampCount(Integer.valueOf(line)); //pobranie ilosci atomów
                String name =  br.readLine(); //wiersz z nazwą
                AnimationData.TimeStamp timeStamp = new AnimationData.TimeStamp(); //stworzenie pojedyńczej klatki
                for(int i=0;i<animationData.getTimeStampCount();i++) {
                    line = br.readLine();
                    List<String> strPos = new LinkedList<>(Arrays.asList(line.split("\\s*A[rR]\\s+|\\s+", -1))); // rozdzielenie floatów regexem czyli początku "Ar  " i spacji
                    strPos.remove(0);// pierwszy string jest pusty
                    timeStamp.addPosition(strPos); //dodanie pozycji
                }
                animationData.addTimeStamp(timeStamp);
            }
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(context,"Nie można załadować pliku in.xyz", Toast.LENGTH_LONG).show();
            return; //jak coś poszło nie tak to wyjście
        }
        this.animationData = animationData; //jeżeli wczytało się dobrze to zamieniamy animationData


    }
    public AnimationData getAnimationData() {
        return animationData;
    }
}
