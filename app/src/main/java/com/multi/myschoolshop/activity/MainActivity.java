package com.multi.myschoolshop.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.fragment.CartFragment;
import com.multi.myschoolshop.fragment.ConsoleFragment;
import com.multi.myschoolshop.fragment.MainFragment;
import com.multi.myschoolshop.fragment.MyOrderFragment;
import com.multi.myschoolshop.fragment.MyProfileFragment;
import com.multi.myschoolshop.fragment.SetUpFragment;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    SavedData savedData;
    ImageView bgapp, clover,imageView;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;
    Button exp, setUp, console, cart, ao,order;
    TextView tvName,tvEmail,tvName2,tvSchool;
    DrawerLayout drawer;
    private boolean isClosed;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvName2=findViewById(R.id.tvName);
        tvSchool=findViewById(R.id.tvSchoolName);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        bgapp = findViewById(R.id.bgapp);
        clover = findViewById(R.id.clover);
        textsplash =findViewById(R.id.textsplash);
        texthome = findViewById(R.id.texthome);
        menus = findViewById(R.id.menus);
        bgapp.animate().translationY(-1900).setDuration(3000).setStartDelay(100);
        clover.animate().alpha(0).setDuration(3000).setStartDelay(1500);
        textsplash.animate().translationY(140).alpha(0).setDuration(3000).setStartDelay(1000);
        menus.animate().translationY(-480).setDuration(2000).setStartDelay(1000);
        texthome.startAnimation(frombottom);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        savedData=new SavedData(this);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                if(savedData.getValue("change").equals("yes")){
                    Picasso.get().load(savedData.getValue("photoUrl")).into(imageView);
                    savedData.setValue("change","no");
                }
                exp.animate().translationX(0).setDuration(500);
                ao.animate().translationX(0).setDuration(500);
                setUp.animate().translationX(0).setDuration(500);
                cart.animate().translationX(0).setDuration(500);
                order.animate().translationX(0).setDuration(500);
                console.animate().translationX(0).setDuration(500);
                imageView.animate().translationY(0).setDuration(500);
                tvName.animate().translationY(0).setDuration(500);
                tvEmail.animate().translationY(0).setDuration(500);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                exp.animate().translationX(-256).setDuration(500);
                ao.animate().translationX(400).setDuration(500);
                setUp.animate().translationX(-256).setDuration(500);
                order.animate().translationX(-256).setDuration(500);
                cart.animate().translationX(400).setDuration(500);
                console.animate().translationX(-256).setDuration(500);

                imageView.animate().translationY(-256).setDuration(500);
                tvName.animate().translationY(-256).setDuration(500);
                tvEmail.animate().translationY(-256).setDuration(500);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvName=navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail=navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        imageView=navigationView.getHeaderView(0).findViewById(R.id.imageView);
        exp=navigationView.getHeaderView(0).findViewById(R.id.home);
        setUp =navigationView.getHeaderView(0).findViewById(R.id.join);
        console =navigationView.getHeaderView(0).findViewById(R.id.console);
        cart =navigationView.getHeaderView(0).findViewById(R.id.cart);
        order =navigationView.getHeaderView(0).findViewById(R.id.btnOrder);
        ao=navigationView.getHeaderView(0).findViewById(R.id.profile);
        exp.setOnClickListener(this);
        setUp.setOnClickListener(this);
        ao.setOnClickListener(this);
        console.setOnClickListener(this);
        cart.setOnClickListener(this);
        order.setOnClickListener(this);

        if(savedData.haveValue("uid")){
            Picasso.get().load(savedData.getValue("photoUrl")).into(imageView);
            tvName.setText(savedData.getValue("name"));
            tvName2.setText(savedData.getValue("name"));
            tvSchool.setText(savedData.getValue("schoolName"));
            tvEmail.setText(savedData.getValue("email"));
            loadMain();
        }else{
            startActivity(new Intent(this,SignUpActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(isClosed){
            loadMain();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(getBaseContext(),SearchActivity.class)
                        .putExtra("query",query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mainFragment.filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
           if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String message="Download this awesome school stationary app !!\n " +
                "https://play.google.com/store/apps/details?id=com.multi.myschoolshop";
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);

        sendIntent.setType("text/plain");
        if (id == R.id.nav_share) {
            startActivity(sendIntent);
        } else if (id == R.id.nav_send) {
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        drawer.closeDrawer(GravityCompat.START);
        isClosed=true;
        switch (v.getId()){
            case R.id.console:
                loadConsole();
                break;
            case R.id.home:
                loadMain();
                break;
            case R.id.profile:
                loadProfile();
                break;
            case R.id.btnOrder:
                loadOrder();
                break;
            case R.id.join:
                loadSetUp();
                break;
            case R.id.cart:
                loadCart();
                break;
        }
    }

    private void loadOrder() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MyOrderFragment());
        ft.commit();
    }

    private void loadCart() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new CartFragment());
        ft.commit();
    }
    private void loadSetUp() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new SetUpFragment());
        ft.commit();
    }
    private void loadProfile() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MyProfileFragment());
        ft.commit();
    }
    private void loadConsole() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new ConsoleFragment());
        ft.commit();
    }
    private void loadMain() {
        isClosed=false;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mainFragment= new MainFragment();
        ft.replace(R.id.container,mainFragment);
        ft.commit();
    }
}
