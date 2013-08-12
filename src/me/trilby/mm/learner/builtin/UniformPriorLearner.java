package me.trilby.mm.learner.builtin;

import me.trilby.mm.MarkovChain;
import me.trilby.mm.learner.PriorLearner;

public class UniformPriorLearner extends PriorLearner {

  @Override
  public double getPosterior(MarkovChain mm, int count, int sum) {
    double posterior = (double) (1 + count) / (double) (mm.getStates().size() + sum);
    
    return posterior;
  }
  
}