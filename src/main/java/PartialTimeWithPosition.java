/**
 * Immutable object. Follows the Builder model. (See Effective Java - Builder Model).
 */
public class PartialTimeWithPosition {
    private final Integer year;
    private final Integer month;  //1-12.
    private final Integer day;    //1-31.
    private final Integer hour;   //0-23.
    private final Integer min;    //0-59.
    private final Integer sec;    //0-59
    private final Integer millis; //0-999
    private final String originalText;
    private final Integer charOffsetStart;
    private final Integer charOffsetEnd;

    private PartialTimeWithPosition(Builder builder) {
        year = builder.year;
        month = builder.month;
        day = builder.day;
        hour = builder.hour;
        min = builder.min;
        sec = builder.sec;
        millis = builder.millis;
        originalText = builder.originalText;
        charOffsetStart = builder.charOffsetStart;
        charOffsetEnd = builder.charOffsetEnd;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getSec() {
        return sec;
    }

    public Integer getMillis() {
        return millis;
    }

    public String getOriginalText() {
        return originalText;
    }

    public Integer getCharOffsetStart() {
        return charOffsetStart;
    }

    public Integer getCharOffsetEnd() {
        return charOffsetEnd;
    }


    /**
     * TODO. Could remove String concat to make this method faster.
     *
     * @return
     */
    public String toString() {
        String tempString = "";
        if (year != null) {
            tempString += year;
        } else {
            tempString += "XXXX";
        }

        if (month != null) {
            tempString += "-" + month;
        } else {
            tempString += "-XX";
        }

        if (day != null) {
            tempString += "-" + day;
        } else {
            tempString += "-XX";
        }

        tempString += "T";

        if (hour != null) {
            tempString += hour;
        } else {
            tempString += "XX";
        }

        if (min != null) {
            tempString += ":" + min;
        } else {
            tempString += ":" + "XX";
        }

        if (sec != null) {
            tempString += ":" + sec;
        } else {
            tempString += ":" + "XX";
        }

        //Add the original text.
        tempString += ". " + originalText;

        //Add the character offsets.
        tempString += ". From char " + charOffsetStart +
                " to char " + charOffsetEnd;

        return tempString;
    }

    public static class Builder {
        private Integer year = null;
        private Integer month = null;
        private Integer day = null;
        private Integer hour = null;
        private Integer min = null;
        private Integer sec = null;
        private Integer millis = null;
        private String originalText = null;
        private Integer charOffsetStart = null;
        private Integer charOffsetEnd = null;

        public Builder() {

        }

        public PartialTimeWithPosition build() {
            return new PartialTimeWithPosition(this);
        }

        public Builder setYear(Integer year) {
            this.year = year;
            return this;
        }

        public Builder setMonth(Integer month) {
            this.month = month;
            return this;
        }

        public Builder setDay(Integer day) {
            this.day = day;
            return this;
        }

        public Builder setHour(Integer hour) {
            this.hour = hour;
            return this;
        }

        public Builder setMin(Integer min) {
            this.min = min;
            return this;
        }

        public Builder setSec(Integer sec) {
            this.sec = sec;
            return this;
        }

        public Builder setMillis(Integer millis) {
            this.millis = millis;
            return this;
        }

        public Builder setOriginalText(String originalText) {
            this.originalText = originalText;
            return this;
        }

        public Builder setCharOffsetStart(Integer charOffsetStart) {
            this.charOffsetStart = charOffsetStart;
            return this;
        }

        public Builder setCharOffsetEnd(Integer charOffsetEnd) {
            this.charOffsetEnd = charOffsetEnd;
            return this;
        }
    }

}
