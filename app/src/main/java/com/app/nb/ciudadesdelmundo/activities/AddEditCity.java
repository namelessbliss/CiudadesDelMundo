package com.app.nb.ciudadesdelmundo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.app.nb.ciudadesdelmundo.R;
import com.app.nb.ciudadesdelmundo.model.City;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class AddEditCity extends AppCompatActivity {

    private int cityId;
    private boolean isCreation;

    private City city;
    private Realm realm;

    private EditText etCityName;
    private EditText etCityDescription;
    private EditText etCityLink;
    private ImageView cityImage;
    private Button btnPreview;
    private FloatingActionButton fab;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_city);

        realm = Realm.getDefaultInstance();
        bindUIReferences();

        //Comprobar si se ve a insertar o editar
        if (getIntent().getExtras() != null) {
            cityId = getIntent().getExtras().getInt("id");
            isCreation = false;
        } else {
            isCreation = true;
        }

        setActivityTitle();

        if (!isCreation) {
            city = getCityById(cityId);
            bindDataToFields();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditNewCity();
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = etCityLink.getText().toString();
                if (link.length() > 0)
                    loadImageLinkPreview(link);
            }
        });

    }


    private void bindUIReferences() {
        etCityName = findViewById(R.id.etCityName);
        etCityDescription = findViewById(R.id.etCityDescription);
        etCityLink = findViewById(R.id.etCityImage);
        cityImage = findViewById(R.id.ivPreview);
        btnPreview = findViewById(R.id.btnPreview);
        fab = findViewById(R.id.fabEditCity);
        ratingBar = findViewById(R.id.ratingBar);
    }

    private void setActivityTitle() {
        String title = "Editar Ciudad";
        if (isCreation) title = "Registrar nueva ciudad";
        setTitle(title);
    }

    private City getCityById(int cityId) {
        return realm.where(City.class).equalTo("id", cityId).findFirst();
    }

    private void bindDataToFields() {
        etCityName.setText(city.getName());
        etCityDescription.setText(city.getDescription());
        etCityLink.setText(city.getImage());
        ratingBar.setRating(city.getStars());
        loadImageLinkPreview(city.getImage());
    }

    private void loadImageLinkPreview(String link) {
        Picasso.get().load(link).fit().into(cityImage);
    }


    private void addEditNewCity() {
        if (isValidDataForNewCity()) {
            String name = etCityName.getText().toString();
            String description = etCityDescription.getText().toString();
            String link = etCityLink.getText().toString();
            float stars = ratingBar.getRating();

            City city = new City(name, description, link, stars);
            // en caso sea una edicion pasamos el ID
            if (!isCreation) city.setId(cityId);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(city);
            realm.commitTransaction();

            goToMainActivity();
        } else {
            Toast.makeText(this, "Los datos no son validos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDataForNewCity() {
        if (etCityName.getText().toString().length() > 0
                && etCityDescription.getText().toString().length() > 0
                && etCityLink.getText().toString().length() > 0) {
            return true;
        } else
            return false;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(AddEditCity.this, MainActivity.class);
        startActivity(intent);
    }
}
