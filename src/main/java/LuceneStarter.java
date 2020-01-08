import analyzers.MyCustomAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;

public class LuceneStarter {
    public static void main(String[] args) {
        try {
            String searchString = "fish";

            //index directory
            Directory index = new RAMDirectory();

            //Custom Analyzer using builder pattern for simple usecase.
            Analyzer analyzerUsingBuilder = CustomAnalyzer.builder()
                    .withTokenizer("standard")
                    .addTokenFilter("lowercase")
                    .addTokenFilter("stop")
                    .addTokenFilter("porterstem")
                    .addTokenFilter("capitalization")
                    .addTokenFilter("edgengram")
                    .build();

            //Custom Analyzer for complex usecase if we need to pass constructor args.
            Analyzer analyzer = new MyCustomAnalyzer(searchString.length());
            //Create Index
            IndexWriter indexWriter = new IndexWriter(index, new IndexWriterConfig(analyzer));
            createDocument(indexWriter, "John Snow", 1);
            createDocument(indexWriter, "Tyrian Lannister", 2);
            createDocument(indexWriter, "Jamie Lannister", 6);
            createDocument(indexWriter, "Arya Stark", 3);
            createDocument(indexWriter, "Jack Sparrow", 4);
            createDocument(indexWriter, "Ravi Bikkina", 5);
            createDocument(indexWriter, "Rambo", 5);
            createDocument(indexWriter, "Fishing", 5);
            createDocument(indexWriter, "Fisher", 5);
            createDocument(indexWriter, "Fished", 5);
            createDocument(indexWriter, "iFished", 5);
            createDocument(indexWriter, "iTestFishing", 5);
            createDocument(indexWriter, "FHFished", 5);
            createDocument(indexWriter, "blahblahFISH", 5);
            createDocument(indexWriter, "completed", 5);
            createDocument(indexWriter, "finished", 5);
            createDocument(indexWriter, "done", 5);
            indexWriter.close();

            //Create Index Searcher
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            Query query = new QueryParser("name", new MyCustomAnalyzer(searchString.length())).parse(searchString);
            TopDocs topDocs = indexSearcher.search(query, 10);
            Arrays.stream(topDocs.scoreDocs).map(scoreDoc -> {
                try {
                    return indexSearcher.doc(scoreDoc.doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).forEach(System.out::println);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static Document createDocument(IndexWriter indexWriter, String name, int rank) throws IOException {
        Document document = new Document();
        document.add(new TextField("name", name, Field.Store.YES));
        document.add(new StoredField("rank", rank));
        indexWriter.addDocument(document);
        return document;
    }
}
