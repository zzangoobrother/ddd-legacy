package calculator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

  public int add(String text) {
    if (text == null || text.isBlank()) {
      return 0;
    }

    String[] result = text.split(",|:");
    Matcher matcher = Pattern.compile("//(.)\n(.*)").matcher(text);
    if (matcher.find()) {
      String customDelimiter = matcher.group(1);
      result = matcher.group(2).split(customDelimiter + "|,|:");
    }

    return Arrays.stream(result).mapToInt(Integer::parseInt).sum();
  }
}
