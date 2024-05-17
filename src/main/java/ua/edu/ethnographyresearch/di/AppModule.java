package ua.edu.ethnographyresearch.di;

import android.content.Context;

import androidx.room.Room;

import ua.edu.ethnographyresearch.repository.dao.RecordDao;
import ua.edu.ethnographyresearch.repository.database.RecordsDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public RecordsDatabase provideRoomDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, RecordsDatabase.class, "RecordsDatabase")
                .fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    public RecordDao provideRecordDao(RecordsDatabase db) {
        return db.getRecordDao();
    }
}
