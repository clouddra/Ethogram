package animal.behavior.ethogram;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pohchiat on 18/2/14.
 */
public class Entry {
    private long id;
    private long startTime;
    private long endTime;
    private long timeTaken;
    private String behavior;
    private String note;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getStartTime(){
        return startTime;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public long getEndTime(){
        return endTime;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
    }

    public long getTimeTaken(){
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken){
        this.timeTaken = timeTaken;
    }

    public String getBehavior(){
        return behavior;
    }

    public void setBehavior(String behavior){
        this.behavior = behavior;
    }

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public String toString(){
        return id + "," + String.valueOf(startTime) + "," + String.valueOf(endTime) + "," + String.valueOf(timeTaken) + "," + behavior + "," + note + "\n";
    }

    public String getTime(){
        return unixToDate(startTime) + " " + unixConvertTo12Hours(startTime) + "-" + unixConvertTo12Hours(endTime) + " (" + String.valueOf(timeTaken) + "s)";
    }

    public Entry(){
        id = startTime = endTime = timeTaken = 0;
        behavior = "";

    }

    private String unixConvertTo12Hours(long unixTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unixTime*1000L);

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");

        String time = sdf.format(c.getTime());

        return time;
    }

    private String unixToDate(long unixTime){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unixTime*1000L);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM");

        String date = sdf.format(c.getTime());

        return date;
    }
}
