package com.nicolosponziello.carparking.tile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.core.app.ActivityCompat;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.model.ParkManager;

/**
 * gestisce il tile che permette di aprire l'activity per salvare una nuova posizione
 */
public class NewPositionTileService extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile tile = getQsTile();

        //disabilita il tile se c'è già una posizione attiva o non ha i permessi di localizzazione
        if(ParkManager.getInstance(getApplicationContext()).hasActiveParking() || !hasLocationPermissions()){
            tile.setState(Tile.STATE_UNAVAILABLE);
        }else{
            tile.setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(getApplicationContext(), NewParkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityAndCollapse(intent);
    }


    private boolean hasLocationPermissions(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

}
