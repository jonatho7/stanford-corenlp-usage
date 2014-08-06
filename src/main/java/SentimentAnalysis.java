import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class SentimentAnalysis {
    public static void main(String[] args) {
        double startTime = System.nanoTime();
        double elapsedTime;

        StanfordCoreNLP pipeline = setupNLPPipelineForSentimentAnalysis();
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("time - StanfordCoreNLP pipeline: " + elapsedTime);




        String inputText =
                "I am brilliantly happy. " +
                "Today is wonderful. " +
                "This movie is awful. " +
                "This airplane is disgusting, gross, and sick. " +
                "The pangs of war are still with us, causing all citizens to continue to live in fear of the occurrence of such atrocities again in the future. " +
                "The recovery of the American economy has been one of the most positive and uplifting events in recent history. " +
                "This movie is wonderful.";

        Double sentiment = calculateSentimentForText(pipeline, inputText);
        System.out.println("sentiment: " + sentiment);
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("time - analyze sentiment: " + elapsedTime);
    }

    private static StanfordCoreNLP setupNLPPipelineForSentimentAnalysis() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        return pipeline;
    }

    private static Double calculateSentimentForText(StanfordCoreNLP pipeline, String text) {
        //Create an empty Annotation just with the given text.
        Annotation document = new Annotation(text);

        //Run all Annotators on this text.
        pipeline.annotate(document);

        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        Double sentiment = calculateSentiment(sentences);
        return sentiment;
    }

    private static Double calculateSentiment(List<CoreMap> sentences) {
        int sentenceCount = 0;
        int totalSentiment = 0;
        for (CoreMap sentence : sentences) {
            Tree sentimentTree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(sentimentTree);
            System.out.println("sent: " + convertSentimentToNewScale(sentiment));

            sentenceCount++;
            totalSentiment += sentiment;
        }
        if (sentenceCount > 0) {
            double averageSentiment = (double) totalSentiment / sentenceCount;
            return convertSentimentToNewScale(averageSentiment);
        } else {
            return null;
        }
    }

    /**
     * Convert sentiment from a 0,1,2,3,4 scale to a -1 to 1 scale.
     * Old scale:
     * 0 = very negative.
     * 2 = neutral.
     * 4 = very positive.
     * New scale:
     * -1 = very negative.
     * 0 = neutral.
     * 1 = very positive.
     * @param oldSentiment
     * @return
     */
    private static double convertSentimentToNewScale(double oldSentiment){
        double newSentiment = (oldSentiment -2) / 2.0;
        return newSentiment;
    }

}
