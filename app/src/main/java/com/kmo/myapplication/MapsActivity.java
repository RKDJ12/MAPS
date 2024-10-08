package com.kmo.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kmo.myapplication.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap smap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int LOCATION_REQUEST_CODE = 101;
    private Marker currentLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar y pedir permisos antes de cargar el mapa
        checkLocationPermission();
    }

    // Verificar permisos
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Si no tienes ambos permisos, pedimos ambos
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            // Si ambos permisos ya están concedidos, cargamos el mapa
            loadMap();
        }
    }

    // Cargar el mapa
    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        smap = googleMap;

        // Verificar permisos y habilitar la capa de ubicación en el mapa
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            smap.setMyLocationEnabled(true); // Activar la capa de ubicación en el mapa
        }

        // Obtener ubicación continua en tiempo real
        requestLocationUpdates();
    }

    // Solicitar actualizaciones de ubicación en tiempo real
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000) // Intervalo de actualización en tiempo real cada 5 segundos
                .setFastestInterval(2000) // Actualización más rápida cada 2 segundos
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    Location location = locationResult.getLastLocation();
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Si hay un marcador existente, lo eliminamos antes de agregar uno nuevo
                    if (currentLocationMarker != null) {
                        currentLocationMarker.remove();
                    }

                    currentLocationMarker = smap.addMarker(new MarkerOptions().position(currentLatLng).title("Ubicación actual"));
                    smap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                }
            }
        }, getMainLooper());
    }

    // Manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            // Verificar si ambos permisos fueron concedidos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Si ambos permisos fueron concedidos, cargar el mapa
                loadMap();
            } else {
                // Si no se conceden ambos permisos, manejar el caso (ej. mostrar un mensaje)
                // Aquí podrías mostrar un mensaje indicando que los permisos son necesarios
            }
        }
    }

    // Obtener la ubicación actual (llamada por primera vez al cargar el mapa)
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (currentLocationMarker != null) {
                        currentLocationMarker.remove();
                    }
                    currentLocationMarker = smap.addMarker(new MarkerOptions().position(currentLatLng).title("Estás aquí"));
                    smap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                } else {
                    // Si no se obtiene la última ubicación, pedir actualizaciones en tiempo real
                    requestLocationUpdates();
                }
            }
        });
    }
}
