package boulhexanome.application_smartooz.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import boulhexanome.application_smartooz.Activities.CongratulationsCircuitEndActivity;
import boulhexanome.application_smartooz.Activities.LoginActivity;
import boulhexanome.application_smartooz.Activities.PlaceNearbyActivity;
import boulhexanome.application_smartooz.Model.CurrentCircuitTravel;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.R;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    // Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        // intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.err.println("No location perms !");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.err.println("No location perms !");
            return;
        }
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(final Location loc)
        {
            if(isBetterLocation(loc, previousBestLocation)) {
                System.out.println("************************************** Location changed");
                Place p = CurrentCircuitTravel.getInstance().getClosePlace(loc.getLatitude(), loc.getLongitude());
                if(p != null){
                    System.out.println("On y est !");
                    ArrayList<Place> places = CurrentCircuitTravel.getInstance().getCircuitEnCours().getPlaces();
                    int NOTIFICATION_ID = (int)(Math.random()*9999);
                    if(places.indexOf(p) == places.size()-1 && ((double)(CurrentCircuitTravel.getInstance().getOnEstPassePar().size()) / (double)(places.size())) > 0.5 ){
                        // on a fini le parcours -> on appelle la vue des congratulations
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(LocationService.this)
                                        .setSmallIcon(R.drawable.logo_smartooz)
                                        .setContentTitle("Félicitations vous avez fini le parcours ! Vous êtes arrivé  au point remarquable : \"" + p.getName() + "\"")
                                        .setContentText("Découvrez-en plus sur \"" + p.getName() + "\"...");
                        Intent intent = new Intent(LocationService.this, CongratulationsCircuitEndActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(LocationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(contentIntent);
                        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nManager.notify(NOTIFICATION_ID, builder.build());
                    }else{
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(LocationService.this)
                                        .setSmallIcon(R.drawable.logo_smartooz)
                                        .setContentTitle("Vous êtes arrivé  au point remarquable : \"" + p.getName() + "\"")
                                        .setContentText("Découvrez-en plus sur \"" + p.getName() + "\"...");
                        Intent intent = new Intent(LocationService.this, PlaceNearbyActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(LocationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(contentIntent);
                        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nManager.notify(NOTIFICATION_ID, builder.build());
                    }
                }
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Le GPS n'est pas activé", Toast.LENGTH_SHORT ).show();
        }

        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "GPS activé : précision accrue.", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}