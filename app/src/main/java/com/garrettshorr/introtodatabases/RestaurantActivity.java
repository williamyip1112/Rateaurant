package com.garrettshorr.introtodatabases;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import org.w3c.dom.Text;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name;
    private EditText cuisine;
    private EditText address;
    private SeekBar price;
    private EditText websitelink;
    private RatingBar rating;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireWidgets();
        prefillfields();
        setListeners();
    }


    private void setListeners()
    {
        save.setOnClickListener(this);
    }

    private void wireWidgets()
    {
        name = findViewById(R.id.editText_resturantactivity_name);
        cuisine = findViewById(R.id.editText_resturantactivity_cuisine);
        address = findViewById(R.id.editText_resturantactivity_address);
        websitelink = findViewById(R.id.editText_restaurantactivity_websitelink);
        price = findViewById(R.id.seekbar_restuarantactivity_price);
        rating = findViewById(R.id.ratingbar_restuarantactivity_rating);
        save = findViewById(R.id.button_restaruantactivity_save);
    }

    private void prefillfields()
    {
        Intent restaurantIntent = getIntent();
        Restaurant restaurant = restaurantIntent.getParcelableExtra(RestaurantListActivity.EXTRA_RESTAURANT);
        if(restaurant != null)
        {
            name.setText(restaurant.getName());
            websitelink.setText(restaurant.getWebsiteLink());
            address.setText(restaurant.getAddress());
            cuisine.setText(restaurant.getCuisine());
            rating.setRating((float) restaurant.getRating());
            price.setProgress(restaurant.getPrice() - 1 );
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case 0:
                ShowAlert("hello from delete item");
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void ShowAlert(String string)
    {
        }


    @Override
    public void onClick(View v)
    {
        Restaurant res = new Restaurant();
        res.setName(name.getText().toString());
        res.setCuisine(cuisine.getText().toString());
        res.setAddress(address.getText().toString());
        res.setWebsiteLink(websitelink.getText().toString());
        res.setPrice(price.getProgress());
        res.setRating(rating.getRating());

        // save object synchronously
        Restaurant savedContact = Backendless.Persistence.save( res );
        // save object asynchronously
        Backendless.Persistence.save( res, new AsyncCallback<Restaurant>() {
            public void handleResponse( Restaurant response )
            {
                // new Contact instance has been saved
                setResult(RESULT_OK);
                finish();
            }

            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });

    }
}
