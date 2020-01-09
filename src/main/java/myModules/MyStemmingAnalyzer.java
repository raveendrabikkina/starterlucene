package myModules;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
/*
public class MyStemmingAnalyzer extends Analyzer{

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		// TODO Auto-generated method stub
		Tokenizer source = new LowerCaseTokenizer(reader); // this method is the combination of tokenizer and filter
		return new Analyzer.TokenStreamComponents(source, new PorterStemFilter(source)); 
	}

}
*/