package de.zebrajaeger.androidpanohead2;

/**
 * Created by lars on 18.09.2016.
 */
public interface PosListener {
    void onPos(String status, float mx, float my, float wx, float wy);
}
