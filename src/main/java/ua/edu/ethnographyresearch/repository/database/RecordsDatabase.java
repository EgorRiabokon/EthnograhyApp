package ua.edu.ethnographyresearch.repository.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.edu.ethnographyresearch.repository.dao.RecordDao;
import ua.edu.ethnographyresearch.repository.entity.EthnographyRecord;

@Database(entities = { EthnographyRecord.class }, version = 5)
public abstract class RecordsDatabase extends RoomDatabase {

    public abstract RecordDao getRecordDao();
}
