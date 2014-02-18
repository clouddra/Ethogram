package animal.behavior.ethogram;

/**
 * Created by pohchiat on 18/2/14.
 */
public class Entry {
    private long id;
    private long startTime;
    private long endTime;
    private long timeTaken;
    private String behavior;

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

    public String toString(){
        return id + "," + String.valueOf(startTime) + "," + String.valueOf(endTime) + "," + String.valueOf(timeTaken) + "," + behavior + "\n";
    }
}
