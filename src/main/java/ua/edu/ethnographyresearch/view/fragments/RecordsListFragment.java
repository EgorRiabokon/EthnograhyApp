package ua.edu.ethnographyresearch.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;
import android.widget.SearchView;

import ua.edu.ethnographyresearch.MainActivity;
import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.databinding.FragmentRecordsListBinding;
import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;
import ua.edu.ethnographyresearch.view.adapter.RecordAdapter;
import ua.edu.ethnographyresearch.viewmodel.MainViewModel;

import java.util.stream.Collectors;

public class RecordsListFragment extends Fragment implements RecordAdapter.OnRecordClickListener {

    private MainViewModel viewModel;
    private FragmentRecordsListBinding binding;

    public RecordsListFragment() {
        super(R.layout.fragment_records_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        binding = FragmentRecordsListBinding.bind(requireView());

        binding.recyclerviewRecords.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.recyclerviewRecords.setHasFixedSize(true);
        binding.addRecordBtn.setOnClickListener(v -> {
            var action = MainFragmentDirections.actionMainFragmentToAddRecordFragment(new EthnographyRecord());
            ((MainActivity)getActivity()).navController.navigate(action);
        });

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterView(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterView(newText);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        RecordAdapter adapter = new RecordAdapter(viewModel.records.blockingFirst(), RecordsListFragment.this);
        binding.recyclerviewRecords.setAdapter(adapter);
    }

    @Override
    public void onClick(EthnographyRecord record) {
        var action = MainFragmentDirections.actionMainFragmentToAddRecordFragment(record);
        ((MainActivity)getActivity()).navController.navigate(action);
    }

    private void filterView(String text) {
        RecordAdapter adapter = new RecordAdapter(viewModel.records.blockingFirst().stream().filter(r -> r.title.contains(text)).collect(Collectors.toList()), RecordsListFragment.this);
        binding.recyclerviewRecords.setAdapter(adapter);
    }
}