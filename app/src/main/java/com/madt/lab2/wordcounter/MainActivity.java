package com.madt.lab2.wordcounter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.madt.lab2.wordcounter.adapter.HistoryAdapter;
import com.madt.lab2.wordcounter.databinding.ActivityMainBinding;
import com.madt.lab2.wordcounter.model.TextMetrics;
import com.madt.lab2.wordcounter.viewmodel.WordCounterViewModel;

import java.util.Random;

/**
 * Main Activity for Word Counter application.
 * Manages UI interactions, binds to WordCounterViewModel, and observes state changes.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WordCounterViewModel viewModel;
    private HistoryAdapter historyAdapter;
    private final Random random = new Random();
    private int lastSampleIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(WordCounterViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupListeners();
        setupObservers();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void setupRecyclerView() {
        historyAdapter = new HistoryAdapter(record -> {
            binding.etInputText.setText(record.getInputText());
            binding.etInputText.setSelection(record.getInputText().length());
            Toast.makeText(this, R.string.toast_copied_to_input, Toast.LENGTH_SHORT).show();
        });

        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvHistory.setAdapter(historyAdapter);
    }

    private void setupListeners() {
        // Calculate Metrics Button
        binding.btnCalculate.setOnClickListener(v -> {
            String input = binding.etInputText.getText() != null
                    ? binding.etInputText.getText().toString()
                    : "";
            viewModel.processText(input);
        });

        // Generate Sample Sentence Button
        binding.btnGenerateSample.setOnClickListener(v -> generateSampleSentence());

        // Clear Input & Metrics Button
        binding.btnClearInput.setOnClickListener(v -> {
            binding.etInputText.setText("");
            viewModel.resetCurrentMetrics();
            Toast.makeText(this, R.string.toast_metrics_cleared, Toast.LENGTH_SHORT).show();
        });

        // Clear Runtime History Button
        binding.btnClearHistory.setOnClickListener(v -> {
            viewModel.clearHistory();
            Toast.makeText(this, R.string.toast_history_cleared, Toast.LENGTH_SHORT).show();
        });

        // Clear validation error when user begins typing
        binding.etInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.clearInputError();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No-op
            }
        });
    }

    private void setupObservers() {
        // Observe Current Metrics
        viewModel.getCurrentMetrics().observe(this, this::updateMetricsDisplay);

        // Observe Runtime Calculation History
        viewModel.getCalculationHistory().observe(this, historyList -> {
            historyAdapter.updateData(historyList);
            boolean hasHistory = historyList != null && !historyList.isEmpty();
            binding.tvEmptyHistory.setVisibility(hasHistory ? View.GONE : View.VISIBLE);
            binding.rvHistory.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
            binding.btnClearHistory.setEnabled(hasHistory);
        });

        // Observe Input Validation Error
        viewModel.getEmptyInputError().observe(this, isError -> {
            if (Boolean.TRUE.equals(isError)) {
                binding.tilInputText.setError(getString(R.string.error_empty_input));
                Toast.makeText(MainActivity.this, R.string.error_empty_input, Toast.LENGTH_SHORT).show();
            } else {
                binding.tilInputText.setError(null);
            }
        });
    }

    private void updateMetricsDisplay(TextMetrics metrics) {
        if (metrics == null) {
            metrics = TextMetrics.empty();
        }
        binding.tvSentencesCount.setText(
                getString(R.string.metric_sentences_format, metrics.getSentencesCount()));
        binding.tvWordsCount.setText(
                getString(R.string.metric_words_format, metrics.getWordsCount()));
        binding.tvPunctuationCount.setText(
                getString(R.string.metric_punctuation_format, metrics.getPunctuationCount()));
        binding.tvNumbersCount.setText(
                getString(R.string.metric_numbers_format, metrics.getNumbersCount()));
    }

    private void generateSampleSentence() {
        String[] samples = getResources().getStringArray(R.array.sample_sentences);
        if (samples.length == 0) {
            return;
        }

        // Pick a sample different from the last one chosen
        int nextIndex = random.nextInt(samples.length);
        if (samples.length > 1 && nextIndex == lastSampleIndex) {
            nextIndex = (lastSampleIndex + 1) % samples.length;
        }
        lastSampleIndex = nextIndex;

        binding.etInputText.setText(samples[nextIndex]);
        binding.etInputText.setSelection(samples[nextIndex].length());
        Toast.makeText(this, R.string.toast_sample_generated, Toast.LENGTH_SHORT).show();
    }
}
