package net.silve.codec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class SmtpUtils {
    public static List<CharSequence> toUnmodifiableList(CharSequence... sequences) {
        return sequences != null && sequences.length != 0 ? Collections.unmodifiableList(Arrays.asList(sequences)) : Collections.emptyList();
    }

    private SmtpUtils() {
    }
}
