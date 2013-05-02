package biz.finder.ipl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlRetriever {
	public static void main(String[] args) throws Exception {
		String url = "http://www.espncricinfo.com/indian-premier-league-2013/content/series/586733.html?template=iplfixtures;wrappertype=print";
		new UrlRetriever().retrieve(url);
	}

	public String retrieve(String url) throws Exception {
		URL fixtures = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				fixtures.openStream()));
		StringBuilder sb = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine).append("\n");
		in.close();
		return sb.toString();
	}
}
