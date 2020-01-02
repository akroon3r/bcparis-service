package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MessageUtils {

    private static final String SEMICOLLON = ":";
    private static final String STRING_END_ONE = "]]>$";
    private static final String STRING_END_TWO = "\n$";

    private static final Set<String> KNOWN_TOKENS;

    static {
        HashSet<String> known_tokens = new HashSet<>();
        known_tokens.add(Keys.REQUEST_SCHEMA_SN_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_MT_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_MSID_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_FROM_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_TO_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_SUBJ_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_TEXT_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_RE_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_SNME_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_DL_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_LIC_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_ODN_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_FLC_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_VIN_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_REG_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_RNS_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_RVL_KEY);
        known_tokens.add(Keys.REQUEST_SCHEMA_TEST_RNS_KEY);
        KNOWN_TOKENS = Collections.unmodifiableSet(known_tokens);
    }


    /**
     * Extract the attribute value based on a give token
     * Known tokens includes:
     * <ul>
     *  <li>FROM
     *  <li>TO
     *  <li>TEXT
     *  <li>RE
     *  <li>SN
     *  <li>MT
     *  <li>MSID
     *  <li>SUBJ
     *  <li>SNME
     *  <li>DL
     *  <li>LIC
     *  <li>ODN
     *  <li>FLC
     *  <li>VIN
     *  <li>REG
     *  <li>RNS
     *  <li>RVL
     *  <li>TestRNS
     * </ul>
     *
     * @param message the source message
     * @param key     a known key
     * @return the value of the attribute
     * @throws IllegalArgumentException if the key is not a known key
     * @since 1.0.20
     */
    public static String GetValue(String message, String key) {

        if (!KNOWN_TOKENS.contains(key)) throw new IllegalArgumentException("key must be a known token");

        if (StringUtils.isEmpty(message)) return null;

        message = removeKnownEnd(message);

        message = removeToToken(message, key);

        if (message == null) return null;

        return message.substring(0, getEndIndex(message)).replaceAll("\\s+$", "");

    }

    private static String removeKnownEnd(String message) {
        message = message.replaceAll(STRING_END_ONE, "");
        return message.replaceAll(STRING_END_TWO, "");
    }

    private static String removeToToken(String message, String token) {
        int startIndex = message.indexOf(token + SEMICOLLON);
        if (startIndex == -1) return null;

        startIndex += token.length() + 1;

        return message.substring(startIndex);
    }

    private static int getEndIndex(String message) {
        int currentEndIndex = message.length();

        for (String token : KNOWN_TOKENS) {

            int tokenIndex = message.indexOf(token + SEMICOLLON);

            if (tokenIndex < currentEndIndex && tokenIndex >= 0) {
                currentEndIndex = tokenIndex;
            }

            if (message.indexOf(":") > currentEndIndex) break;
        }

        return currentEndIndex;
    }
}


