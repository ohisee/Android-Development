/**
 * 
 */
package org.freeman.concurrency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is to extract view names from file.
 *
 */
public class ExtractViews {

  public static void main(String[] args) {
    try {
      Pattern pattern = Pattern.compile(".*((?i)abc\\.\\w+)");
      File file = new File(args[0]);
      FileReader reader = new FileReader(file);
      BufferedReader br = new BufferedReader(reader);
      String str = null;
      while ((str = br.readLine()) != null) {
        if (str.length() > 0) {
          Matcher matcher = pattern.matcher(str);
          if (matcher.find()) {
            System.out.println(matcher.group(1));
          }
        }
      }
      br.close();
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

}
