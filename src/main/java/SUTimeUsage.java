import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;

public class SUTimeUsage {
    public static void main(String[] args) {
        StanfordCoreNLP pipeline = SUTimeUtil.setupNLPPipelineForSUTime();
        String inputText =  "My brother was born on 18 Feb 1997 2:45PM. " +
                            "My sister was born on 18 Feb 1997. " +
                            "I was born in 1997. " +
                            "You were born on 18 Feb. ";

        ArrayList<Object> extractedTimes =  SUTimeUtil.getTimeAnnotations(pipeline, inputText);

        for(int i = 0; i < extractedTimes.size(); i++){
            Object temp = extractedTimes.get(i);
            if (temp.getClass() == DateWithPosition.class) {
                DateWithPosition dateWithPosition = (DateWithPosition) temp;
                System.out.println("date " + i + ": " + dateWithPosition.toString() );
            } else if (temp.getClass() == PartialTimeWithPosition.class){
                PartialTimeWithPosition partialTime = (PartialTimeWithPosition) temp;
                System.out.println("date " + i + ": " + partialTime.toString());
            }
        }

    }

}
