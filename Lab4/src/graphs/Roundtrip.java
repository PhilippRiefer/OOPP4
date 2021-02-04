package graphs;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class Roundtrip {
  private GraphWithWeightedEdges distances;
  private JTextArea textArea;
  
  protected static int minsum = -1;     // minimum travel distance
  protected static long minindex = -1;  // index of the shortest travel found so far 

  
  public Roundtrip(GraphWithWeightedEdges distances, JTextArea textArea) {
    super();
    this.distances = distances;
    this.textArea = textArea;
  } 
 
  /**
   * Computes the distance between two cities. 
   * 
   * <p>This method computes the distance between two cities
   * given as indices.
   * @param i index of 1st city
   * @param j index of 2nd city (order does not matter!)
   * @return distance between cities 
   */
  protected int distance(int i, int j) {
    return distances.getWeight(i,j);
  }
  
  /**
   * Generate the k-th permutation of {0,...,(n-1)}.
   * 
   * <p>This method computes a permutation of number from 0..(n-1). 
   * The permutation is given by an index k which may range
   * from 0 .. n!-1
   * @param n number of elements
   * @param k index of the permutation
   * @return array containing the k-th permutation
   */
  protected static int[] permutation(int n, long k) {

    // first build result array, initialised with -1
    int[] result = new int[n]; 
    for (int i = 0; i < n; i++) {
      result[i] = -1;
    }

    // filling the array
    for (int i = 0; i < n; i++) {
      // (n-i) is the number of options for the number i
      int hits = (int) (k % (n - i)); // how many -1 to find
      int pos = 0;                    // position we check
      while (hits > 0 || result[pos] != -1) { // not finished yet 
        if (result[pos] == -1) {
          hits--; // hit a -1
        }
        pos++;                       // move ahead
      } 
      // now we know: hits==0 && result[pos]==-1
      result[pos] = i; // fill the hole
      k /= n - i;      // divide by the number of options
                       // for number i
    }
    return result;
  }
  
  /**
   * Computes the faculty(n) = 1*2*...*n.
   * @param n the value to compute the faculty for
   */
  protected static long faculty(int n) {
    long result = 1;
    int i = 1;

    while (i <= n) { 
      result *= i;
      i++;
    }
    return result;
  }
  
  /**
   * Add a string to the output.
   * @param msg the message to add to the output
   */
  protected void addOutput(String msg) {
    // output to console
    System.out.println(msg);  
    // and to textArea
    textArea.setText(textArea.getText() + msg + "\n");
  }
  
  // output the tour with distances
  /**
   * Prints the tour with index k for n cities.
   * @param n number of cities to visit
   * @param k index of round trip
   */
  protected void printTour(int n, long k) {
    int[] perm = permutation(n,k);
    int sum = 0;

    addOutput("Tour " + k);
    for (int i = 0; i < n; i++) {
      int j = (i + 1) % n; // next city. Last one is the first one
      sum += distance(perm[i],perm[j]);
      addOutput(distances.getName(perm[i]));
      addOutput("      KM " + distance(perm[i],perm[j]));
    }
    addOutput(distances.getName(perm[0]));
    addOutput("Gesamtstrecke : " + sum);
  }

  /**
   * Compute the shortest round trip for n cities.
   * @param n the number of cities to visit
   * @param go 
   */
  public void compute(int n, JButton go) {

    go.setEnabled(false);
    addOutput("");
    addOutput("=================================================");
    addOutput("Start der Berechung für " + n + " Städte");

    long end = faculty(n); // how many tours exist
    final long starttime = System.currentTimeMillis(); // start timing

    minsum = -1;
    minindex = -1;

    for (long k = 0; k < end; k++) {  // check all trips
      // it shall be a trip starting in city[0] 
      if (k % n == 0) {  
        int[] perm = permutation(n,k);
        int sum = 0;

        for (int i = 0; i < n; i++) { // for all cities visited
          int j = (i + 1) % n;        // j is next city. Last one is the first one
          sum += distance(perm[i],perm[j]); // summing up the distance from i to j
        }
        // now check whether this trip is shorter than current minimum
        if (minsum > sum || minsum == -1) { // shorter OR first trip analysed
          minsum = sum; // remember the travel distance
          minindex = k; // remember the index
        }
      }
    }
    // stop timing 
    long duration = System.currentTimeMillis() - starttime;

    // print result
    printTour(n,minindex);
    addOutput("Dauer der Berechnung in Sekunden : " + duration / 1000.0);
    addOutput("Anzahl der verschiedenen Touren: " + end / n);
    go.setEnabled(true);
  }

}
