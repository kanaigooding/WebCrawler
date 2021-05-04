package webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Link {

  String link;
  ArrayList<Link> children = new ArrayList<>();
  final int depth;
  boolean visited;
  boolean broken;

  public Link(String link, int depth) {
    this.link = link;
    this.depth = depth;
    this.visited = false;
    this.broken = !isValidURL(link);
  }

  private String getHTML() {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link)).GET().build();

    HttpResponse<String> response;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      this.broken = true;
      return "";
    }
    return response.body();
  }

  private ArrayList<String> getLinkChildren() {

    ArrayList<String> result = new ArrayList<>();

    String regex = "href = (https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(getHTML());
    while (m.find())
    {
      result.add(m.group());
    }

    return result;

  }

  public ArrayList<Link> getChildren() {

    if (!children.isEmpty()) {
      return children;
    }

    for (String temp : getLinkChildren()) {
      Link link = new Link(temp, depth + 1);
      children.add(link);
    }
    return children;
  }

  public boolean isValidURL(String url) {

    try {
      new URL(url).toURI();
    } catch (MalformedURLException | URISyntaxException e) {
      return false;
    }

    return true;
  }

}



