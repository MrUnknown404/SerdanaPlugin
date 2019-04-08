package main.java.com.mrunknown404.serdana.util.math;

public class MathHelper {
	public static float clamp(float num, float min, float max) {
		return num < min ? min : (num > max ? max : num);
	}
}
