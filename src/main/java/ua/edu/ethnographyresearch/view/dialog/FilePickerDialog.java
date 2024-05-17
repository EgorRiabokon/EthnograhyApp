package ua.edu.ethnographyresearch.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.github.file_picker.FilePicker;
import com.github.file_picker.FileType;
import com.github.file_picker.listener.OnSubmitClickListener;

import ua.edu.ethnographyresearch.databinding.DialogFilepickerBinding;

public class FilePickerDialog extends DialogFragment {

    private OnSubmitClickListener listener;
    private DialogFilepickerBinding binding;

    public FilePickerDialog() {
    }

    public FilePickerDialog setListener(OnSubmitClickListener listener) {
        this.listener = listener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = DialogFilepickerBinding.inflate(requireActivity().getLayoutInflater());

        builder.setView(binding.getRoot());

        binding.photo.setOnClickListener(v -> {
            showMediaPicker(FileType.IMAGE, listener);
        });
        binding.video.setOnClickListener(v -> {
            showMediaPicker(FileType.VIDEO, listener);
        });
        binding.audio.setOnClickListener(v -> {
            showMediaPicker(FileType.AUDIO, listener);
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    private void showMediaPicker(FileType fileType, OnSubmitClickListener listener)
    {
        new FilePicker.Builder((AppCompatActivity) getActivity())
                .setLimitItemSelection(50)
                .setTitle("Оберіть медіа")
                .setSubmitText("Підтвердити")
                .setFileType(fileType)
                .setAccentColor(Color.parseColor("#500ABA"))
                .setOnSubmitClickListener(listener)
                .setOnItemClickListener((media, pos, adapter) -> {
                    if (!media.getFile().isDirectory()) {
                        adapter.setSelected(pos);
                    }
                })
                .buildAndShow();
    }
}
