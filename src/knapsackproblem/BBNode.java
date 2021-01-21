/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knapsackproblem;

/**
 * Class representing node of branch and bound tree.
 * It accumulates results calculated in search path.
 * @author Andrzej Marciniak
 */
public class BBNode { 
    
    float upperBound;  //best case
 
    float lowerBound;  //worst case
  
    int level; // Level of the node in the decision tree 
     
    boolean isSelected; 
  
    float totalCost; // Aggregated Cost
  
    float totalWeight; // Aggregated Weights
    public BBNode() {} 
    public BBNode(BBNode cpy) 
    { 
        this.totalCost = cpy.totalCost; 
        this.totalWeight = cpy.totalWeight;
        this.upperBound = cpy.upperBound; 
        this.lowerBound = cpy.lowerBound; 
        this.level = cpy.level; 
        this.isSelected = cpy.isSelected; 
    } 
} 
  