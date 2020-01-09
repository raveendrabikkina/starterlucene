package demoPack;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

public class caseOneField {
	/**
	 * The data structure should be like: therefore you need to index multiple fields
	 * doc
	 * 	SpaceId: 
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
	String spaceid; 
	String spacename;
	String spacecountry; 
	String spacecity; 
	String state; 
	String avai;
	String spacetype;
	String categ; 
	String timing;

	public caseOneField(String spaceid, 
						String spacename, 
						String spacecountry, 
						String spacecity, 
						String state, 
						String avai,
						String categ, 
						String timing,
						String spacetype) {
		
		this.spaceid = spaceid; 
		this.spacename = spacename;
		this.spacecountry = spacecountry;
		this.spacecity=spacecity;
		this.state=state;
		this.avai=avai;
		this.spacetype=spacetype;
		this.categ=categ;
		this.timing=timing;
	}
	
	
	//this adddoc is specifically applied for case 1 therefore set as a class method
	
	public void addCase1Doc(IndexWriter writer) throws IOException{
		
		Document doc = new Document(); 
		doc.add(new TextField("spaceid", this.spaceid, Field.Store.YES));
		doc.add(new TextField("country", this.spacecountry, Field.Store.YES));
		doc.add(new TextField("city", this.spacecity, Field.Store.YES));
		doc.add(new TextField("state", this.state, Field.Store.YES));
		doc.add(new TextField("spacename", this.spacename, Field.Store.YES));
		doc.add(new StringField("spacetype", this.spacetype, Field.Store.YES));
		doc.add(new StringField("timing", this.timing, Field.Store.YES));
		doc.add(new StringField("category", this.categ, Field.Store.YES));
		doc.add(new StringField("availability", this.avai, Field.Store.YES));
		writer.addDocument(doc);
	}
	
	
	

}
