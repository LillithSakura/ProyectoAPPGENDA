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
    private String Fecha;
    private String Hora;

    public Note(long nDateTime, String nTitle, String nContent, String Fecha, String Hora) {
        this.nDateTime = nDateTime;
        this.nTitle = nTitle;
        this.nContent = nContent;
        this.Fecha = Fecha;
        this.Hora = Hora;
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

    public void setnFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public void setnHora(String Hora) {
        this.Fecha = Hora;
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

    public String getnFecha() {
        return Fecha;
    }

    public String getnHora() {
        return Hora;
    }

    public String getDateTimeFormated(Context context ){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"
        ,context.getResources().getConfiguration().locale);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date(nDateTime));
    }
}
