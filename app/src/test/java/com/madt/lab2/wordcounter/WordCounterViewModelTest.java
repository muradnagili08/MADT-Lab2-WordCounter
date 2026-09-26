package com.madt.lab2.wordcounter;

import com.madt.lab2.wordcounter.model.CalculationRecord;
import com.madt.lab2.wordcounter.model.TextMetrics;
import com.madt.lab2.wordcounter.viewmodel.WordCounterViewModel;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for WordCounterViewModel verifying MVVM state transitions,
 * input validation, metrics updates, and history management.
 */
public class WordCounterViewModelTest {

    private WordCounterViewModel viewModel;

    @Before
    public void setUp() {
        ArchTaskExecutor.getInstance().setDelegate(new TaskExecutor() {
            @Override
            public void executeOnDiskIO(Runnable runnable) {
                runnable.run();
            }

            @Override
            public void postToMainThread(Runnable runnable) {
                runnable.run();
            }

            @Override
            public boolean isMainThread() {
                return true;
            }
        });
        viewModel = new WordCounterViewModel();
    }

    @After
    public void tearDown() {
        ArchTaskExecutor.getInstance().setDelegate(null);
    }

    @Test
    public void testInitialState() {
        assertNotNull(viewModel.getCurrentMetrics().getValue());
        assertEquals(0, viewModel.getCurrentMetrics().getValue().getWordsCount());
        assertNotNull(viewModel.getCalculationHistory().getValue());
        assertTrue(viewModel.getCalculationHistory().getValue().isEmpty());
        assertEquals(Boolean.FALSE, viewModel.getEmptyInputError().getValue());
    }

    @Test
    public void testEmptyInputValidationSetsErrorAndResetsMetrics() {
        boolean result = viewModel.processText("   ");
        assertFalse(result);
        assertEquals(Boolean.TRUE, viewModel.getEmptyInputError().getValue());
        assertEquals(0, viewModel.getCurrentMetrics().getValue().getWordsCount());
        assertTrue(viewModel.getCalculationHistory().getValue().isEmpty());
    }

    @Test
    public void testValidInputUpdatesMetricsAndHistory() {
        boolean result = viewModel.processText("Hello world! 42.");
        assertTrue(result);
        assertEquals(Boolean.FALSE, viewModel.getEmptyInputError().getValue());

        TextMetrics metrics = viewModel.getCurrentMetrics().getValue();
        assertNotNull(metrics);
        assertEquals(2, metrics.getWordsCount());
        assertEquals(1, metrics.getNumbersCount());

        List<CalculationRecord> history = viewModel.getCalculationHistory().getValue();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals("Hello world! 42.", history.get(0).getInputText());
    }

    @Test
    public void testClearHistoryAndResetMetrics() {
        viewModel.processText("Sample text");
        assertEquals(1, viewModel.getCalculationHistory().getValue().size());

        viewModel.clearHistory();
        assertTrue(viewModel.getCalculationHistory().getValue().isEmpty());

        viewModel.resetCurrentMetrics();
        assertEquals(0, viewModel.getCurrentMetrics().getValue().getWordsCount());
    }

    @Test
    public void testEmptyInputAfterValidCalculationResetsMetrics() {
        viewModel.processText("Valid sentence with words.");
        assertEquals(4, viewModel.getCurrentMetrics().getValue().getWordsCount());

        boolean result = viewModel.processText("");
        assertFalse(result);
        assertEquals(Boolean.TRUE, viewModel.getEmptyInputError().getValue());
        assertEquals(0, viewModel.getCurrentMetrics().getValue().getWordsCount());
    }

    @Test
    public void testClearInputError() {
        viewModel.processText("");
        assertEquals(Boolean.TRUE, viewModel.getEmptyInputError().getValue());

        viewModel.clearInputError();
        assertEquals(Boolean.FALSE, viewModel.getEmptyInputError().getValue());
    }
}
