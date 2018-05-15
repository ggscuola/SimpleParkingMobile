package com.scuola.simpleparking.common;

import android.widget.ImageButton;

import com.scuola.simpleparking.R;

public class Mappa {

    public int Posto = -1;
    public int Stato = -1;
    public int Piano = -1;
    public ImageButton ImageButton;


    public Mappa(int posto, int stato, int piano){

        this.Posto = posto;
        this.Stato = stato;
        this.Piano = piano;



    }
}
