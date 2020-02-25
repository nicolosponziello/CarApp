package com.nicolosponziello.carparking.database;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.util.Callback;
import com.nicolosponziello.carparking.util.DataLoadingCallback;
import com.nicolosponziello.carparking.util.Utils;
import java.io.File;

public class FirebaseHandler {

    private static FirebaseHandler instance;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private Context context;

    private FirebaseHandler(Context context){
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        this.context = context;
    }

    public static FirebaseHandler getInstance(Context context){
        if(instance == null){
            instance = new FirebaseHandler(context);
        }
        return instance;
    }

    public void login(String email, String pass, LoginRegistrationActivity activity){
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                //l'utente è stato registrato con successo
                getData(new DataLoadingCallback(activity));
            }else{
                Toast.makeText(activity, "Errore durante il login. Riprovare", Toast.LENGTH_SHORT).show();
                activity.stopLoadingAnimation();
            }
        });
    }

    public void register(String email, String pass, LoginRegistrationActivity activity){
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                //l'utente è stato registrato con successo
                Toast.makeText(activity, "Restrazione completata con successo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MainActivity.class);
                FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).keepSynced(true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Errore durante la registarzione. Riprovare", Toast.LENGTH_SHORT).show();
            }
            activity.stopLoadingAnimation();
        });
    }

    public void saveData(ParkingData data){
        CollectionReference userCollection = firestore.collection(firebaseAuth.getCurrentUser().getUid());
        userCollection.document(data.getId()).set(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("firestore", "added success");
                ParkManager.getInstance(context).completeAddData(data);
            }
        });
        if(data.getPhotoPath().size() > 0){
            //ci sono immagini da salvare
            for(String path : data.getPhotoPath()) {
                Uri uri = Uri.fromFile(new File(path));
                firebaseStorage.getReference(path).putFile(uri).addOnCompleteListener(t ->{
                    if(t.isSuccessful()){
                        Log.d("CarParking", "Photo uploaded");
                    }else{
                        Log.d("CarParking", "Photo uploading error");
                    }
                });
            }
        }
    }
    public void updateData(ParkingData data){
        CollectionReference userCollection = firestore.collection(firebaseAuth.getCurrentUser().getUid());
        userCollection.document(data.getId()).set(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("firestore", "updated successfully success");
            }
        });
    }
    public void deleteData(String uuid){
        ParkingData data = ParkManager.getInstance(context).getParkingData(uuid);
        ParkManager.getInstance(context).completeDeleteData(uuid);
        CollectionReference userCollection = firestore.collection(firebaseAuth.getCurrentUser().getUid());
        userCollection.document(uuid).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("firestore", "delete success");
            }
        });
        if(data != null && data.getPhotoPath().size() > 0){
            //ci sono immagini da salvare
            for(String path : data.getPhotoPath()) {
                firebaseStorage.getReference(path).delete().addOnCompleteListener(t ->{
                    if(t.isSuccessful()){
                        Log.d("CarParking", "Photo deleted");
                    }else{
                        Log.d("CarParking", "Photo deleting error " + path);
                    }
                });
            }
        }
    }

    public void getData(Callback callback){
        Log.d("CarParking", "loading data for " + firebaseAuth.getCurrentUser().getUid());
        CollectionReference userCollection = firestore.collection(firebaseAuth.getCurrentUser().getUid());
        userCollection.get().addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                for(QueryDocumentSnapshot document : t.getResult()){
                    Log.d("store", firebaseAuth.getCurrentUser().getEmail() + " - " + document.getData().toString());
                    ParkingData data = Utils.buildParkingData(document);
                    ParkManager.getInstance(context).completeAddData(data);
                    if(data.getPhotoPath().size() > 0){
                        //scarica foto se non presenti
                        for(String path : data.getPhotoPath()){
                            File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            String filename = Utils.extractPhotoname(path) + ".jpg";
                            if(!(new File(dir, filename).exists())) {
                                File f = new File(
                                        dir,
                                        filename);
                                firebaseStorage.getReference(path).getFile(f).addOnCompleteListener(task -> {
                                    if (t.isSuccessful()) {
                                        Log.d("CarParking", "Photo " + path + " downloaded");
                                        if(data.getPhotoPath().indexOf(path) == data.getPhotoPath().size() -1){
                                            if(callback != null) {
                                                callback.onSuccess();
                                            }
                                        }
                                    } else {
                                        Log.d("CarParking", "Error downloading photo");
                                    }
                                });

                            }else{
                                Log.d("CarParking", "Image already downloaded");
                                if(callback != null) {
                                    callback.onSuccess();
                                }
                            }
                        }
                    }else{
                        if(callback != null) {
                            callback.onSuccess();
                        }
                    }
                }
            }else{
                if(callback != null) {
                    callback.onError();
                }
            }
        });
    }
}
