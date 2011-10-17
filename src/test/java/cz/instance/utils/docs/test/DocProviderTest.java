package cz.instance.utils.docs.test;

import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.instance.utils.docs.Document;
import cz.instance.utils.docs.DocumentProvider;


public class DocProviderTest {

	private Map<String, List<Document>> documents;

	@BeforeClass
	public void prepare() throws Exception {
		documents = DocumentProvider.getDocuments();
	}
	
	@Test
	public void test() {
		System.out.println("Hello");
	}
	
}
