import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.time.Timex;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.TypesafeMap;
import edu.stanford.nlp.time.SUTime.Temporal;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class SUTime {
    public static void main(String[] args) {
        double startTime = System.nanoTime();
        double elapsedTime;

        StanfordCoreNLP pipeline = setupNLPPipelineForSUTime();
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("time - StanfordCoreNLP pipeline: " + elapsedTime);

        String inputText =
                "I am brilliantly happy. " +
                        "Today is wonderful. " +
                        "This movie is awful. " +
                        "This airplane is disgusting, gross, and sick. " +
                        "The pangs of war are still with us, causing all citizens to continue to live in fear of the occurrence of such atrocities again in the future. " +
                        "The recovery of the American economy has been one of the most positive and uplifting events in recent history. " +
                        "This movie is wonderful." +
                        "Three interesting dates are 18 Feb 1997, the 20th of july and 4 days from today." +
                        "I will be working from 5:00PM to 9:00PM tomorrow." +
                        "You are going there on Tuesday 1-2pm." +
                        "I am free on Wednesday night";

        analyzeTimeAnnotations(pipeline, inputText);
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("time - analyze sentiment: " + elapsedTime);

    }

    private static StanfordCoreNLP setupNLPPipelineForSUTime() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
        return pipeline;
    }

    public static void analyzeTimeAnnotations(StanfordCoreNLP pipeline, String text) {
        //Create an empty Annotation just with the given text.
        Annotation document = new Annotation(text);

        ////Set the document's "today" date. Only use this if this is known for sure.
        //document.set(CoreAnnotations.DocDateAnnotation.class, "2013-07-14");

        //Run all Annotators on this text.
        pipeline.annotate(document);

        System.out.println(document.get(CoreAnnotations.TextAnnotation.class));

        //A CoreMap is a Map that uses class objects as keys and has values with custom types.
        List<CoreMap> timexAnnotations = document.get(TimeAnnotations.TimexAnnotations.class);
        for (CoreMap cm : timexAnnotations) {
            // a CoreLabel is a CoreMap with additional token-specific methods
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            System.out.println(cm +
                    " [from char offset " + tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
                    " to " + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
                    " --> " + cm.get(TimeExpression.Annotation.class).getTemporal());

            //Timex c = cm.get(TimeAnnotations.TimexAnnotation.class);
            //System.out.println("c.timexType() - String: " + c.timexType() );

            TimeExpression a = cm.get(TimeExpression.Annotation.class);
            Temporal b = a.getTemporal();
            System.out.println("b.getTimexType - SUTime.TimexType: " + b.getTimexType() );
            System.out.println("b.getTimexValue - String: " + b.getTimexValue() );
            System.out.println("b.getTime() - SUTime.Time: " + b.getTime() );
            System.out.println("b.getDuration(): - SUTime.Duration " + b.getDuration() );
            System.out.println("b.getPeriod() - SUTime.Duration : " + b.getPeriod() );
            System.out.println("b.getRange() - SUTime.Range: " + b.getRange());

            System.out.println("token thing:" + tokens);
            System.out.println();
        }
        System.out.println("--");
    }
}
