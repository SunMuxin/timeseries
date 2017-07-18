package com.realsight.westworld.tsp.lib.model.htm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.realsight.westworld.tsp.lib.model.htm.neurongroups.NeuroGroup;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.TimeSeries.Entry;

import Jama.Matrix;

public class AnomalyHierarchy extends Hierarchy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 973860126368457409L;

	private int maxLeftSemiContextsLenght = 7;
	private int maxActiveNeuronsNum = 15;
	private double minValue;
	private double maxValue;
	private double minValueStep;
	private int numBit = 3;
	private Random rng = new Random(542);

	public int getNumBit() {
		return 1<<numBit;
	}
	
	public Random getRng() {
		return rng;
	}
	
	public double getMinValue() {
		return minValue;
	} 
	
	public double getMaxValue() {
		return maxValue;
	}
	
	public double getMinValueStep() {
		return minValueStep;
	}

	private AnomalyHierarchy(DoubleSeries series) {
		this.neuroGroup = new NeuroGroup(maxActiveNeuronsNum, maxLeftSemiContextsLenght);
		double scope = series.max() - series.min();
		this.minValue = series.min() - scope*0.2;
		this.maxValue = series.max() + scope*0.2;
		int numNormValue = (1<<numBit) - 1;
		this.minValueStep = (maxValue - minValue)/numNormValue;
		if (this.minValueStep < 1e-5) this.minValueStep = 1.0;
	}
	
	private AnomalyHierarchy(Double minValue, Double maxValue) {
		this.neuroGroup = new NeuroGroup(maxActiveNeuronsNum, maxLeftSemiContextsLenght);
		this.minValue = minValue;
		this.maxValue = maxValue;
		int numNormValue = (1<<numBit) - 1;
		this.minValueStep = (maxValue - minValue)/numNormValue;
		if (this.minValueStep < 1e-5) this.minValueStep = 1.0;
	}
	
	public int getBit(double value) {
		int bit = (int) ((value-this.minValue)/this.minValueStep);
		int numNormValue = (1<<numBit) - 1;
		if (bit < 0) bit = 0;
		if (bit > numNormValue) bit = numNormValue;
		return bit;
	}
	
	protected List<Integer> value2SensFacts(Matrix value) {
		List<Integer> currSensFacts = new ArrayList<Integer>();
		int bit = getBit(value.get(0, 0));
		for(int j = 0; j < numBit; j++){
			if ( (bit&(1<<j)) > 0 ) {
				currSensFacts.add(2*j+1);
			} else {
				currSensFacts.add(2*j);
			}
		}
		return currSensFacts;
	}
	
	public static AnomalyHierarchy build(DoubleSeries nSeries){
		return build(nSeries, false);
	}
	
	public static AnomalyHierarchy build(DoubleSeries nSeries, boolean flag){
		AnomalyHierarchy res = new AnomalyHierarchy(nSeries);
		if (flag){
			for(int i = 0; i < nSeries.size(); i++) {
				Entry<Double> entry = nSeries.get(i);
				res.learn(new Matrix(1, 1, entry.getItem()));
			}
		}
		return res;
	}

	public static AnomalyHierarchy build(Double minValue, Double maxValue) {
		// TODO Auto-generated method stub
		return new AnomalyHierarchy(minValue, maxValue);
	}
}

