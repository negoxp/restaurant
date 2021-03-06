package com.example.jorgeflores.restaurant;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.jorgeflores.restaurant.model.Order;
import com.example.jorgeflores.restaurant.model.Product;
import com.example.jorgeflores.restaurant.model.ProductAdd;
import com.example.jorgeflores.restaurant.model.ProductSize;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    public FirebaseDatabase database;

    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<ProductAdd> productAdds = new ArrayList<ProductAdd>();
    public static ArrayList<ProductSize> productSizes = new ArrayList<ProductSize>();
    public static Order myorder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //todo validate auth
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("products");

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Product product = snapshot.getValue(Product.class);
                    products.add(product);

                    ProductFragment fragment = new ProductFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString( "id" , Integer.toString(product.id));
                    arguments.putString( "title" , product.name);
                    arguments.putString( "description" , product.description);
                    arguments.putString( "basePrice" , "$ "+Float.toString(product.basePrice));
                    arguments.putString( "photo_cover" , product.photo_cover);
                    fragment.setArguments(arguments);

                    ft.add(R.id.fragment_container, fragment,"x_");

                }
                ft.commit();
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        productAdds.clear();
        DatabaseReference productsExtrasRef = database.getReference("products_adds");
        productsExtrasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductAdd productAdd = snapshot.getValue(ProductAdd.class);
                    productAdds.add(productAdd);
                    Log.d("productAdd:",productAdd.name_add);
                }
                ft.commit();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        productSizes.clear();
        DatabaseReference productsSizesRef = database.getReference("products_sizes");
        productsSizesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductSize productSize = snapshot.getValue(ProductSize.class);
                    productSizes.add(productSize);
                    Log.d("productAdd:",productSize.size);
                }
                ft.commit();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
    Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return(super.onCreateOptionsMenu(menu));
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                auth.signOut();
                Intent intentOut = new Intent(this, LoginActivity.class);
                this.startActivity(intentOut);
                return true;
            case R.id.checkout:
                Intent intent1 = new Intent(this, CheckoutActivity.class);
                this.startActivity(intent1);
                return true;
            case R.id.booking:
                Intent intent2 = new Intent(this, BookingActivity.class);
                this.startActivity(intent2);
                return true;
            case R.id.rating:
                Intent intent3 = new Intent(this, RestaurantCommentsActivity.class);
                this.startActivity(intent3);
                return true;
            case R.id.contactus:
                Intent intent4 = new Intent(this, AboutUsActivity.class);
                this.startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    END Menu
     */

}
