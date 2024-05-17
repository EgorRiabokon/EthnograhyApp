package ua.edu.ethnographyresearch.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;
import ua.edu.ethnographyresearch.viewmodel.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapViewDialog extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    private MainViewModel mainViewModel;
    private EthnographyRecord record;
    private OnSelectMapLocation listener;

    public MapViewDialog setViewModel (MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        return this;
    }

    public MapViewDialog setRecord (EthnographyRecord record) {
        this.record = record;
        return this;
    }

    public MapViewDialog setOnSelectMapLocationListener (OnSelectMapLocation listener){
        this.listener = listener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_location_picker, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        rootView.findViewById(R.id.close_mapview).setOnClickListener(v -> {
            MapViewDialog.this.dismiss();
        });

        rootView.findViewById(R.id.apply_location_btn).setOnClickListener(v -> {
            if(googleMap == null){
                return;
            }
            listener.onSelect(googleMap.getCameraPosition().target);
            MapViewDialog.this.dismiss();
        });

        return rootView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        if(mainViewModel.isValidForMap(record)){
            mainViewModel.addMarkersToMap(googleMap, false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(record.latitude, record.longitude), 15));
        }else{
            mainViewModel.addMarkersToMap(googleMap, true);
        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public interface OnSelectMapLocation {
        void onSelect(LatLng pos);
    }
}
