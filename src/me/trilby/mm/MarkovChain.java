package me.trilby.mm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.la4j.factory.CRSFactory;
import org.la4j.factory.Factory;
import org.la4j.matrix.Matrix;

import me.trilby.mm.exception.MalformedModelException;

import be.ac.ulg.montefiore.run.jahmm.Observation;


public class MarkovChain {
  
  /**
   * Markov Model Implementation. Can be learned by Observations with MMLearner
   * 
   * @param nOrder  Order of MM
   * @param states  Observable State List
   * @param tMatrix  Transition Matrix
   * 
   */
  
  private int nOrder; 
  
  private Map<String, Integer> statesMap;	// <state, order> hash map
  private List<Observation> states;

  private Map<String, Integer> infosMap; // <info, order> hash map
  private List<Observation> infos;
  
  private Matrix tMatrix;
  
  //Constructor
  public MarkovChain(int n) {
    this.nOrder = n;
  }
  
  public MarkovChain(int n, Matrix tMatrix) {
    this.nOrder = n;
    this.tMatrix = tMatrix;
  }
  
  
  //Methods
  /**
   * n-order of Markov model
   * 
   * setOrder(int n)
   * getOrder()
   */
  public void setOrder(int n) {
    this.nOrder = n;
  }
  
  public int getOrder() {
    return nOrder;
  }
  
  /**
   * States of Markov model
   * 
   * setStates(List<Observation> states)
   * getStates()
   */
  public void setStates(List<Observation> states) {
    HashMap<String, Integer> statesMap = new HashMap<String, Integer>();
    
    for (int i=0; i < states.size(); i++)
      statesMap.put(states.get(i).toString(), i);
    
    this.statesMap = statesMap;
    this.states = states;
    
  }
  
  public List<Observation> getStates() {    
    return states;
  }

  /**
   * Additional Infomation Prior for Markov model
   * 
   * setInfos(List<Observation> infos)
   * getInfos()
   * isInfosAvailable()
   */
  public void setInfos(List<Observation> infos) {
    HashMap<String, Integer> infosMap = new HashMap<String, Integer>();
    
    for (int i=0; i < infos.size(); i++)
      infosMap.put(infos.get(i).toString(), i);
    
    this.infosMap = infosMap;
    this.infos = infos;
    
  }
  
  public List<Observation> getInfos() {    
    return infos;
  }
  
  public boolean isInfosAvailable() {
    return (infos != null);
  }
  
  
  
  /**
   * Transition matrix of Markov model
   * 
   * setTransitionMatrix(double tMatrix[][])
   * getTransitionMatrix()
   */
  public void setTransitionMatrix(Matrix tMatrix) {
    this.tMatrix = tMatrix;
  }
  
  public Matrix getTransitionMatrix() {
    return tMatrix;
  }
  
  /**
   * Initialize transition matrix with given state values. MarkovModel itself should have proper sequence values of states.
   * This function only initialize transition matrix with zero values with proper matrix size.
   * If you want to learn transition rules from the values of states, please use MMLearner class. 
   * @throws MalformedModelException  throw this exception when #(n-order)<0 or stored states are empty.
   */
  public void initializeTransitionMatrix() throws MalformedModelException {
    if ((nOrder < 0) || (states.size() <= 0)) throw new MalformedModelException();

    int rows = 1;
    int cols = states.size();
    
    int infoPrior = 1;
    
    //determine # of rows
    if (isInfosAvailable()) {
      infoPrior = infos.size();
    }
    
    if (nOrder == 1) {
      rows = states.size() * infoPrior;
    } else { 
      rows = (int) Math.pow(states.size(), nOrder) * infoPrior;
    }
    
    
    Factory crsFactory = new CRSFactory();
    tMatrix = crsFactory.createMatrix(rows, cols);
  }
  
  
  public int getMatrixIndex(Observation obs) {
    return statesMap.get(obs.toString());
  }
  
  public int getMatrixIndex(Observation[] obs) {
    int index = 0;
    int nState = states.size();
    
    for (int i=0; i < obs.length; i++) {
      int coef = (int) Math.pow(nState, (obs.length - i - 1));
      index = index + statesMap.get(obs[i].toString()) * coef;
    }
    
    return index;
  }
  
  public int getMatrixIndex(Observation[] obs, Observation info) {
    int index = 0;
    int nState = states.size();
    int nInfo = infos.size();
    
    for (int i=0; i < obs.length; i++) {
      int coef = (int) Math.pow(nState, (obs.length - i - 1));
      index = index + statesMap.get(obs[i].toString()) * coef;
    }
    
    index = index * nInfo + infosMap.get(info.toString());
    
    return index;
  }
}

