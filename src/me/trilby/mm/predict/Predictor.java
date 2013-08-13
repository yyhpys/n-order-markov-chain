package me.trilby.mm.predict;

import java.util.ArrayList;
import java.util.List;

import me.trilby.mm.MarkovChain;
import be.ac.ulg.montefiore.run.jahmm.Observation;

/**
 *  Abstract class for n-order Markov chain predictor 
 */
public abstract class Predictor {
  public List<Integer> constraintIndex;
  
  protected boolean isConstraint(int index) {
    if (constraintIndex == null)
      return false;
    
    if (constraintIndex.contains(index))
      return true;
    else
      return false;
  }
  
  
  public int getNext(MarkovChain mm, Observation[] currentContext) {
    int fromIndex = mm.getMatrixIndex(currentContext);
    
    constraintIndex = null;
    
    return _getNext(mm, fromIndex);
  }
  
  public int getNext(MarkovChain mm, Observation[] currentContext, Observation[] constraint) {
    int fromIndex = mm.getMatrixIndex(currentContext);
    
    constraintIndex = new ArrayList<Integer>();
    for (int i=0; i < constraint.length; i++)
      constraintIndex.add(mm.getMatrixIndex(constraint[i]));
    
    return _getNext(mm, fromIndex);
  }

  
  public int getNext(MarkovChain mm, Observation[] currentContext, Observation currentInfo) {
    int fromIndex = mm.getMatrixIndex(currentContext, currentInfo);
    
    constraintIndex = null;
    
    return _getNext(mm, fromIndex);
  }
  
  public int getNext(MarkovChain mm, Observation[] currentContext, Observation currentInfo, Observation[] constraint) {
    int fromIndex = mm.getMatrixIndex(currentContext, currentInfo);

    constraintIndex = new ArrayList<Integer>();
    for (int i=0; i < constraint.length; i++)
      constraintIndex.add(mm.getMatrixIndex(constraint[i]));
    
    return _getNext(mm, fromIndex);
  }

  
  protected abstract int _getNext(MarkovChain mm, int fromIndex); //TODO: getNext as simple comparator or rank calculator
}