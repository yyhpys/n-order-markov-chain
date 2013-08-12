package me.trilby.mm.predict.builtin;

import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;

import me.trilby.mm.MarkovChain;
import me.trilby.mm.predict.Predictor;

public class FixedHighestPredictor extends Predictor {
  
  @Override
  protected int _getNext(MarkovChain mm, int fromIndex) {
    Vector toMatrix = mm.getTransitionMatrix().getRow(fromIndex);
    
    if (toMatrix.is(Vectors.ZERO_VECTOR))
      return -1;
    
    int highestIndex = 0;
    double prob = 0;
    
    for (int i=0; i < toMatrix.length(); i++) {
      if (toMatrix.get(i) > prob) {
        if (!isConstraint(i)) {
          prob = toMatrix.get(i);
          highestIndex = i;
        }
      }
      
      //TODO: implement tie-break
    }
    
    return highestIndex;
  }
  
}