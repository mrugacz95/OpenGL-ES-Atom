package example.com.projekt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mruga on 18.12.2016.
 */

public class AnimationData { //klasa przetrzymująca dane dot animacji
    private int timeStampCount;
    private int currentTimeStamp = 0;
    private List<TimeStamp> timeStamps = new ArrayList<>();

    public int getTimeStampCount() {
        return timeStampCount;
    }  //getter wynikający z enkapsulacji


    public void addTimeStamp(TimeStamp timeStamp) { //dodanie jednej klatki
        timeStamps.add(timeStamp);
    }
    public TimeStamp getCurrentTimeStamp() { //getter do pobrania aktulnej klatki
        return timeStamps.get(currentTimeStamp);
    }

    public void nextFrame() { //przejście do następnej klatki
        currentTimeStamp++;
        if(currentTimeStamp >= timeStamps.size())
            currentTimeStamp =0;
    }

    public static class TimeStamp { //pojedyńcza klatka
        public List<Point3d> getPositions() {
            return positions;
        } //położenia atomów

        private List<Point3d> positions = new ArrayList<>();//lista z pozycjami w tej klatce

        public void addPosition(Point3d point3d){
            positions.add(point3d);
        }
        public void addPosition(List<String> strPos){
            positions.add(new Point3d(strPos));
        }
    }

    public void setTimeStampCount(int timeStampCount) {
        this.timeStampCount = timeStampCount;
    }

}
