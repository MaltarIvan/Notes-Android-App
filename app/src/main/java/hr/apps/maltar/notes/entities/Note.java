package hr.apps.maltar.notes.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maltar on 30.8.2017..
 */

public class Note implements Parcelable {
    private Long dateLong;
    private String content;

    public Note(Long dateLong, String content) {
        this.dateLong = dateLong;
        this.content = content;
    }

    public Note(Parcel source) {
        this.dateLong = source.readLong();
        this.content = source.readString();
    }

    public Long getDateLong() {
        return dateLong;
    }

    public void setDateLong(Long dateLong) {
        this.dateLong = dateLong;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dateLong);
        dest.writeString(this.content);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
