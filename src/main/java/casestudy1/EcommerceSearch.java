package casestudy1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;

public class EcommerceSearch {
    public static void main(String[] args) {
        //index directory
        try (Directory index = new RAMDirectory();
             Analyzer analyzer = new StandardAnalyzer();
             IndexWriter indexWriter = new IndexWriter(index, new IndexWriterConfig())) {
            //Create Index
            createDocument(indexWriter, "1", "Wynyard", "AUS", "Sydney", "NSW", "Available", "Rent", "9AM to 8PM", "Apartment", 1);
            createDocument(indexWriter, "2", "Kogarah", "AUS", "Kogarah", "NSW", "Available", "Buy", "11AM to 4PM", "Individual House", 2);
            createDocument(indexWriter, "3", "Winterfell", "US", "Winterfell", "NYC", "Not Available", "Rent", "10AM to 5PM", "Studio", 3);
            indexWriter.close();
            //Create Index Searcher
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            Query query = getQueryByField("popularity", "1", analyzer);

            TopDocs topDocs = indexSearcher.search(query, 10);
            System.out.println("Total Results:" + topDocs.scoreDocs.length);
            Arrays.stream(topDocs.scoreDocs).map(scoreDoc -> {
                try {
                    return indexSearcher.doc(scoreDoc.doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document createDocument(IndexWriter indexWriter, String spaceId, String spaceName, String spaceCountry, String city, String state, String availability, String category, String timings, String spaceType, int popularity) throws IOException {
        Document document = new Document();
        /*
            SpaceId
            Space Name
            Space Country
            City
            State
            Availability
            Category
            Timings
            Space Type
         */
        document.add(new StringField("spaceId", spaceId, Field.Store.YES));
        document.add(new TextField("popularity", String.valueOf(popularity), Field.Store.YES));
        document.add(new StoredField("rank", popularity));
        document.add(new TextField("name", spaceName, Field.Store.YES));
        document.add(new StringField("spaceCountry", spaceCountry, Field.Store.YES));
        document.add(new StringField("city", city, Field.Store.YES));
        document.add(new StringField("state", state, Field.Store.YES));
        document.add(new StringField("availability", availability, Field.Store.YES));
        document.add(new StringField("category", category, Field.Store.YES));
        //TODO: select right FieldType
        document.add(new StringField("timings", timings, Field.Store.YES));
        document.add(new StringField("spaceType", spaceType, Field.Store.YES));
        indexWriter.addDocument(document);
        return document;
    }

    private static Query getQueryByField(String field, String value, Analyzer analyzer) {
        try {
            switch (field) {
                case "spaceId":
                case "category":
                case "popularity":
                case "rank":
                case "spaceCountry":
                case "city":
                case "state":
                case "availability":
                case "timings":
                case "spaceType":
                    return new PhraseQuery(field, value);
                case "spaceName":
                    return new QueryParser("name", analyzer).parse(value);
                default:
                    throw new IllegalStateException("Unexpected value: " + field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
