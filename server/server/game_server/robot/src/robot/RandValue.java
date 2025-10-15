package robot;

import java.util.Random;

public class RandValue {

	static private final Random random = new Random(System.currentTimeMillis());

	static public int randRanger(int s, int e) {
		if (s == e)
			return s;
		else if (s < e)
			return random.nextInt(e - s + 1) + s;
		else
			return random.nextInt(s - e + 1) + e;
	}

	static public int rand(int e) {
		return random.nextInt(e);
	}

	static private char randNameChar() {
		while (true) {
			char c = (char) random.nextInt(128);
			if (Character.isDigit(c) || Character.isLetter(c))
				return c;
		}
	}

	static public String randName(int length) {
		StringBuilder sb = new StringBuilder();
		sb.append("RB");
		for (int i = 2; i < length; i++)
			sb.append(randNameChar());
		return sb.toString();
	}

	static public String randFamilyName() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++)
			sb.append(randNameChar());
		return sb.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++)
			System.out.println(randName(5));
	}
}
