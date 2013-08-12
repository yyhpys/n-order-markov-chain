package me.trilby.mm.learner;

import me.trilby.mm.MarkovChain;


public abstract class PriorLearner {
  
  public abstract double getPosterior (MarkovChain mm,int count, int sum);
  
}