package analyzers;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.IOException;


public class MyCustomAnalyzer extends Analyzer {

    int searchStringLength = 2;

    /**
     * //TODO: it's been observed that synonym filter and ngram filter creating ba results
     * Especially this entry in synonym--> synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("finished"), true) and
     * When searching for a string fish
     * As the EdgeNGramTokenFilter is using minGram as 2 it matches the token from finished and fish as both are starting with fi
     * <p>
     * <p>
     * So to avoid this i think we can dynamically assign the minGram when ever user types the searchString
     *
     * @param searchStringLength Length of the searchString
     */
    public MyCustomAnalyzer(final int searchStringLength) {
        this.searchStringLength = searchStringLength;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        TokenStream result = new StandardFilter(src);
        result = new LowerCaseFilter(result);

        result = new SynonymGraphFilter(result, getSynonymMap(), false);
        result = new StopFilter(result, StandardAnalyzer.STOP_WORDS_SET);
        result = new PorterStemFilter(result);
        result = new CapitalizationFilter(result);
        int minGram = this.searchStringLength;
        int maxGram = 10;
        result = new EdgeNGramTokenFilter(result, minGram, maxGram, false);

        return new TokenStreamComponents(src, result);
    }


    private SynonymMap getSynonymMap() {
        try {
            SynonymMap.Builder synonymMapBuilder = new SynonymMap.Builder(true);
            synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("finished"), true);
            synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("done"), true);
            synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("success"), true);
            synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("over"), true);
            synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("ended"), true);
            synonymMapBuilder.add(new CharsRef("completed"), new CharsRef("complete"), true);
            return synonymMapBuilder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}