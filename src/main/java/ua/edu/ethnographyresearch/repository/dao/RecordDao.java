package ua.edu.ethnographyresearch.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RecordDao {
    @Query("Select * from ethnography_records ORDER BY date Desc")
    Flowable<List<EthnographyRecord>> getAllRecords();

    @Upsert
    void upsert(EthnographyRecord record);

    @Delete
    void delete(EthnographyRecord record);
}
