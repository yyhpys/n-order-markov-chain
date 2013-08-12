package be.ac.ulg.montefiore.run.jahmm;

import java.text.NumberFormat;

import be.ac.ulg.montefiore.run.jahmm.Observation;


public class ObservationString extends Observation implements Comparable<String> {

  public final String value;
  
  
  public ObservationString(String value) {
    this.value = value;
  }
  
  
  public String toString() {
    return value;
  }
  
  
  public String toString(NumberFormat nf) {
    return toString();
  }


  @Override
  public int compareTo(String arg0) {        
    return value.compareTo(arg0);
  }
}
