package me.trilby.mm;

import java.util.Arrays;
import org.la4j.matrix.*;
import org.la4j.vector.*;

import me.trilby.mm.exception.MalformedModelException;
import me.trilby.mm.learner.PriorLearner;

import be.ac.ulg.montefiore.run.jahmm.Observation;


public class MMLearner {
  
  /**
   * Markov Model Learner Implementation. Learn transition matrix with MLE.
   * 
   * @param prior
   * 
   */

  PriorLearner priorLearner;

  private Matrix tMatrix;
  
  //Constructor
  public MMLearner() {  }
  
  public MMLearner(Class<? extends PriorLearner> priorLearner) {  
    setPriorLearner(priorLearner);
  }

  
  //Method
  public void setPriorLearner(Class<? extends PriorLearner> priorLearner) {
    try {
      this.priorLearner = priorLearner.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
  
  public boolean isPriorLearnerAvailable() {
    return priorLearner != null;    
  }
  
  /**
   * Learn through single observation list
   * 
   * @param mm
   * @param o
   */
  public void learn(MarkovChain mm, Observation[] o) throws MalformedModelException {    
    mm.initializeTransitionMatrix();

    tMatrix = mm.getTransitionMatrix();
    
    //learn and convert to probability matrix
    _learnFreq(mm, o);
    
    _convertFreqToProb(mm);
    
    //set transition matrix
    mm.setTransitionMatrix(tMatrix);
  }
  
  /**
   * Learn through multiple observation list
   * 
   * @param mm
   * @param o
   */
  public void learn(MarkovChain mm, java.util.Vector<Observation[]> o) throws MalformedModelException {
    mm.initializeTransitionMatrix();

    tMatrix = mm.getTransitionMatrix();
    
    //learn and convert to probability matrix
    for (int i=0; i < o.size(); i++)
      _learnFreq(mm, o.get(i));
    
    _convertFreqToProb(mm);
    
    //set transition matrix
    mm.setTransitionMatrix(tMatrix);
  }
  
  /**
   * Learn through single observation list with additional info
   * 
   * @param mm
   * @param obs
   * @param info
   */
  public void learn(MarkovChain mm, Observation[] obs, Observation[] info) throws MalformedModelException {    
    mm.initializeTransitionMatrix();

    tMatrix = mm.getTransitionMatrix();
    
    //learn and convert to probability matrix
    _learnFreq(mm, obs, info);
    
    _convertFreqToProb(mm);
    
    //set transition matrix
    mm.setTransitionMatrix(tMatrix);
  }
  
  /**
   * Learn through multiple observation list with additional info
   * 
   * @param mm
   * @param obs
   * @param info
   */
  public void learn(MarkovChain mm, java.util.Vector<Observation[]> obs, java.util.Vector<Observation[]> info) throws MalformedModelException {
    mm.initializeTransitionMatrix();

    tMatrix = mm.getTransitionMatrix();
    
    //learn and convert to probability matrix
    for (int i=0; i < obs.size(); i++)
      _learnFreq(mm, obs.get(i), info.get(i));
    
    _convertFreqToProb(mm);
    
    //set transition matrix
    mm.setTransitionMatrix(tMatrix);
  }
  
  //internel learner
  private void _learnFreq (MarkovChain mm, Observation[] o) {    
    int nOrder = mm.getOrder();
  
    //loop through observation
    for (int i=nOrder; i < o.length; i++) {
      Observation[] fromState = Arrays.copyOfRange(o, i-nOrder, i);
      Observation toState = o[i];
      
      int fromIndex = mm.getMatrixIndex(fromState);
      int toIndex = mm.getMatrixIndex(toState);      
      
      tMatrix.set(fromIndex, toIndex, tMatrix.get(fromIndex, toIndex) + 1);
    }
  }
  
  private void _learnFreq (MarkovChain mm, Observation[] obs, Observation[] info) {    
    int nOrder = mm.getOrder();
  
    //loop through observation
    for (int i=nOrder; i < obs.length; i++) {
      Observation[] fromState = Arrays.copyOfRange(obs, i-nOrder, i);
      Observation toState = obs[i];
      
      Observation fromInfo = info[i];
      
      int fromIndex = mm.getMatrixIndex(fromState, fromInfo);
      int toIndex = mm.getMatrixIndex(toState);      
      
      tMatrix.set(fromIndex, toIndex, tMatrix.get(fromIndex, toIndex) + 1);
    }
  }
  
  
  private void _convertFreqToProb (MarkovChain mm) {
    for (int i=0; i < tMatrix.rows(); i++) {
      org.la4j.vector.Vector rowMatrix = tMatrix.getRow(i);
      double count = rowMatrix.fold(Vectors.asSumAccumulator(0));
      if (count <= 0)
        continue;
      
      for (int j=0; j < rowMatrix.length(); j++) {
        if (isPriorLearnerAvailable()) {
          //prior learner
          double val = priorLearner.getPosterior(mm, (int) tMatrix.get(i, j), (int) count);
          tMatrix.set(i, j, val);
        } else{
          //normal counting
          double val = tMatrix.get(i, j) / count;
          tMatrix.set(i, j, val);
        }
      }
    }
  }
  
}