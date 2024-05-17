package ua.edu.ethnographyresearch.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.databinding.FragmentMainBinding;
import ua.edu.ethnographyresearch.viewmodel.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel viewModel;
    private FragmentMainBinding binding;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = FragmentMainBinding.bind(requireView());

        binding.navigation.setOnItemSelectedListener(item -> {
            setFragment(item.getItemId());
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setFragment(binding.navigation.getSelectedItemId());
    }

    private void setFragment(int id){
        if(id == R.id.records_list){
            replaceFragment(new RecordsListFragment());
        }else if (id == R.id.records_map){
            replaceFragment(new RecordsMapFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment).commit();
    }
}