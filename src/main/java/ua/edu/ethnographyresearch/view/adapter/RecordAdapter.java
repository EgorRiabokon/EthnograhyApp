package ua.edu.ethnographyresearch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ua.edu.ethnographyresearch.databinding.ItemRecordsBinding;
import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private final List<EthnographyRecord> records;
    private final OnRecordClickListener listener;

    public RecordAdapter(List<EthnographyRecord> records, OnRecordClickListener listener){
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecordsBinding binding = ItemRecordsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(records.get(position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecordsBinding binding;

        public ViewHolder(ItemRecordsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    EthnographyRecord record = records.get(pos);
                    listener.onClick(record);
                }
            });
        }

        public void bind(EthnographyRecord record) {
            binding.titleRecord.setText(record.title);
            binding.contentRecord.setText(record.description);

            if(record.mediaPaths != null) {
                binding.labelAttachments.setText(record.mediaPaths.size() + "");
            } else {
                binding.labelAttachments.setText("0");
            }

            if(record.latitude == 0 && record.longitude == 0) {
                binding.labelLocation.setText("Локація не вказана");
            } else {
                binding.labelLocation.setText(record.latitude + " " + record.longitude);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            binding.dateRecord.setText(formatter.format(record.date));
        }
    }

    public interface OnRecordClickListener {
        void onClick(EthnographyRecord record);
    }
}
