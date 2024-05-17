package ua.edu.ethnographyresearch.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import ua.edu.ethnographyresearch.repository.dao.RecordDao;
import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddRecordViewModel extends ViewModel {

    private final RecordDao recordDao;
    private final RecordLiveData recordLiveData;

    @Inject
    public AddRecordViewModel(Application application, RecordDao recordDao){
        this.recordDao = recordDao;
        recordLiveData = new RecordLiveData(new EthnographyRecord());
    }

    public RecordLiveData getCurrentRecordLiveData () {
        return recordLiveData;
    }

    public void upsertRecord(){
        AsyncTask.execute(() -> {
            recordDao.upsert(recordLiveData.getValue());
        });
    }

    public void deleteRecord(){
        AsyncTask.execute(() -> {
            recordDao.delete(recordLiveData.getValue());
        });
    }

    public void gcPaths(EthnographyRecord record) {
        if(record.mediaPaths == null || record.mediaPaths.size() <= 0){
            return;
        }

        for (int i = record.mediaPaths.size() - 1; i >= 0; i--) {
            if(!new File(record.mediaPaths.get(i)).exists()){
                record.mediaPaths.remove(i);
            }
        }
    }
}
