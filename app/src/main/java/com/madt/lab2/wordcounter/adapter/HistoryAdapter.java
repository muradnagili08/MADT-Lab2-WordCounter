package com.madt.lab2.wordcounter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madt.lab2.wordcounter.R;
import com.madt.lab2.wordcounter.databinding.ItemCalculationHistoryBinding;
import com.madt.lab2.wordcounter.model.CalculationRecord;
import com.madt.lab2.wordcounter.model.TextMetrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying calculation history records in a RecyclerView.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CalculationRecord record);
    }

    private final List<CalculationRecord> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public HistoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<CalculationRecord> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCalculationHistoryBinding binding = ItemCalculationHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CalculationRecord record = items.get(position);
        holder.bind(record, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCalculationHistoryBinding binding;

        HistoryViewHolder(ItemCalculationHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CalculationRecord record, OnItemClickListener listener) {
            Context context = binding.getRoot().getContext();
            TextMetrics m = record.getMetrics();

            binding.tvHistoryTimestamp.setText(
                    context.getString(R.string.history_item_time_format, record.getTimestampFormatted()));
            binding.tvHistorySnippet.setText(record.getSnippet(140));
            binding.tvHistoryMetricsSummary.setText(
                    context.getString(R.string.history_item_metrics_summary,
                            m.getSentencesCount(),
                            m.getWordsCount(),
                            m.getPunctuationCount(),
                            m.getNumbersCount()));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(record);
                }
            });
        }
    }
}
