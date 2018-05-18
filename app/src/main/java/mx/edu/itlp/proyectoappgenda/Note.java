package mx.edu.itlp.proyectoappgenda;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by MR_TODOGAMES on 15/05/2018.
 */

public class Note implements Serializable {
    private long nDateTime;
    private String nTitle;
    private String nContent;

    public Note(long nDateTime, String nTitle, String nContent) {
        this.nDateTime = nDateTime;
        this.nTitle = nTitle;
        this.nContent = nContent;
    }

    public void setnDateTime(long nDateTime) {
        this.nDateTime = nDateTime;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public void setnContent(String nContent) {
        this.nContent = nContent;
    }

    public long getnDateTime() {
        return nDateTime;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnContent() {
        return nContent;
    }

    public String getDateTimeFormated(Context context ){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"
        ,context.getResources().getConfiguration().locale);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date(nDateTime));
    }
}
