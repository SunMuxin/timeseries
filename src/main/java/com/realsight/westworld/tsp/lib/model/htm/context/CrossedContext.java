package com.realsight.westworld.tsp.lib.model.htm.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CrossedContext implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4985948833978382789L;
	
	private int T;
	private int F;
	private int zeroLevel;
	private int level;
	private int leftHash;
	private int rightHash;
	private Map<String, Object> rewards;
	
	public CrossedContext(int T, 
			int F, 
			int zeroLevel,
			int level,
			int leftHash, 
			int rightHash, 
			Map<String, Object> rewards) {
		this.T = T;
		this.F = F;
		this.zeroLevel = zeroLevel;
		this.level = level;
		this.leftHash = leftHash;
		this.rightHash = rightHash;
		this.rewards = rewards;
	}
	
	public CrossedContext(int T, int F, int zeroLevel, int leftHash, int rightHash) {
		this(T, F, zeroLevel, 0, leftHash,rightHash, new HashMap<String, Object>());
	}
	
	public double getNumActivate() {
		return 1.0*T/(T+F);
	}
	public int getT() {
		return T;//Math.pow(2, rightSize-1);
	}
	public int getF() {
		return F;//Math.pow(2, rightSize-1);
	}
	public void addNumActivate() {
		T ++;
	}
	public void subNumActivate() {
		F --;
	}
	public int getZeroLevel() {
		return zeroLevel;
	}
	public void setZeroLevel(int zeroLevel) {
		this.zeroLevel = zeroLevel;
	}
	public int getLeftHash() {
		return leftHash;
	}
	public void setLeftHash(int leftHash) {
		this.leftHash = leftHash;
	}
	public int getRightHash() {
		return rightHash;
	}
	public Map<String, Object> getRewards() {
		return this.rewards;
	}
	public void setRewards(Map<String, Object> rewards) {
		this.rewards = rewards;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
