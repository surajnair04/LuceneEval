package parsers;

import core.CacmDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CacmDocParser implements Parser {

	private BufferedReader reader;
	private List<CacmDocument> documents;

	private static class Fields {

		private static final char PREFIX = '.';
		private static final char AUTHORS = 'A';
		private static final char DATE = 'B';
		private static final char CONTENT = 'C';
		private static final char ID = 'I';
		private static final char KEYWORDS = 'K';
		private static final char ENTRYDATE = 'N';
		private static final char TITLE = 'T';
		private static final char ABSTRACT = 'W';
		private static final char REFERENCE = 'X';
	}

	public CacmDocParser(String docfile) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(new File(docfile)));
		documents = new LinkedList<CacmDocument>();
	}

	@Override
	public void parse() throws IOException {
		CacmDocument document = null;
		String line = "";
		char state = 0;
		while ((line = reader.readLine()) != null) {
			if ((line = line.trim()).isEmpty()) {
				continue;
			}
			if (line.charAt(0) == Fields.PREFIX) {
				state = line.charAt(1);
				if (state == Fields.ID) {
					if (document != null) {
						documents.add(document);
					}
					document = new CacmDocument();
					document.setId(line.substring(2));
				}
			} else {
				switch (state) {
					case Fields.AUTHORS:
						document.addAuthor(line);
						break;
					case Fields.DATE:
						document.addDate(line);
						break;
					case Fields.CONTENT:
						document.addContent(line);
						break;
					case Fields.KEYWORDS:
						document.addKeywords(line);
						break;
					case Fields.ENTRYDATE:
						document.addEntrydate(line);
						break;
					case Fields.TITLE:
						document.addTitle(line);
						break;
					case Fields.ABSTRACT:
						document.addAbstractInfo(line);
						break;
					case Fields.REFERENCE:
						document.addReference(line);
						break;
					/* Fields.ID and no state should never happen */
					case Fields.ID:
					default:
				}
			}
		}
		reader.close();
	}

	private String parseBlock(StringBuilder text) throws IOException {
		String line = "";
		while ((line = reader.readLine()) != null && !line.startsWith(".")) {
			text.append(line).append(" ");
		}
		return line;
	}

	public List<CacmDocument> getDocuments() {
		return documents;
	}
}