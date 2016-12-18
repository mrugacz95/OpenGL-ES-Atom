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
    }


    public void addTimeStamp(TimeStamp timeStamp) {
        timeStamps.add(timeStamp);
    }
    public TimeStamp getCurrentTimeStamp() {
        return timeStamps.get(currentTimeStamp);
    }

    public void nextFrame() {
        currentTimeStamp++;
        if(currentTimeStamp >= timeStamps.size())
            currentTimeStamp =0;
    }

    public static class TimeStamp { //pjedyńcza klatka
        public List<Point3d> getPositions() {
            return positions;
        } //położenia atomów

        private List<Point3d> positions = new ArrayList<>();

        public void addPosition(Point3d point3d){
            positions.add(point3d);
        }
        public void addPosition(String[] strPos){
            positions.add(new Point3d(strPos));
        }
        public void addPosition(List<String> strPos){
            positions.add(new Point3d(strPos));
        }
    }

    public void setTimeStampCount(int timeStampCount) {
        this.timeStampCount = timeStampCount;
    }

}
