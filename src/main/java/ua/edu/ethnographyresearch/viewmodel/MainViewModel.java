package ua.edu.ethnographyresearch.viewmodel;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import ua.edu.ethnographyresearch.R;
import ua.edu.ethnographyresearch.repository.dao.RecordDao;
import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final RecordDao recordDao;
    public final Flowable<List<EthnographyRecord>> records;

    private final IconGenerator iconFactory;

    @Inject
    public MainViewModel(Application application, RecordDao recordDao){
        this.recordDao = recordDao;
        this.records = recordDao.getAllRecords();
        this.iconFactory = new IconGenerator(application.getApplicationContext());
        this.iconFactory.setColor(R.color.ic_launcher_background);
        this.iconFactory.setTextAppearance(R.style.EthnographyResearch_TextAppearance_Dark);
        this.iconFactory.setContentRotation(90);
    }

    public void addMarkersToMap(GoogleMap googleMap, boolean zoomToLastUpdated) {
        records.blockingFirst().stream().sorted(Comparator.comparing(o -> o.date)).forEach(record -> {
            if (!isValidForMap(record)) {
                return;
            }
            var marker = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(record.title)))
                    .position(new LatLng(record.latitude, record.longitude))
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

            googleMap.addMarker(marker);

            if(zoomToLastUpdated){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(record.latitude, record.longitude), 15));
            }
        });
    }

    public Optional<EthnographyRecord> findRecordByPos(double latitude, double longitude) {
        return records.blockingFirst().stream().filter(record -> record.latitude == latitude && record.longitude == longitude).findFirst();
    }

    public boolean isValidForMap(EthnographyRecord record){
        return !((record.title == null || record.title.isEmpty()) ||
                (record.latitude == 0 || record.longitude == 0));
    }
}
