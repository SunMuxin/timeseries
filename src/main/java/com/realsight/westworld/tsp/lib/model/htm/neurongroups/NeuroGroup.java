package com.realsight.westworld.tsp.lib.model.htm.neurongroups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.realsight.westworld.tsp.lib.util.Entry;

public class NeuroGroup {
	
	private int maxActiveNeuronsNum;
	private List<Integer> leftFactsGroup;
	private List<Integer> activeNeuros;
	private NeuroGroupOperator neuroGroupOperator;
	private List<Entry<List<Integer>, List<Integer>>> potentialNewContextList;
	
	public NeuroGroup(int maxActiveNeuronsNum, int maxLeftSemiContextsLenght) {
		this.maxActiveNeuronsNum = maxActiveNeuronsNum;
		this.leftFactsGroup = new ArrayList<Integer>();
		this.neuroGroupOperator = new NeuroGroupOperator(maxLeftSemiContextsLenght);
		this.potentialNewContextList = new ArrayList<Entry<List<Integer>, List<Integer>>>();
	}
	
	public void sleep() {
		this.neuroGroupOperator.sleep();
	}
	
	private double activate(List<Integer> currSensFacts) {
		currSensFacts = new ArrayList<Integer>(new HashSet<Integer>(currSensFacts));
		Collections.sort(currSensFacts);
		List<Entry<List<Integer>, List<Integer>>> potNewZeroLevelContext = 
				new ArrayList<Entry<List<Integer>, List<Integer>>>();
		int newContextFlag = -1;
		if ( (this.leftFactsGroup.size()>0) && (currSensFacts.size()>0) ) {
			potNewZeroLevelContext.add(new Entry<List<Integer>, List<Integer>>(this.leftFactsGroup, currSensFacts));
            newContextFlag = this.neuroGroupOperator.getContextByFacts(potNewZeroLevelContext, 1);
		}
		this.neuroGroupOperator.contextCrosser(1, currSensFacts, newContextFlag>=0, new ArrayList<Entry<List<Integer>, List<Integer>>>());
		double percentSelectedContextActive = 0.0;
		if (this.neuroGroupOperator.getNumSelectedContext() > 0) {
			percentSelectedContextActive = 
					1.0 * this.neuroGroupOperator.getActiveContexts().size() / this.neuroGroupOperator.getNumSelectedContext();
		}
		
		Set<Entry<List<Integer>, List<Integer>>> activeContext = new HashSet<Entry<List<Integer>, List<Integer>>>();
		activeContext.addAll(this.neuroGroupOperator.getPotentialNewContextList());
		
		if ( (this.leftFactsGroup.size()>0) && (currSensFacts.size()>0) ) {
//			System.out.println(activeContext.contains(new Entry<List<Integer>, List<Integer>>(this.leftFactsGroup, currSensFacts)));
			activeContext.add(new Entry<List<Integer>, List<Integer>>(this.leftFactsGroup, currSensFacts));
//			System.out.println(activeContext.contains(new Entry<List<Integer>, List<Integer>>(this.leftFactsGroup, currSensFacts)));
		}
		
		Collections.sort(this.neuroGroupOperator.getActiveContexts());
		this.leftFactsGroup = new ArrayList<Integer>();
		for ( int i = 0; i<this.neuroGroupOperator.getActiveContexts().size() && i<this.maxActiveNeuronsNum; i++){
			this.leftFactsGroup.add(this.neuroGroupOperator.getActiveContexts().get(i).getContextID()+(1<<30));
		}
		for ( int i = 0; i < currSensFacts.size(); i++ ) {
			this.leftFactsGroup.add(currSensFacts.get(i));
		}
		Collections.sort(this.leftFactsGroup);
		this.activeNeuros = new ArrayList<Integer>();
		for ( int i = 0; i < this.neuroGroupOperator.getActiveContexts().size(); i++ ) {
			this.activeNeuros.add(this.neuroGroupOperator.getActiveContexts().get(i).getContextID());
		}
		
		this.potentialNewContextList = this.neuroGroupOperator.getPotentialNewContextList();
		
		this.neuroGroupOperator.contextCrosser(0, this.leftFactsGroup, false, this.potentialNewContextList);
		double percentaddContextActive = 0.0;
		if ( activeContext.size() > 0 ) {
			double newContextNum = this.neuroGroupOperator.getNumAddedContexts();
			if ( newContextFlag >= 0 )
				newContextNum += 1.0;
			percentaddContextActive = newContextNum / activeContext.size();
//			System.out.println(percentSelectedContextActive + "," + newContextNum + "," + activeContext.size()+","+newContextFlag);
//			System.out.println((1.0 - percentSelectedContextActive + percentaddContextActive) / 2.0);
		}
		
//		System.out.println(percentaddContextActive);
		return (1.0 - percentSelectedContextActive + percentaddContextActive) / 2.0; 
	}
	
	public double learnSeries(List<Integer> currSensFacts) {
		return activate(currSensFacts);
	}
	
	public double predictSeries(List<Integer> currSensFacts) {
		return this.neuroGroupOperator.getFactsActiveContext(currSensFacts).size();
	}

	public List<Integer> getActiveNeuros() {
		return activeNeuros;
	}
}

