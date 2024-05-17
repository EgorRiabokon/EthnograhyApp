package ua.edu.ethnographyresearch.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ua.edu.ethnographyresearch.MainActivity;
import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.viewmodel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

public class RecordsMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private MainViewModel viewModel;
    private GoogleMap googleMap;

    public RecordsMapFragment() {
        super(R.layout.fragment_records_map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_records_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);

        viewModel.addMarkersToMap(googleMap, true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.d("CLICK", marker.getPosition().toString());

        var latLng = marker.getPosition();
        var recordOptional = viewModel.findRecordByPos(latLng.latitude, latLng.longitude);
        if(recordOptional.isPresent()){
            var action = MainFragmentDirections.actionMainFragmentToAddRecordFragment(recordOptional.get());
            ((MainActivity)getActivity()).navController.navigate(action);
        }

        return false;
    }
}