package ray.math;

public class Tricky {
	public static double epsilon = 0.0000000000005;
	public static double epsilon2 = 0.000000001;
	public static double epsilon3 = 0.001;

	public static boolean equals(double opt1, double opt2) {
		return Math.abs(opt1 - opt2) < epsilon;
	}
	public static boolean larger(double opt1, double opt2){
		return (opt1 - opt2) > epsilon;
	}
	public static boolean larger(double opt1, double opt2, double opt3){
		return (opt1 - opt2) > opt3;
	}
	public static boolean smaller(double opt1, double opt2){
		return (opt1 - opt2) < -epsilon;
	}

	public static double limited(double value, double max, double min) {
		return Math.max(min, Math.min(max, value));
	}

	public static Color limited(Color value, double max, double min) {
		return new Color(limited(value.r, max, min),
				limited(value.g, max, min), limited(value.b, max, min));
	}
}
