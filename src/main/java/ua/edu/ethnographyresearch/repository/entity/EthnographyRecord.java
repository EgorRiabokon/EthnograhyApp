package ua.edu.ethnographyresearch.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

import javax.annotation.Nullable;

import kotlinx.android.parcel.Parcelize;

@Entity(tableName = "ethnography_records")
@Parcelize
@TypeConverters(Converters.class)
public class EthnographyRecord implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    public String title;
    public String description;

    @Nullable public ArrayList<String> mediaPaths;
    public Long date;

    public double latitude = 0;
    public double longitude = 0;

    public EthnographyRecord(){
        mediaPaths = new ArrayList<>();
    }

    @Ignore
    public EthnographyRecord(String title, String description, ArrayList<String> mediaPaths, Long date, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.mediaPaths = mediaPaths;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EthnographyRecord(int id, String title, String description, ArrayList<String> mediaPaths, Long date, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.mediaPaths = mediaPaths;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EthnographyRecord(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        mediaPaths = in.createStringArrayList();
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(mediaPaths);
        if (date == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(date);
        }
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EthnographyRecord> CREATOR = new Creator<EthnographyRecord>() {
        @Override
        public EthnographyRecord createFromParcel(Parcel in) {
            return new EthnographyRecord(in);
        }

        @Override
        public EthnographyRecord[] newArray(int size) {
            return new EthnographyRecord[size];
        }
    };
}
