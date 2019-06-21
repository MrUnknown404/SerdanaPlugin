package main.java.serdana.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
	private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
	private final Random random;
	private double total = 0;
	
	public RandomCollection() {
		random = new Random();
	}
	
	public void add(double weight, E result) {
		total += weight;
		map.put(total, result);
	}
	
	public E getRandom() {
		return map.higherEntry(random.nextDouble() * total).getValue();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
}
