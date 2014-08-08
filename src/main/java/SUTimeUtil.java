import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SUTimeUtil {

    public static StanfordCoreNLP setupNLPPipelineForSUTime() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
        return pipeline;
    }


    public static ArrayList<Object> getTimeAnnotations(StanfordCoreNLP pipeline, String text) {
        //Create an empty Annotation just with the given text.
        Annotation document = new Annotation(text);

        ////Set the document's "today" date. Only use this if this is known for sure.
        //document.set(CoreAnnotations.DocDateAnnotation.class, "2013-07-14");

        //Run all Annotators on this text.
        pipeline.annotate(document);

        System.out.println(document.get(CoreAnnotations.TextAnnotation.class));

        ArrayList<Object> extractedTimes = new ArrayList<Object>();

        //A CoreMap is a Map that uses class objects as keys and has values with custom types.
        List<CoreMap> timexAnnotations = document.get(TimeAnnotations.TimexAnnotations.class);
        for (CoreMap cm : timexAnnotations) {
            // a CoreLabel is a CoreMap with additional token-specific methods
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            System.out.println(cm +
                    " [from char offset " + tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
                    " to " + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
                    " --> " + cm.get(TimeExpression.Annotation.class).getTemporal());

            Integer charOffsetStart = tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
            Integer charOffsetEnd = tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class);

            SUTime.Temporal temporal = cm.get(TimeExpression.Annotation.class).getTemporal();
            System.out.println("b.getTimexType - SUTime.TimexType: " + temporal.getTimexType());
            System.out.println("b.getTime() - SUTime.Time: " + temporal.getTime() );
            System.out.println();

            if (temporal.getTimexType().toString().equals("TIME") || temporal.getTimexType().toString().equals("DATE") ){
                SUTime.Time time = temporal.getTime();
                String dateString = time.toString();
                Date date = parseStringForDate(dateString);
                if (date != null) {
                    DateWithPosition extractedDate = new DateWithPosition(date, charOffsetStart, charOffsetEnd);
                    extractedTimes.add(extractedDate);
                    continue;
                }

                PartialTimeWithPosition partialTimeWithPosition = customParse(dateString);
                if (partialTimeWithPosition != null) {
                    extractedTimes.add(partialTimeWithPosition);
                    continue;
                }

            }


        }
        return extractedTimes;
    }

    private static Date parseStringForDate(String dateString) {
        //Since there is no way to know the TimeZone of the document, will assume the time zone of the end-user.

        String[] dateFormats = new String[]{    "yyyy-MM-dd'T'HH:mm",
                                                "yyyy-MM-dd"};

        for (int i = 0; i < dateFormats.length; i++) {
            Date date = attemptParse(dateString, dateFormats[i]);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

    /**
     * An attempt to parse the dateString. Will return null if it cannot be parsed.
     * @param dateString
     * @param dateFormat
     * @return
     */
    private static Date attemptParse(String dateString, String dateFormat){
        try {
            SimpleDateFormat format;
            if (dateFormat != null){
                format = new SimpleDateFormat(dateFormat);
            } else {
                format = new SimpleDateFormat();
            }
            Date parsedDate = format.parse(dateString);
            return parsedDate;
        } catch (ParseException e) {
            ;
        }

        return null;
    }

    private static PartialTimeWithPosition customParse(String dateString){
        //Temp fix for now.
        dateString = "1997-XX-XX";
        Integer year = Integer.parseInt(dateString.substring(0, 4));
        PartialTimeWithPosition time = new PartialTimeWithPosition.Builder().setYear(year).build();
        return time;
    }





}
