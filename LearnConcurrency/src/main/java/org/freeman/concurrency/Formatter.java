/**
 * 
 */
package org.freeman.concurrency;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 *
 */
public class Formatter {
  
  public static void main(String[] args) {
    final DecimalFormat DF = new DecimalFormat("#.######");
    final BigDecimal decimal = new BigDecimal("567.1200000");
    System.out.println(DF.format(decimal));
  }

}
