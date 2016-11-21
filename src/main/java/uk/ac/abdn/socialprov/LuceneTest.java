package uk.ac.abdn.socialprov;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import opennlp.tools.ngram.NGramGenerator;

public class LuceneTest {

	public static void main(String[] args)
			throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
		LuceneTest lt = new LuceneTest();
		lt.displayIndex();
		lt.createIndex();
		String text = FileUtils.readFileToString(new File("resources/papers/test.txt"));
		NGramGenerator generator = new NGramGenerator();
		List<String> t = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(text);
		while (st.hasMoreTokens()) {
			t.add(st.nextToken());
		}

		List<String> ngrams = generator.generate(t, 2, " ");
		System.out.println(ngrams);
		lt.searchIndex(ngrams);
	}

	private static final String INDEX_DIRECTORY = "resources/lucence/index/";
	private String FILES_TO_INDEX_DIRECTORY = "resources/ncrm";
	private IndexWriter writer;
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

	public LuceneTest() {

	}

	public void searchIndex(List<String> ngrams) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_DIRECTORY)));
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);

		for (String s : ngrams) {
			Query q = new QueryParser(Version.LUCENE_46, "contents", analyzer).parse(s);

			// PhraseQuery phrase = new PhraseQuery();
			// phrase.add(new Term("textfield", s));
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// 4. display results
			if (hits.length > 0) {
				System.out.println(s + " found " + hits.length + " hits.");
			}
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
//				System.out.println(searcher.explain(q, i));
			}
		}
	}

	public void displayIndex() throws IOException {
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_DIRECTORY)));
		Fields fields = MultiFields.getFields(reader);
		for (String field : fields) {
			Terms terms = fields.terms(field);
			TermsEnum termsEnum = terms.iterator(null);
			int count = 0;
			BytesRef ref;
			while ((ref = termsEnum.next()) != null) {
				System.out.println(ref.utf8ToString());
				count++;
			}
			System.out.println(count);
		}
	}

	public void createIndex() throws CorruptIndexException, LockObtainFailedException, IOException {

		writer = new IndexWriter(FSDirectory.open(new File(INDEX_DIRECTORY)),
				new IndexWriterConfig(Version.LUCENE_46, analyzer));

		File dir = new File(FILES_TO_INDEX_DIRECTORY);
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".txt");
			}
		});
		for (File file : files) {
			Document document = new Document();
			FileReader fr = new FileReader(file);

			document.add(new TextField("contents", fr));
			document.add(new StringField("path", file.getPath(), Field.Store.YES));
			document.add(new StringField("filename", file.getName(), Field.Store.YES));
			writer.addDocument(document);

		}
		writer.close();
	}

}
