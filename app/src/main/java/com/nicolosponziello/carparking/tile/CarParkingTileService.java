package com.nicolosponziello.carparking.tile;

import android.content.Intent;
import android.net.Uri;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.nicolosponziello.carparking.model.ParkManager;

public class CarParkingTileService extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onClick() {
        super.onClick();

        if(ParkManager.getInstance(getApplicationContext()).hasActiveParking()){
            Uri activityUri = Uri.parse("google.navigation:q=" + ParkManager.getInstance(getApplicationContext()).getCurrentParking().getLatitude()+","+ParkManager.getInstance(getApplicationContext()).getCurrentParking().getLongitude() +
                    "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, activityUri);
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivityAndCollapse(mapIntent);
        }
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile tile = getQsTile();

        if(ParkManager.getInstance(getApplicationContext()).hasActiveParking()){
            tile.setState(Tile.STATE_INACTIVE);
        }else{
            tile.setState(Tile.STATE_UNAVAILABLE) ;
        }
        tile.updateTile();
    }
}
