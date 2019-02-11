package com.garrettshorr.introtodatabases;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class RestaurantListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewRestaurant;
    private FloatingActionButton add;
    public static final String EXTRA_RESTAURANT = "The Restaurant";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        wireWidgets();
        setListeners();
        populateListView();
    }

    private void setListeners()
    {
        add.setOnClickListener(this);
    }

    private void populateListView()
    {
        //refactor to only ge tthe item that belong to the user
        //get the current user's objectID (hint: use Backendless.UserService
        //make a dataquery and use the advanced object retrieval pattern
        //to find all restaurants whose ownerID matches the user's objectID
        //sample WHERE clause with a string: name = 'Joe'

        String ownerId = Backendless.UserService.CurrentUser().getObjectId();
        String whereClause = "ownerId = ' " + ownerId + " ' ";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of( Restaurant.class).find(new AsyncCallback<List<Restaurant>>(){
            @Override
            public void handleResponse( List<Restaurant> restaurantList )
            {
                // all Restaurnt instances have been found
                RestaurantAdapter adapter = new RestaurantAdapter(
                        RestaurantListActivity.this,
                        R.layout.item_restaurantlist,
                        restaurantList);
                listViewRestaurant.setAdapter(adapter);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(RestaurantListActivity.this,
                        fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityForResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            populateListView();
        }
    }

    private void wireWidgets()
    {
        listViewRestaurant = findViewById(R.id.listview_restaurantlist);
        add = findViewById(R.id.floatingActionButton_restuarantlist_add);
    }

    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(RestaurantListActivity.this, RestaurantActivity.class);
        startActivityForResult(i, 1);


    }
}
