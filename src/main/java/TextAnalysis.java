import com.surmize.textalytics.AnalyzedDocument;
import com.surmize.textalytics.TextAnalyzer;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;


/**
 * @author Christopher Manning
 */
public class TextAnalysis {

    private TextAnalysis() {
    }

    /**
     * Example usage:
     * java SUTimeDemo "Three interesting dates are 18 Feb 1997, the 20th of july and 4 days from today."
     *
     * @param args Strings to interpret
     */
    public static void main(String[] args) {
        /*
        Properties props = new Properties();
        AnnotationPipeline pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(new PTBTokenizerAnnotator(false));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
        pipeline.addAnnotator(new POSTaggerAnnotator(false));
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
*/

        Properties props = new Properties();
        //props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
        //pipeline.addAnnotator(new SentimentAnnotator("sentimentAnnotator", props));


        //String inputText = "Three interesting dates are 18 Feb 1997, the 20th of july and 4 days from today.";
        String inputText = "I am brilliantly happy. Today is wonderful. This movie is awful. This airplane is disgusting, gross, and sick. The pangs of war are still with us, causing all citizens to continue to live in fear of the occurrence of such atrocities again in the future. The recovery of the American economy has been one of the most positive and uplifting events in recent history.";

        ////Create an empty Annotation just with the given text.
        //Annotation document = new Annotation(inputText);
        ////Set the document's "today" date. If not available, should default to the end-user's today date.
        //document.set(CoreAnnotations.DocDateAnnotation.class, "2013-07-14");
        ////Run all Annotators on this text.
        //pipeline.annotate(document);


        TextAnalyzer textAnalyzer = new TextAnalyzer();
        AnalyzedDocument analyzedDocument = textAnalyzer.analyze(inputText);
        System.out.println("analyzedDocument sentiment: " + analyzedDocument.getSentiment());

        //getTimeAnnotations(document);
        //analyzeSentences(document);

    }


    public static void analyzeTimeAnnotations(Annotation document) {

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
        }
        System.out.println("--");
    }


    public static void analyzeSentences(Annotation document) {
        //Sentences...
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                System.out.println("word: " + word);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                System.out.println("pos: " + pos);
                // this is the NER label of the token
                String ner = token.get(NamedEntityTagAnnotation.class);
                System.out.println("ner: " + ner);
                System.out.println();
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeAnnotation.class);
            System.out.println("tree: " + tree);

            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println("dependencies: " + dependencies);
        }
    }

}
