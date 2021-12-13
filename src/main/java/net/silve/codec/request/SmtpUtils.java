package net.silve.codec.request;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class SmtpUtils {
    private SmtpUtils() {
    }

    public static List<CharSequence> toUnmodifiableList(CharSequence... sequences) {
        return sequences != null && sequences.length != 0 ? List.copyOf(Arrays.asList(sequences)) : Collections.emptyList();
    }
}
