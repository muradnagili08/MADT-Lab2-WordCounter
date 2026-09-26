package com.madt.lab2.wordcounter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madt.lab2.wordcounter.model.CalculationRecord;
import com.madt.lab2.wordcounter.model.TextMetrics;
import com.madt.lab2.wordcounter.util.TextMetricsCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ViewModel for WordCounter. Retains current metrics and runtime calculation history
 * across configuration changes (e.g. screen rotations).
 */
public class WordCounterViewModel extends ViewModel {

    private final MutableLiveData<TextMetrics> currentMetrics = new MutableLiveData<>(TextMetrics.empty());
    private final MutableLiveData<List<CalculationRecord>> calculationHistory = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> emptyInputError = new MutableLiveData<>(false);

    private final List<CalculationRecord> internalHistoryList = new ArrayList<>();

    public LiveData<TextMetrics> getCurrentMetrics() {
        return currentMetrics;
    }

    public LiveData<List<CalculationRecord>> getCalculationHistory() {
        return calculationHistory;
    }

    public LiveData<Boolean> getEmptyInputError() {
        return emptyInputError;
    }

    /**
     * Analyzes the given input text. If empty, triggers validation error.
     * Otherwise, calculates metrics and appends to runtime history.
     *
     * @param input Raw text from user input field.
     * @return true if analysis was successful, false if input was empty.
     */
    public boolean processText(String input) {
        if (input == null || input.trim().isEmpty()) {
            emptyInputError.setValue(true);
            currentMetrics.setValue(TextMetrics.empty());
            return false;
        }

        emptyInputError.setValue(false);
        TextMetrics metrics = TextMetricsCalculator.calculate(input);
        currentMetrics.setValue(metrics);

        CalculationRecord record = new CalculationRecord(input.trim(), metrics);
        // Prepend most recent calculation to the top of the history list
        internalHistoryList.add(0, record);
        calculationHistory.setValue(new ArrayList<>(internalHistoryList));

        return true;
    }

    /**
     * Resets the empty input validation error when the user starts typing.
     */
    public void clearInputError() {
        if (Boolean.TRUE.equals(emptyInputError.getValue())) {
            emptyInputError.setValue(false);
        }
    }

    /**
     * Clears all calculation history records for the current session.
     */
    public void clearHistory() {
        internalHistoryList.clear();
        calculationHistory.setValue(Collections.emptyList());
    }

    /**
     * Clears current calculated metrics.
     */
    public void resetCurrentMetrics() {
        currentMetrics.setValue(TextMetrics.empty());
    }
}
