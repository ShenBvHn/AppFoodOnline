package com.example.appfoodv2.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.example.appfoodv2.Model.SanPhamModels;
import com.example.appfoodv2.Model.SanPhamSearchModel;
import com.example.appfoodv2.Utils.Utils;
import com.example.appfoodv2.View.Bill.ContentProDuctActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.example.appfoodv2.R;
import com.example.appfoodv2.ThongtinungdungFragment;
import com.example.appfoodv2.View.Account.SignInActivity;
import com.example.appfoodv2.View.Bill.CartActivity;
import com.example.appfoodv2.View.FragMent.FragMent_Bill;
import com.example.appfoodv2.View.FragMent.FragMent_Home;
import com.example.appfoodv2.View.FragMent.FragMent_ProFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements FragMent_Home.FragMent_HomeListener {
    private NavigationView navigationView;
    //    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FloatingSearchView mFloatingSearchView;
    private Fragment fm;
    private FirebaseAuth firebaseAuth;
    //    private EditText editsearch;
    private TextView tvusername, tvemail;
    private CircleImageView imaProfile;

    public static CountDownTimer countDownTimer;

    private FirebaseFirestore db;

    private final List<SanPhamSearchModel> mDataSearch = new ArrayList<>();
    private List<SanPhamModels> mDataAll = new ArrayList<>();

    public interface OnFindSuggestionsListener {
        void onResults(List<SanPhamSearchModel> results);
    }

    private String mLastQuery = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitWidget();
        Init();
        setProFile();
    }

    private void setProFile() {
        db = FirebaseFirestore.getInstance();
        tvemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        db.collection("thongtinUser").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Profile")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            if (documentSnapshot != null) {
                                try {
                                    tvusername.setText(documentSnapshot.getString("hoten").length() > 0 ?
                                            documentSnapshot.getString("hoten") : "");

                                    if (documentSnapshot.getString("avatar").length() > 0) {
                                        Picasso.get().load(documentSnapshot.getString("avatar").trim()).into(imaProfile);
                                    }
                                } catch (Exception e) {
                                    Log.d("ERROR", e.getMessage());
                                }
                            }
                        }
                    }
                });
        db.collection("SanPham").
                get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() > 0) {
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            mDataSearch.add(new SanPhamSearchModel(d.getId(), d.getString("tensp"),
                                    d.getLong("giatien"), d.getString("hinhanh"),
                                    d.getString("loaisp"), d.getString("mota"),
                                    d.getLong("soluong"), d.getString("hansudung"),
                                    d.getLong("type"), d.getString("trongluong")));
                            mDataAll.add(new SanPhamModels(d.getId(), d.getString("tensp"),
                                    d.getLong("giatien"), d.getString("hinhanh"),
                                    d.getString("loaisp"), d.getString("mota"),
                                    d.getLong("soluong"), d.getString("hansudung"),
                                    d.getLong("type"), d.getString("trongluong")));
                        }
                    }
                });
    }

    private void Init() {
        fm = new FragMent_Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm).commit();
        //Check user phân quyền tk đang nhap va chua dang nhap
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser f = firebaseAuth.getCurrentUser();
        if (f != null) { // chua dang nhap
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_logined);
        } else { // da dang nhap chuyen sang menu chính
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_dashboard);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //ánh xạ view và bắt sự kiện
                switch (item.getItemId()) {
                    case R.id.home:
                        fm = new FragMent_Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm, "Home").commit();
                        break;
                    case R.id.dangnhap:
                        startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                        break;
                    case R.id.lienhe:
                        startActivity(new Intent(HomeActivity.this, ContactActivity.class));
                        break;
                    case R.id.your_bill:
                        fm = new FragMent_Bill();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm, "Bill").commit();
                        break;
                    case R.id.your_cart:
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                        break;
                    case R.id.your_profile:
                        fm = new FragMent_ProFile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm, "Profile").commit();
                        break;
                    case R.id.signout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                        finish();
                        break;
                    case R.id.danhmuc:
                        startActivity(new Intent(HomeActivity.this, ThongKeDanhMucActivity.class));
                        break;
                    case R.id.thongtinungdung:
                        fm = new ThongtinungdungFragment();
                        break;

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    private void InitWidget() {
        navigationView = findViewById(R.id.navigationview);
        View headerLayout = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawerlayout);
        tvusername = headerLayout.findViewById(R.id.tvusername);
        tvemail = headerLayout.findViewById(R.id.tvemail);
        imaProfile = headerLayout.findViewById(R.id.profile_image);
        mFloatingSearchView = findViewById(R.id.floating_search_view);
        mFloatingSearchView.attachNavigationDrawerToMenuButton(drawerLayout);
        setupSearchBar();
    }

    private void setupSearchBar() {
        mFloatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (!oldQuery.equals("") && newQuery.equals("")) {
                mFloatingSearchView.clearSuggestions();
            } else {
                mFloatingSearchView.showProgress();
                findSuggestions(newQuery, 5,
                        250, results -> {
                            mFloatingSearchView.swapSuggestions(results);
                            mFloatingSearchView.hideProgress();
                        });
            }
        });
        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                SanPhamSearchModel model = (SanPhamSearchModel) searchSuggestion;
                for (SanPhamModels sp : mDataAll) {
                    if (sp.getId().equals(model.getId()) && sp.getTensp().equals(model.getBody())) {
                        Intent intent = new Intent(HomeActivity.this, ContentProDuctActivity.class);
                        intent.putExtra("SP", sp);
                        startActivity(intent);
                        break;
                    }
                }
                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
            }
        });
        mFloatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mFloatingSearchView.swapSuggestions(getHistory(3));
            }

            @Override
            public void onFocusCleared() {
                mFloatingSearchView.setSearchBarTitle(mLastQuery);
            }
        });
        mFloatingSearchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) -> {
            SanPhamSearchModel colorSuggestion = (SanPhamSearchModel) item;
            String textColor = "#000000";
            String textLight = "#787878";
            if (colorSuggestion.isIsHistory()) {
                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_history_black_24dp, null));
                Util.setIconColor(leftIcon, Color.parseColor(textColor));
                leftIcon.setAlpha(.36f);
            } else {
                leftIcon.setAlpha(0.0f);
                leftIcon.setImageDrawable(null);
            }
            textView.setTextColor(Color.parseColor(textColor));
            String text = colorSuggestion.getBody()
                    .replaceFirst(mFloatingSearchView.getQuery(),
                            "<font color=\"" + textLight + "\">" + mFloatingSearchView.getQuery() + "</font>");
            textView.setText(Html.fromHtml(text));
        });
    }

    public void findSuggestions(String query, final int limit, final long simulatedDelay,
                                final OnFindSuggestionsListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resetSuggestionsHistory();
                List<SanPhamSearchModel> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {
                    for (SanPhamSearchModel suggestion : mDataSearch) {
                        String name = suggestion.getBody().toLowerCase();
                        if (name.contains(constraint.toString().toLowerCase())
                                || Utils.unsignedString(name).contains(constraint.toString().toLowerCase())) {
                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }
                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, (lhs, rhs) -> lhs.isIsHistory() ? -1 : 0);
                results.values = suggestionList;
                results.count = suggestionList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<SanPhamSearchModel>) results.values);
                }
            }
        }.filter(query);
    }

    public void resetSuggestionsHistory() {
        for (SanPhamSearchModel colorSuggestion : mDataSearch) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public List<SanPhamSearchModel> getHistory(int count) {
        List<SanPhamSearchModel> suggestionList = new ArrayList<>();
        SanPhamSearchModel colorSuggestion;
        for (int i = 0; i < mDataSearch.size(); i++) {
            colorSuggestion = mDataSearch.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    @Override
    public void onBackPressed() {
        if (mFloatingSearchView.setSearchFocused(false)) {
            super.onBackPressed();
        }
    }

    @Override
    public void onButtonClick() {
        Intent intent = new Intent(HomeActivity.this, ThongKeDanhMucActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProFile();
    }
}
