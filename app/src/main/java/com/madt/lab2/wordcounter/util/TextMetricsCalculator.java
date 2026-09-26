package com.madt.lab2.wordcounter.util;

import com.madt.lab2.wordcounter.model.TextMetrics;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reusable, standalone utility class for calculating text metrics.
 * Has zero dependencies on the Android framework to enable fast, headless JVM testing.
 */
public final class TextMetricsCalculator {

    // Regex for numeric tokens: integers, decimals, and formatted numbers (e.g. 42, 3.14, 1,000, 1,000,000)
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\b\\d+(?:[.,]\\d+)*\\b");

    // Regex for words: tokens containing letters, supporting Unicode, alphanumeric words (e.g. Lab2, COVID-19), contractions & hyphens
    private static final Pattern WORD_PATTERN = Pattern.compile("(?U)\\b(?=[^\\s]*?\\p{L})[\\p{L}\\p{N}]+(?:['’\\p{Pd}][\\p{L}\\p{N}]+)*\\b");

    // Regex for punctuation characters (both ASCII and Unicode punctuation)
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}\\p{P}]");

    private TextMetricsCalculator() {
        // Prevent instantiation of utility class
    }

    /**
     * Calculates all 4 metrics for the provided text.
     *
     * @param text The input string to analyze.
     * @return Immutable TextMetrics object containing the calculated counts.
     */
    public static TextMetrics calculate(String text) {
        if (text == null || text.trim().isEmpty()) {
            return TextMetrics.empty();
        }

        int sentences = countSentences(text);
        int words = countWords(text);
        int punctuation = countPunctuation(text);
        int numbers = countNumbers(text);

        return new TextMetrics(sentences, words, punctuation, numbers);
    }

    /**
     * Counts the number of sentences in the input text.
     * Uses Java's BreakIterator for standard sentence boundary detection,
     * ensuring sentences with contractions, decimals, abbreviations, or multi-punctuation are handled.
     */
    public static int countSentences(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.getDefault());
        iterator.setText(text);

        int count = 0;
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String sentence = text.substring(start, end).trim();
            // A valid sentence must contain at least one alphanumeric character
            if (hasAlphanumeric(sentence)) {
                count++;
            }
        }

        // Fallback: If BreakIterator didn't find boundaries but text contains alphanumeric characters
        if (count == 0 && hasAlphanumeric(text)) {
            count = 1;
        }

        return count;
    }

    /**
     * Counts the number of words in the input text.
     * Word tokens must contain letters (e.g., "hello", "it's", "well-known").
     * Pure numeric tokens are excluded and counted under numbers.
     */
    public static int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        Matcher matcher = WORD_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Counts all punctuation marks in the input text.
     * Matches standard ASCII punctuation as well as Unicode punctuation characters.
     */
    public static int countPunctuation(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        Matcher matcher = PUNCTUATION_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Counts distinct numeric tokens in the input text (e.g., "100", "3.14").
     */
    public static int countNumbers(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        Matcher matcher = NUMBER_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static boolean hasAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetterOrDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
