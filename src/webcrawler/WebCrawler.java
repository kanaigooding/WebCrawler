package webcrawler;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class WebCrawler {

  static int maxDepth;

  public static void main(String[] args) {

    String startLink;

    try {

      startLink = args[0];
      maxDepth = Integer.parseInt(args[1]);

    } catch (ArrayIndexOutOfBoundsException io) {

      startLink = getLink();
      String input = JOptionPane.showInputDialog("Enter Max Depth");
      maxDepth = Integer.parseInt(input);

    }

    Link firstLink = new Link(startLink, 0);

    long startTime = System.currentTimeMillis();
    constructAndClean(firstLink);
    long endTime = System.currentTimeMillis();
    System.out.println("That took " + (endTime - startTime) + " milliseconds");

    writeToFile(BFS(firstLink), "bfsResults.txt");
    writeToFile(DFS(firstLink), "dfsResults.txt");
  }

  private static ArrayList<Link> DFS(Link firstLink) {
    constructAndClean(firstLink);
    Stack<Link> linkStack = new Stack<>();
    ArrayList<Link> listOfLinks = new ArrayList<>();
    long startTime = System.currentTimeMillis();

    Link currentLink;

    linkStack.push(firstLink);

    while (!linkStack.empty() ) {

      currentLink = linkStack.pop();

      listOfLinks.add(currentLink);

      if (!currentLink.visited && currentLink.depth <= maxDepth && !currentLink.broken) {

        currentLink.visited = true;

        for (Link temp : currentLink.getChildren()) {
          if ( temp.depth <= maxDepth ) {
            linkStack.push(temp);
          }
        }
      }
    }
    long endTime = System.currentTimeMillis();
    System.out.println("DFS Done!");
    System.out.println("That took " + (endTime - startTime) + " milliseconds");
    return listOfLinks;
  }

  private static ArrayList<Link> BFS(Link firstLink) {
    constructAndClean(firstLink);
    Queue<Link> linkQueue = new LinkedList<>();
    ArrayList<Link> listOfLinks = new ArrayList<>();
    long startTime = System.currentTimeMillis();

    Link currentLink;

    linkQueue.offer(firstLink);

    while ( linkQueue.peek() != null ) {

      currentLink = linkQueue.poll();

      listOfLinks.add(currentLink);

      if (!currentLink.visited && currentLink.depth <= maxDepth && !currentLink.broken) {

        currentLink.visited = true;

        for (Link temp : currentLink.getChildren()) {
          if ( temp.depth <= maxDepth ) {
            linkQueue.offer(temp);
          }
        }
      }
    }
    long endTime = System.currentTimeMillis();
    System.out.println("BFS Done!");
    System.out.println("That took " + (endTime - startTime) + " milliseconds");
    return listOfLinks;
  }

  private static String getLink() {
    String link;

    link = JOptionPane.showInputDialog("Please enter a valid starting link...");

    while ( !isValidURL(link) || link == null ) {
      link = JOptionPane.showInputDialog("Please enter a VALID starting link...");
    }

    return link;
  }

  private static boolean isValidURL(String url) {

    try {
      new URL(url).toURI();
    } catch (MalformedURLException | URISyntaxException e) {
      return false;
    }

    return true;
  }

  private static void writeToFile(ArrayList<Link> links, String filename) {
    try {
      FileWriter myWriter = new FileWriter(filename);
      for (Link link : links) {
        myWriter.write("Link: " + link.link + "  Depth: " + link.depth + "\n");
      }
      System.out.println("Successfully wrote to file.");
      myWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  private static void printResults(ArrayList<Link> links) {
    System.out.println("Number of Results: " + links.size());
    for (Link link : links ) {
      System.out.println("Link: " + link.link + "  Depth: " + link.depth);
    }
  }

  private static void constructAndClean(Link link) {
    link.visited = false;
    for (Link temp : link.getChildren()) {
      if ( link.visited && link.depth <= maxDepth ) {
        constructAndClean(temp);
      }
    }
  }
}