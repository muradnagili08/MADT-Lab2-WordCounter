package com.madt.lab2.wordcounter;

import com.madt.lab2.wordcounter.model.TextMetrics;
import com.madt.lab2.wordcounter.util.TextMetricsCalculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for TextMetricsCalculator verifying all requirements and edge cases.
 */
public class TextMetricsCalculatorTest {

    @Test
    public void testNullInputReturnsZeroMetrics() {
        TextMetrics metrics = TextMetricsCalculator.calculate(null);
        assertNotNull(metrics);
        assertEquals(0, metrics.getSentencesCount());
        assertEquals(0, metrics.getWordsCount());
        assertEquals(0, metrics.getPunctuationCount());
        assertEquals(0, metrics.getNumbersCount());
    }

    @Test
    public void testEmptyAndWhitespaceInputReturnsZeroMetrics() {
        TextMetrics metrics = TextMetricsCalculator.calculate("   \n\t  ");
        assertNotNull(metrics);
        assertEquals(0, metrics.getSentencesCount());
        assertEquals(0, metrics.getWordsCount());
        assertEquals(0, metrics.getPunctuationCount());
        assertEquals(0, metrics.getNumbersCount());
    }

    @Test
    public void testSimpleSentence() {
        String text = "The quick brown fox jumps over the lazy dog.";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);

        assertEquals(1, metrics.getSentencesCount());
        assertEquals(9, metrics.getWordsCount());
        assertEquals(1, metrics.getPunctuationCount()); // period
        assertEquals(0, metrics.getNumbersCount());
    }

    @Test
    public void testNumbersAndDecimals() {
        String text = "Room 402 costs 3.50 dollars.";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);

        assertEquals(1, metrics.getSentencesCount());
        assertEquals(3, metrics.getWordsCount()); // Room, costs, dollars
        assertEquals(2, metrics.getPunctuationCount()); // . in 3.50 and final .
        assertEquals(2, metrics.getNumbersCount()); // 402 and 3.50
    }

    @Test
    public void testMultipleSentencesWithDifferentPunctuation() {
        String text = "Hello world! How are you doing today? I am fine.";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);

        assertEquals(3, metrics.getSentencesCount());
        assertEquals(10, metrics.getWordsCount());
        assertEquals(3, metrics.getPunctuationCount()); // !, ?, .
        assertEquals(0, metrics.getNumbersCount());
    }

    @Test
    public void testContractionsAndHyphenatedWords() {
        String text = "It's a state-of-the-art laboratory in 2026.";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);

        assertEquals(1, metrics.getSentencesCount());
        assertEquals(5, metrics.getWordsCount()); // It's, a, state-of-the-art, laboratory, in
        assertEquals(5, metrics.getPunctuationCount()); // ' in It's, 3 hyphens in state-of-the-art, 1 final period .
        assertEquals(1, metrics.getNumbersCount()); // 2026
    }

    @Test
    public void testMultiplePunctuationMarks() {
        String text = "Really?! That is amazing...";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);

        assertEquals(2, metrics.getSentencesCount());
        assertEquals(4, metrics.getWordsCount()); // Really, That, is, amazing
        assertEquals(5, metrics.getPunctuationCount()); // ?, !, ., ., .
        assertEquals(0, metrics.getNumbersCount());
    }

    @Test
    public void testSentenceWithoutEndingPeriod() {
        String text = "Just a headline with no period";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);

        assertEquals(1, metrics.getSentencesCount());
        assertEquals(6, metrics.getWordsCount());
        assertEquals(0, metrics.getPunctuationCount());
        assertEquals(0, metrics.getNumbersCount());
    }

    @Test
    public void testUnicodeWordsLithuanianAndFrench() {
        String lithuanian = "Ąžuolas auga miške.";
        TextMetrics metricsLT = TextMetricsCalculator.calculate(lithuanian);
        assertEquals(1, metricsLT.getSentencesCount());
        assertEquals(3, metricsLT.getWordsCount()); // Ąžuolas, auga, miške
        assertEquals(1, metricsLT.getPunctuationCount()); // .
        assertEquals(0, metricsLT.getNumbersCount());

        String french = "C'est un café délicieux!";
        TextMetrics metricsFR = TextMetricsCalculator.calculate(french);
        assertEquals(1, metricsFR.getSentencesCount());
        assertEquals(4, metricsFR.getWordsCount()); // C'est, un, café, délicieux
        assertEquals(2, metricsFR.getPunctuationCount()); // ' and !
        assertEquals(0, metricsFR.getNumbersCount());
    }

    @Test
    public void testFormattedNumbersWithThousandSeparators() {
        String text = "There are 1,000,000 stars and 1,000.50 credits remaining.";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);
        assertEquals(1, metrics.getSentencesCount());
        assertEquals(6, metrics.getWordsCount()); // There, are, stars, and, credits, remaining
        assertEquals(2, metrics.getNumbersCount()); // 1,000,000 and 1,000.50
    }

    @Test
    public void testAlphanumericWordsAndIdentifiers() {
        String text = "Lab2 is held in room 402 during year 2026.";
        TextMetrics metrics = TextMetricsCalculator.calculate(text);
        assertEquals(1, metrics.getSentencesCount());
        assertEquals(7, metrics.getWordsCount()); // Lab2, is, held, in, room, during, year
        assertEquals(2, metrics.getNumbersCount()); // 402, 2026
    }
}
