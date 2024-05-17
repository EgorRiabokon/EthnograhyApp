package ua.edu.ethnographyresearch.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.Toast;

import ua.edu.ethnographyresearch.MainActivity;
import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.databinding.FragmentAddRecordBinding;
import ua.edu.ethnographyresearch.view.adapter.GalleryAdapter;
import ua.edu.ethnographyresearch.view.dialog.MapViewDialog;
import ua.edu.ethnographyresearch.view.dialog.FilePickerDialog;
import ua.edu.ethnographyresearch.view.dialog.MediaViewDialog;
import ua.edu.ethnographyresearch.viewmodel.AddRecordViewModel;
import ua.edu.ethnographyresearch.viewmodel.MainViewModel;
import ua.edu.ethnographyresearch.viewmodel.RecordLiveData;

import java.util.ArrayList;
import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddRecordFragment extends Fragment {

    private FragmentAddRecordBinding binding;
    private AddRecordViewModel viewModel;
    private MainViewModel mainViewModel;

    private RecordLiveData record;

    public AddRecordFragment(){
        super(R.layout.fragment_add_record);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AddRecordViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = FragmentAddRecordBinding.bind(requireView());
        AddRecordFragmentArgs args = AddRecordFragmentArgs.fromBundle(getArguments());

        viewModel.getCurrentRecordLiveData().postValue(args.getEthnograpyRecord());
        record = viewModel.getCurrentRecordLiveData();

        binding.mediaAttachments.galleryPhoto.setHasFixedSize(true);
        binding.mediaAttachments.galleryPhoto.setAdapter(new GalleryAdapter(new ArrayList<>(), path -> {
            new MediaViewDialog().setMediaPath(path).setOnDeleteListener(mediaPath -> {
                record.removeMediaPath(mediaPath);
            }).show(getActivity().getSupportFragmentManager(), "Media View");
        }));

        binding.addRecord.setOnClickListener(v -> {
            clearTextEditFocus();
            record.setTitle(binding.recordTitle.getText().toString());
            record.setDescription(binding.recordDesc.getText().toString());
            record.getValue().date = Calendar.getInstance().getTimeInMillis();

            if(record.getValue().title == null || record.getValue().title.isEmpty()){
                Toast.makeText(getActivity(), "Неможливо зберегти запис, відсутній заголовок", Toast.LENGTH_LONG).show();
                return;
            }

            viewModel.upsertRecord();

            var action = AddRecordFragmentDirections.actionAddRecordFragmentToMainFragment();
            Navigation.findNavController(getView()).navigate(action);
        });

        binding.addMedia.setOnClickListener(v -> {
            clearTextEditFocus();
            new FilePickerDialog().setListener(files -> {
                files.stream().map(f ->
                        f.getFile().getAbsolutePath()).forEach(path -> record.addMediaPath(path));
            }).show(getActivity().getSupportFragmentManager(), "Media Picker");
        });

        binding.deleteRecord.setOnClickListener(v -> {
            clearTextEditFocus();
            viewModel.deleteRecord();
            ((MainActivity)getActivity()).navController.popBackStack();
        });

        binding.cancel.setOnClickListener(v -> {
            clearTextEditFocus();
            ((MainActivity)getActivity()).navController.popBackStack();
        });

        binding.openMap.setOnClickListener(v -> {
            clearTextEditFocus();
            new MapViewDialog().setViewModel(mainViewModel).setRecord(record.getValue()).setOnSelectMapLocationListener(pos -> {
                record.getValue().latitude = pos.latitude;
                record.getValue().longitude = pos.longitude;
            }).show(getActivity().getSupportFragmentManager(), "Location picker");
        });

        binding.recordTitle.setOnFocusChangeListener((v, f) -> {
            if(f){
                return;
            }

            record.setTitle(binding.recordTitle.getText().toString());
        });

        binding.recordDesc.setOnFocusChangeListener((v, f) -> {
            if(f){
                return;
            }

            record.setDescription(binding.recordDesc.getText().toString());
        });

        record.observe(getViewLifecycleOwner(), changes -> {
            binding.recordTitle.setText(changes.title);
            binding.recordDesc.setText(changes.description);

            if(changes.mediaPaths == null || changes.mediaPaths.size() <= 0){
                binding.mediaAttachments.getRoot().setVisibility(View.GONE);
                return;
            }

            binding.mediaAttachments.getRoot().setVisibility(View.VISIBLE);

            viewModel.gcPaths(changes);
            ((GalleryAdapter)binding.mediaAttachments.galleryPhoto.getAdapter()).setPaths(changes.mediaPaths);
        });
    }

    private void clearTextEditFocus(){
        binding.recordTitle.clearFocus();
        binding.recordDesc.clearFocus();
    }
}