import java.util.Date;

public class DateWithPosition {
    private Date date;
    private Integer charOffsetStart;
    private Integer charOffsetEnd;

    public DateWithPosition(Date date, Integer charOffsetStart, Integer charOffsetEnd) {
        this.date = date;
        this.charOffsetStart = charOffsetStart;
        this.charOffsetEnd = charOffsetEnd;
    }

    public Date getDate(){
        return date;
    }

    public Integer getCharOffsetStart(){
        return charOffsetStart;
    }

    public Integer getCharOffsetEnd(){
        return charOffsetEnd;
    }
}
