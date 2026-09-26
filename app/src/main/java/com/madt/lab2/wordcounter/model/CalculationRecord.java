package com.madt.lab2.wordcounter.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Model representing a single calculation event recorded during the current app runtime.
 */
public final class CalculationRecord implements Serializable {
    private final long id;
    private final String inputText;
    private final String timestampFormatted;
    private final TextMetrics metrics;

    public CalculationRecord(String inputText, TextMetrics metrics) {
        this.id = System.currentTimeMillis();
        this.inputText = inputText != null ? inputText : "";
        this.metrics = metrics != null ? metrics : TextMetrics.empty();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        this.timestampFormatted = sdf.format(new Date(this.id));
    }

    public long getId() {
        return id;
    }

    public String getInputText() {
        return inputText;
    }

    public String getSnippet(int maxLength) {
        if (inputText.length() <= maxLength) {
            return inputText.replace("\n", " ");
        }
        return inputText.substring(0, maxLength).replace("\n", " ") + "...";
    }

    public String getTimestampFormatted() {
        return timestampFormatted;
    }

    public TextMetrics getMetrics() {
        return metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculationRecord that = (CalculationRecord) o;
        return id == that.id &&
                Objects.equals(inputText, that.inputText) &&
                Objects.equals(timestampFormatted, that.timestampFormatted) &&
                Objects.equals(metrics, that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inputText, timestampFormatted, metrics);
    }
}
