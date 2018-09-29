package com.example.android.n_otepad;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Note
{
    private int id = -1;
    private  String title;
    private String content;
    private Calendar dateCreated;
    private Calendar dataModified = GregorianCalendar.getInstance();


    public Note(String title,String content)
    {
        this.title = title;
        this.content = content;
    }

    public Note(int id,String title,String content)
    {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
