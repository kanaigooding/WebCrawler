package webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaWebCrawler {

  public static HashMap<String, Node> graph = new HashMap<>();

  public static void main(String[] args) {

    int maxdepth = 1;

    try {

      Node node = new Node("http://courses.ics.hawaii.edu/", 0);
      DFS(node, maxdepth);
      reset();
      BFS(node,maxdepth);


    } catch (IOException o ) {

      System.out.println("Error : invalid web address");

    }

  }

  private static void DFS(Node n, int maxDepth) {
    Stack<Node> theStack = new Stack<>();
    theStack.push(n);
    Node currentNode;
    System.out.println("DFS RUN ==================== ");

    while (!theStack.empty() ) {

      currentNode = theStack.pop();

      if (!currentNode.isVisited && currentNode.depth <= maxDepth ) {

        currentNode.isVisited = true;
        graph.put(currentNode.web, currentNode);
        System.out.println("Visited: " + currentNode.web + " Depth: " + currentNode.depth );

        for (Node temp : currentNode.getChildren()) {
          if ( temp.depth <= maxDepth ) {
            theStack.push(temp);
          }
        }
      }
    }
  }

  private static void BFS(Node n, int maxDepth) {
    Deque<Node> theQueue = new LinkedList<>();
    theQueue.offer(n);
    Node currentNode;
    System.out.println("BFS RUN ==================== ");

    while (!theQueue.isEmpty()) {

      currentNode = theQueue.poll();

      if (!currentNode.isVisited && currentNode.depth <= maxDepth ) {

        currentNode.isVisited = true;
        graph.put(currentNode.web, currentNode);
        System.out.println("Visited: " + currentNode.web + " Depth: " + currentNode.depth );

        for (Node temp : currentNode.getChildren()) {
          if ( temp.depth <= maxDepth ) {
            theQueue.offer(temp);
          }
        }
      }
    }
  }

  private static void reset() {
    for (Node node : graph.values() ) {
      node.isVisited = false;
    }
  }

  private static class Node {

    ArrayList<Node> childLinks;
    String web; //string of associated website
    public boolean isVisited; //true if website has been visited
    public int depth;

    Node(String url, int depth) throws IOException {

      if ( !validateLink(url) && !tryConnection(url) ) {
        throw new IOException();
      }
      this.web = url;
      this.depth = depth;
    }

    private static boolean validateLink(String website) {

      try {
        String regex = "((http|https)://)(www.)[a-z"
            + "A-Z0-9@:%._\\+~#?&//=]{2,256}\\.(com|edu|mil|gov|org)"
            + "\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";

        Pattern pattern = Pattern.compile(regex);

        Matcher websiteMatch = pattern.matcher(website);

        return websiteMatch.matches();

      } catch (NumberFormatException e) {
        return false;
      }

    }

    private static boolean tryConnection(String url) {

      try {
        new URL(url).toURI();
      } catch (MalformedURLException | URISyntaxException e) {
        return false;
      }

      return true;
    }

    private String getWebsite(String url) throws IOException, InterruptedException {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    }

    private ArrayList<String> getLinks(String website) {

      // https://stackoverflow.com/questions/5120171/extract-links-from-a-web-page

      ArrayList<String> result = new ArrayList<>();

      String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(website);
      while (m.find()) {
        result.add(m.group());
      }

      return result;

    }

    private ArrayList<Node> getChildren() {

      if (childLinks != null) {
        return childLinks;
      }

      ArrayList<Node> listOfChildren = new ArrayList<>();

      try {
        ArrayList<String> listOfLinks = getLinks(getWebsite(web));
        for (String link : listOfLinks) {
          try {
            Node child = new Node(link, depth + 1);
            listOfChildren.add(child);
          } catch (IOException e) {
            // dont do anything
          }
        }
      } catch (IOException | InterruptedException e) {
        // dont do anything
      }
      childLinks = listOfChildren;
      return listOfChildren;
    }

  }

}
