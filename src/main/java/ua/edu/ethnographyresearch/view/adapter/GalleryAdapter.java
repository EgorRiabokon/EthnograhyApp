package ua.edu.ethnographyresearch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.Utils;
import ua.edu.ethnographyresearch.databinding.ItemMediaAttachmentBinding;

import com.github.file_picker.FileType;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<String> paths;
    private final OnMediaClickListener listener;

    public GalleryAdapter(ArrayList<String> paths, OnMediaClickListener listener){
        this.paths = paths;
        this.listener = listener;
    }

    public ArrayList<String> getPaths(){
        return paths;
    }

    public void setPaths(ArrayList<String> paths){
        if(paths == null){
            return;
        }

        this.paths = paths;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMediaAttachmentBinding binding = ItemMediaAttachmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(paths.get(position));
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMediaAttachmentBinding binding;

        public ViewHolder(@NonNull ItemMediaAttachmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v -> {
                var pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onClick(paths.get(pos));
                }
            });
        }

        public void bind(String path) {
            var fileType = Utils.getFileType(path);
            if(fileType == FileType.IMAGE){
                binding.videoImg.setVisibility(View.GONE);
            }else if (fileType == FileType.AUDIO){
                binding.videoImg.setVisibility(View.VISIBLE);
                binding.imageView.setImageResource(R.drawable.baseline_audiotrack_24);
                return;
            }
            else{
                binding.videoImg.setVisibility(View.VISIBLE);
            }

            Glide.with(binding.getRoot().getContext()).load(path).sizeMultiplier(0.4f)
                    .into(binding.imageView);
        }
    }

    public interface OnMediaClickListener {
        void onClick(String mediaPath);
    }
}
