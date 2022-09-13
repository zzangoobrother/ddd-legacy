package kitchenpos.domain;

import java.util.List;

public class FakeProfanityClient implements ProfanityClient {

  private static final List<String> profanities = List.of("비속어", "욕설");

  @Override
  public boolean containsProfanity(String text) {
    return profanities.stream()
        .anyMatch(text::contains);
  }
}
