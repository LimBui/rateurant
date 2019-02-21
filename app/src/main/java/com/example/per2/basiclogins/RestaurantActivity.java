package com.example.per2.basiclogins;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RestaurantActivity extends AppCompatActivity {

    private EditText name;
    private EditText cuisine;
    private RatingBar rating;
    private EditText address;
    private EditText website;
    private SeekBar price;
    private Button save;

    private Restaurant restaurant;

    private boolean allFieldsValid(String name, String cuisine, String address, String website) {
        if (name.length() > 0 && cuisine.length() > 0 && address.length() > 0 && website.length() > 0) {
            return true;
        }
        return false;
    }

    private void prefillFields() {
        Intent restaurantIntent = getIntent();
        restaurant = restaurantIntent.getParcelableExtra(RestaurantListActivity.EXTRA_RESTAURANT);
        if(restaurant != null){
            name.setText(restaurant.getName());
            cuisine.setText(restaurant.getCuisine());
            address.setText(restaurant.getAddress());
            website.setText(restaurant.getWebsiteLink());
            rating.setRating((float)restaurant.getRating());
            price.setProgress(restaurant.getPrice());
        }
    };

    private void saveNewRestaurant() {
        String restaurantName = name.getText().toString();
        String restaurantCuisine = cuisine.getText().toString();
        String restaurantAddress = address.getText().toString();
        String wesbsite = website.getText().toString();
        float restaurantRating = rating.getRating();
        int restaurantPrice = price.getProgress();
        if (allFieldsValid(restaurantName, restaurantCuisine, restaurantAddress, wesbsite)) {
            if(restaurant != null){
            restaurant.setName(restaurantName);
            restaurant.setWebsiteLink(wesbsite);
            restaurant.setCuisine(restaurantCuisine);
            restaurant.setAddress(restaurantAddress);
            restaurant.setRating(restaurantRating);
            restaurant.setPrice(restaurantPrice);
        } else {
            restaurant = new Restaurant(restaurantName, restaurantCuisine, restaurantRating, wesbsite, restaurantAddress, restaurantPrice);
        }


        // save object synchronously

        // save object asynchronously
        Backendless.Persistence.save(restaurant, new AsyncCallback<Restaurant>() {
            public void handleResponse(Restaurant restaurant) {
                finish();
                // new Contact instance has been saved
            }

            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(RestaurantActivity.this, "NOT SAVED", Toast.LENGTH_SHORT).show();
            }
        });

    }
        else{
        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
    }

}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        wirewidgets();
        prefillFields();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //public <Restaurant> void Backendless.Data.of( E ).save( E entity, AsyncCallback<E> responder );
                saveNewRestaurant();
                Intent newListRestaurant = new Intent(RestaurantActivity.this, RestaurantListActivity.class);
                startActivity(newListRestaurant);
            }
        });


    }







    private void wirewidgets() {
        name = findViewById(R.id.edittext_restaurantactivity_name);
        cuisine = findViewById(R.id.edittext_restaurantactivity_cuisine);
        rating = findViewById(R.id.ratingbar_restaurantactivity_rating);
        website = findViewById(R.id.edittext_restaurantactivity_website);
        address = findViewById(R.id.edittext_restaurantactivity_address);
        price = findViewById(R.id.seekbar_restaurantactivity_price);
        save= findViewById(R.id.button_restaurantactivity_save);
    }
}
