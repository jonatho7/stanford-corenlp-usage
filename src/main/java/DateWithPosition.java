import java.util.Date;

public class DateWithPosition {
    private final Date date;
    private final String originalText;
    private final Integer charOffsetStart;
    private final Integer charOffsetEnd;

    public DateWithPosition(Date date, String originalText, Integer charOffsetStart, Integer charOffsetEnd) {
        this.date = date;
        this.originalText = originalText;
        this.charOffsetStart = charOffsetStart;
        this.charOffsetEnd = charOffsetEnd;
    }

    public Date getDate(){
        return date;
    }

    public String getOriginalText() {
        return originalText;
    }

    public Integer getCharOffsetStart(){
        return charOffsetStart;
    }

    public Integer getCharOffsetEnd(){
        return charOffsetEnd;
    }

    public String toString(){
        String tempString = "";
        tempString += date.toString();

        //Add the original text.
        tempString += ". " + originalText;

        //Add the character offsets.
        tempString += ". From char " + charOffsetStart +
                " to char " + charOffsetEnd;

        return tempString;
    }

}
