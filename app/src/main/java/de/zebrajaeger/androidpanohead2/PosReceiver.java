package de.zebrajaeger.androidpanohead2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lars on 18.09.2016.
 */
public class PosReceiver extends BroadcastReceiver {
    private List<PosListener> posListeners = new LinkedList<>();

    public boolean add(PosListener posListener) {
        return posListeners.add(posListener);
    }

    public PosListener remove(int index) {
        return posListeners.remove(index);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle x = intent.getExtras();
        String status = x.getString("status");
        float mx = x.getFloat("m.x");
        float my = x.getFloat("m.y");
        float wx = x.getFloat("w.x");
        float wy = x.getFloat("w.y");

        for(PosListener l : posListeners){
            l.onPos(status,mx,my,wx,wy);
        }
    }
}
