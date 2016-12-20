package example.com.projekt;

import java.util.List;

/**
 * Created by mruga on 18.12.2016.
 */

public class Point3d { //punkt w 3d trzymający położeine pojedyńczego atomu
    public float x;
    public float y;
    public float z;

    public Point3d(float x, float y, float z) { //konstruktor z 3 floatów
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point3d(List<String> strPos) { //konstruktor z listy stringów
        this.x = Float.valueOf(strPos.get(0));
        this.y = Float.valueOf(strPos.get(1));
        this.z = Float.valueOf(strPos.get(2));
    }
}
