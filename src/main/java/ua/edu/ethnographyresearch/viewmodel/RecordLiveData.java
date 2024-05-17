package ua.edu.ethnographyresearch.viewmodel;

import androidx.lifecycle.LiveData;

import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

public class RecordLiveData extends LiveData<EthnographyRecord> {

    public RecordLiveData(@Nullable EthnographyRecord record){
        super(record);
    }

    public void setTitle(String title) {
        getValue().title = title;
        postValue(getValue());
    }

    public void setDescription(String description) {
        getValue().description = description;
        postValue(getValue());
    }

    public void addMediaPath(String path) {
        if(getValue().mediaPaths == null){
            getValue().mediaPaths = new ArrayList<>();
        }

        if(getValue().mediaPaths.stream().anyMatch(rpath -> Objects.equals(rpath, path))){
            return;
        }

        getValue().mediaPaths.add(path);
        postValue(getValue());
    }

    public void removeMediaPath(String path) {
        if(getValue().mediaPaths == null){
            return;
        }

        getValue().mediaPaths.remove(path);
        postValue(getValue());
    }
    
    public void postValue(EthnographyRecord record){
        super.postValue(record);
    }
}
