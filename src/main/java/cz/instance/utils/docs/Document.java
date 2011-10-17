package cz.instance.utils.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.tika.mime.MediaType;

public class Document {

	private long id;
	private long size;
	private long checksum;
	private String type;
	private String sample;
	private File sampleFile;
	private String url;
	private String state;
	private File file;
	private MediaType mediaType;
	private int wordCount;
	private String content;
	private List<String> words;
	private List<String> sampleWords;
	private int sampleWordCount;

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public List<String> getSampleWords() {
		return sampleWords;
	}

	public void setSampleWords(List<String> sampleWords) {
		this.sampleWords = sampleWords;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}

	public long getChecksum() {
		return checksum;
	}

	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}

	public int getSampleWordCount() {
		return sampleWordCount;
	}
	
	public void setSampleWordCount(int sampleWordCount) {
		this.sampleWordCount = sampleWordCount;
	}

	public File getSampleFile() {
		return sampleFile;
	}

	public void setSampleFile(File sampleFile) {
		this.sampleFile = sampleFile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Document(long id, String url, String state) {
		this.id = id;
		this.url = url;
		this.state = state;
	}
	
	public String getMediaTypeString() {
		return mediaType.toString();
	}
	
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

}
