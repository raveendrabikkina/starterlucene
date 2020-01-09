package demoPack;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello lucene
 * Indexing: add document 
 * */

@SuppressWarnings("deprecation")
public class LuceneTester {
	public static void main(String[] args) throws IOException, ParseException {
		
		//index directory
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory(); 
		
		//create index
		IndexWriter indexWriter = new IndexWriter(index, new IndexWriterConfig(analyzer));  // no need to identify the version in Lucene7
/*In class practice*/
//		addDoc(indexWriter, "Lucene in Action", "1");
//		addDoc(indexWriter, "Creates a new config", "2");
//		addDoc(indexWriter, "Returns the max amount", "3");
//		addDoc(indexWriter, "Information about merges", "4");
		
/*Case Study 1
		SpaceId: 
		Space Name
		Space Country
		City
		State
		Availability
		Category [Premium / Normal]
		Timings [Hourly/ Daily/ Monthly]
		Space Type: [Room/Seat/Gym etch]
		 * 
		 * */
		caseOneField caseOne1 = new caseOneField("11", "name1", "China", "Beijing", "NA", "available", "normal", "Hourly", "Room"); 
		caseOneField caseOne2 = new caseOneField("22", "name2", "US", "NY", "NJ", "available", "Premium", "Hourly", "Room");
		caseOne1.addCase1Doc(indexWriter);
		indexWriter.close(); // index finish
		
		//create query
		Query query = new QueryParser("spaceid", new StandardAnalyzer()).parse("11");
		//create index searcher to search index
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		
		//search
		TopDocs topdoc = indexSearcher.search(query, 10);
		
		ScoreDoc[] scoreDocs = topdoc.scoreDocs;
		System.out.println("found "+ topdoc.totalHits); // return the number of results
		for (ScoreDoc scoreDoc:scoreDocs) {
			Document dis_doc = indexSearcher.doc(scoreDoc.doc);
			System.out.println(dis_doc.get("spacename"));
		}
		
		// scoreDocs is score of each document: this is a (doc_id, doc_score) and use id to fetch the document
		//totalhits refers to total number of results returned
	
		// here you do have some questions with returned results the data structure, list, array
	}
	
	
	public static void addDoc(IndexWriter writer, String title, String rank) throws IOException{
		Document doc = new Document(); 
		doc.add(new TextField("name", title, Field.Store.YES));
		doc.add(new StringField("rank", title, Field.Store.YES));
		writer.addDocument(doc);
		
	}
	
	
	
	
}
