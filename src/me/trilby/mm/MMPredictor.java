package me.trilby.mm;

import java.util.Arrays;

import me.trilby.mm.exception.MalformedModelException;
import me.trilby.mm.predict.Predictor;
import be.ac.ulg.montefiore.run.jahmm.Observation;


public class MMPredictor {
  
  /**
   * Markov Model Predictor Implementation. Use Model and Predictor to predict next sequence.
   * 
   * @param predictor set predictor
   * 
   */
  
  Predictor markovPredictor;
  
  
  //Constructor
  public MMPredictor() {  }
  
  public MMPredictor(Class<? extends Predictor> predictor) {  
    setPredictor(predictor);
  }
  
  //Method
  public void setPredictor(Class<? extends Predictor> predictor) {
    try {
      markovPredictor = predictor.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Predict observation list
   * 
   * @param mm
   * @param obs
   */
  public Observation predict(MarkovChain mm, Observation[] obs) throws MalformedModelException  {    
    if (markovPredictor == null) throw new MalformedModelException();
    
    int nOrder = mm.getOrder();
    Observation[] currentContext = Arrays.copyOfRange(obs, obs.length-nOrder, obs.length);
    
    int state = markovPredictor.getNext(mm, currentContext);
    
    if (state >= 0)
      return mm.getStates().get(state);   
    else
      return null;
  }
  
  /**
   * Predict observation list with state constraint
   * 
   * @param mm
   * @param obs
   * @param constraint
   */
  public Observation predict(MarkovChain mm, Observation[] obs, Observation[] constraint) throws MalformedModelException  {    
    if (markovPredictor == null) throw new MalformedModelException();
    
    int nOrder = mm.getOrder();
    Observation[] currentContext = Arrays.copyOfRange(obs, obs.length-nOrder, obs.length);
    
    int state = markovPredictor.getNextWithConstraint(mm, currentContext, constraint);
    
    if (state >= 0)
      return mm.getStates().get(state);   
    else
      return null;
  }
  
  /**
   * Predict observation list with additional info
   * 
   * @param mm
   * @param obs
   * @param info
   */
  public Observation predict(MarkovChain mm, Observation[] obs, Observation info) throws MalformedModelException  {    
    if (markovPredictor == null) throw new MalformedModelException();
    
    int nOrder = mm.getOrder();
    Observation[] currentContext = Arrays.copyOfRange(obs, obs.length-nOrder, obs.length);
    
    int state = markovPredictor.getNext(mm, currentContext, info);
    
    if (state >= 0)
      return mm.getStates().get(state);   
    else
      return null;
  }
  
  /**
   * Predict observation list with additional info and constraint
   * 
   * @param mm
   * @param obs
   * @param info
   * @param constraint
   */
  public Observation predict(MarkovChain mm, Observation[] obs, Observation info, Observation[] constraint) throws MalformedModelException  {    
    if (markovPredictor == null) throw new MalformedModelException();
    
    int nOrder = mm.getOrder();
    Observation[] currentContext = Arrays.copyOfRange(obs, obs.length-nOrder, obs.length);
    
    int state = markovPredictor.getNextWithConstraint(mm, currentContext, info, constraint);
    
    if (state >= 0)
      return mm.getStates().get(state);   
    else
      return null;
  }
}