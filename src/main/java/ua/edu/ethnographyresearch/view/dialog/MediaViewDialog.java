package ua.edu.ethnographyresearch.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.Player;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import ua.edu.ethnographyresearch.Utils;
import ua.edu.ethnographyresearch.databinding.DialogMediaviewBinding;

import com.github.file_picker.FileType;

public class MediaViewDialog extends DialogFragment implements OnPreparedListener {

    private OnDeleteMediaClick deleteListener;
    private String mediaPath;
    private DialogMediaviewBinding binding;

    public MediaViewDialog setMediaPath (String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }

    public MediaViewDialog setOnDeleteListener(OnDeleteMediaClick deleteListener){
        this.deleteListener = deleteListener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = DialogMediaviewBinding.inflate(requireActivity().getLayoutInflater());

        builder.setView(binding.getRoot());

        if(mediaPath == null || mediaPath.isEmpty()){
            return builder.create();
        }

        if(Utils.getFileType(mediaPath) == FileType.IMAGE){
            Glide.with(getContext()).load(mediaPath).into(binding.imageView);
            binding.videoView.setVisibility(View.GONE);
        }else{
            binding.imageView.setVisibility(View.GONE);
            binding.videoView.setOnPreparedListener(this);
            binding.videoView.setMedia(Uri.parse(mediaPath));
            binding.videoView.setRepeatMode(Player.REPEAT_MODE_ALL);
        }

        binding.closeMediaview.setOnClickListener(v -> {
            MediaViewDialog.this.dismiss();
        });

        binding.deleteMedia.setOnClickListener(v -> {
            deleteListener.onClick(mediaPath);
            MediaViewDialog.this.dismiss();
        });

        return builder.create();
    }

    @Override
    public void onPrepared() {
        binding.videoView.start();
    }

    public interface OnDeleteMediaClick {
        void onClick(String path);
    }
}
