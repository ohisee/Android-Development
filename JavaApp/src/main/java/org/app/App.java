package org.app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import static java.util.AbstractMap.SimpleEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class App {
  private static final Map<String, List<String>> LOOKUP = new HashMap<>();
  private static final Map<String, Map<Integer, Map<String, Integer>>> CONTENTS = new HashMap<>();

  /**
   * Constructor
   *
   * @param folder file filer
   * @throws IOException file io exception
   */
  public App(File folder) throws IOException {
    init(folder);
  }

  /**
   * Initialize by reading files' content into a lookup map
   *
   * @param folder folder contains files
   * @throws IOException file io exception
   */
  private void init(File folder) throws IOException {
    for (File file : folder.listFiles()) {
      Map<Integer, Map<String, Integer>> fileContents = new HashMap<>();
      try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
          int ln = reader.getLineNumber();
          Map<String, Integer> lineContent = new HashMap<>();
          String[] contents = line.split("\\s+");
          for (String word : contents) {
            lineContent.put(word, line.indexOf(word));
          }
          fileContents.put(ln, lineContent);
        }
      }
      CONTENTS.put(file.getName(), fileContents);
    }
  }

  /**
   * Find the word in file contents
   *
   * @param word         search this word
   * @param fileContents file contents
   * @return pair of line number and index or null if not found
   */
  private SimpleEntry<Integer, Integer> findWord(
          String word,
          Map<Integer, Map<String, Integer>> fileContents) {
    for (Map.Entry<Integer, Map<String, Integer>> entry : fileContents.entrySet()) {
      Integer index = entry.getValue().get(word);
      if (index != null) {
        return new SimpleEntry<>(entry.getKey(), index);
      }
    }
    return null;
  }

  /**
   * Check whether a file contains an array of words in its order
   *
   * @param searchWords  an array of words for searching
   * @param fileContents file contents
   * @return true if an array of words is found, for example, <abc, xyz> is found
   */
  private boolean containSearchQuery(
          String[] searchWords,
          Map<Integer, Map<String, Integer>> fileContents) {
    int index = 0;
    int lineNumber = 1;
    for (String word : searchWords) {
      SimpleEntry<Integer, Integer> result = findWord(word, fileContents);
      if (result == null) {
        return false;
      } else {
        int returnedLineNumber = result.getKey();
        int returnedIndex = result.getValue();
        if (lineNumber == returnedLineNumber) {
          if (index <= returnedIndex) {
            index = returnedIndex;
          } else {
            return false;
          }
        } else if (lineNumber < returnedLineNumber) {
          lineNumber = returnedLineNumber;
          index = 0;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Find file name of file that contains search query
   *
   * @param searchQuery string containing word or words for searching
   * @return list of file names containing these words or null if not found
   */
  private List<String> searchWords(String searchQuery) {
    String[] searchWords = searchQuery.split("\\s+");
    List<String> result = new ArrayList<>();
    CONTENTS.forEach((filename, contents) -> {
      if (containSearchQuery(searchWords, contents)) {
        result.add(filename);
      }
    });
    return result.isEmpty() ? null : result;
  }

  /**
   * Search words to find file name of file that contains these words
   *
   * @param args string containing word or words for searching
   * @return list of file names containing search words
   * or null if args parameter is blank
   */
  public List<String> findFileNames(String args) {
    if (args == null || args.isEmpty()) {
      return null;
    } else {
      return LOOKUP.computeIfAbsent(args, this::searchWords);
    }
  }
}
