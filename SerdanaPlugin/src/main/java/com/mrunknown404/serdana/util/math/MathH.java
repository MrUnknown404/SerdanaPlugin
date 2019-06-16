package main.java.com.mrunknown404.serdana.util.math;

import java.util.List;

public class MathH {
	/** Clamps the given number within the specified min/max */
	public static float clamp(float num, float min, float max) {
		return num < min ? min : (num > max ? max : num);
	}
	
	/** Rounds the given number to the specified decimal */
	public static float roundTo(float number, int decimal) {
		double tempDecimal = 1;
		for (int i = 0; i < decimal; i++) {
			tempDecimal *= 10;
		}
		
		return (float) (Math.round(number * tempDecimal) / tempDecimal);
	}
	
	/** Calculates an average based on the given list */
	public static float calculateAverage(List<Integer> list) {
		Integer sum = 0;
		
		if (!list.isEmpty()) {
			for (Integer mark : list) {
				sum += mark;
			}
			
			return sum.floatValue() / list.size();
		}
		
		return sum;
	}
}
