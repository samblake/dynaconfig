package samblake.dynaconfig;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

public class StringConverter {
	
	public static String toDotted(String camelCase) {
		StringBuilder dotted = new StringBuilder();
		for (char c : camelCase.toCharArray()) {
			if (isUpperCase(c)) {
				dotted.append('.').append(toLowerCase(c));
			}
			else {
				dotted.append(c);
			}
		}
		return dotted.toString();
	}
}