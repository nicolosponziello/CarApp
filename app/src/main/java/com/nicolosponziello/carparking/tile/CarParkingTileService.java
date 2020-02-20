package com.nicolosponziello.carparking.tile;

import android.content.Intent;
import android.net.Uri;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.nicolosponziello.carparking.model.ParkManager;

/**
 * gestisce il Tile che avvia la navigazione al parcheggio se c'è una posizione salvata
 */
public class CarParkingTileService extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onClick() {
        super.onClick();
        //il click va gestito solo se l'utente ha una posizione attualmente salvata
        if(ParkManager.getInstance(getApplicationContext()).hasActiveParking()){
            Uri activityUri = Uri.parse("google.navigation:q=" + ParkManager.getInstance(
                    getApplicationContext()).getCurrentParking().getLatitude()+","
                    +ParkManager.getInstance(getApplicationContext()).getCurrentParking().getLongitude() +
                    "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, activityUri);
            //flag necessario per aprire un'activity da un Tile
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //il package è necessario dal momento che l'activity è di un'altra app
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivityAndCollapse(mapIntent);
        }
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile tile = getQsTile();
        //se non cè una posizione salvata, disabilita il Tile
        if(ParkManager.getInstance(getApplicationContext()).hasActiveParking()){
            tile.setState(Tile.STATE_INACTIVE);
        }else{
            tile.setState(Tile.STATE_UNAVAILABLE) ;
        }
        tile.updateTile();
    }
}
