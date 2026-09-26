package com.madt.lab2.wordcounter.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Immutable data model representing the calculated metrics of an analyzed text.
 */
public final class TextMetrics implements Serializable {
    private final int sentencesCount;
    private final int wordsCount;
    private final int punctuationCount;
    private final int numbersCount;

    public TextMetrics(int sentencesCount, int wordsCount, int punctuationCount, int numbersCount) {
        this.sentencesCount = Math.max(0, sentencesCount);
        this.wordsCount = Math.max(0, wordsCount);
        this.punctuationCount = Math.max(0, punctuationCount);
        this.numbersCount = Math.max(0, numbersCount);
    }

    public static TextMetrics empty() {
        return new TextMetrics(0, 0, 0, 0);
    }

    public int getSentencesCount() {
        return sentencesCount;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public int getPunctuationCount() {
        return punctuationCount;
    }

    public int getNumbersCount() {
        return numbersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextMetrics that = (TextMetrics) o;
        return sentencesCount == that.sentencesCount &&
                wordsCount == that.wordsCount &&
                punctuationCount == that.punctuationCount &&
                numbersCount == that.numbersCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentencesCount, wordsCount, punctuationCount, numbersCount);
    }

    @Override
    public String toString() {
        return "TextMetrics{" +
                "sentencesCount=" + sentencesCount +
                ", wordsCount=" + wordsCount +
                ", punctuationCount=" + punctuationCount +
                ", numbersCount=" + numbersCount +
                '}';
    }
}
