package me.trilby.mm.predict.builtin;

import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;

import me.trilby.mm.MarkovChain;
import me.trilby.mm.predict.Predictor;

public class RandomHighestPredictor extends Predictor {

  @Override
  protected int _getNext(MarkovChain mm, int fromIndex) {
    Vector toMatrix = mm.getTransitionMatrix().getRow(fromIndex);
    
    if (toMatrix.is(Vectors.ZERO_VECTOR))
      return -1;
    
    double randProb = Math.random();
    
    int highestIndex = 0;
    double integratedProb = 0;    
    for (int i=0; i < toMatrix.length(); i++) {
      integratedProb += toMatrix.get(i);
      
      if (integratedProb > randProb) {
        if (!isConstraint(i)) {
          highestIndex = i;
          break;
        }
      }
    }
    
    return highestIndex;
  }
  
}