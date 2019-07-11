package com.app.nb.ciudadesdelmundo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.app.nb.ciudadesdelmundo.R;
import com.app.nb.ciudadesdelmundo.adapter.CityAdapter;
import com.app.nb.ciudadesdelmundo.model.City;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<City>> {

    private Realm realm;
    private FloatingActionButton fab;

    private RealmResults<City> cities;

    private RecyclerView recyclerView;
    private CityAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        cities = realm.where(City.class).findAll();
        cities.addChangeListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = findViewById(R.id.fabAddCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditCity.class);
                startActivity(intent);
            }
        });

        setHideShowFab();


        adapter = new CityAdapter(cities, R.layout.recycler_view_item, new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(City city, int position) {
                Intent intent = new Intent(MainActivity.this, AddEditCity.class);
                intent.putExtra("id", city.getId());
                startActivity(intent);
            }
        }, new CityAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(City city, int position) {
                showAlertForRemovingCity("Borrar ciudad", "¿Estas seguro de que quieres eliminar " + city.getName() + "? ", position);
            }
        });

        recyclerView.setAdapter(adapter);
        cities.addChangeListener(this);
    }


    private void setHideShowFab() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }

    private void showAlertForRemovingCity(String title, String message, final int position) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteCity(position);
                        Toast.makeText(MainActivity.this, "Ciudad borrada", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null).show();
    }

    private void deleteCity(int position) {
        realm.beginTransaction();
        cities.get(position).deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void onChange(RealmResults<City> cities) {
        adapter.notifyDataSetChanged();
    }
}
