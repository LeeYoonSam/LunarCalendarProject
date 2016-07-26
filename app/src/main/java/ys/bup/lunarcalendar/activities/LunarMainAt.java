package ys.bup.lunarcalendar.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;
import ys.bup.lunarcalendar.LunarCalendarApplication;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.adapter.FavoriteAdapter;
import ys.bup.lunarcalendar.entity.FavoriteEntity;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarMainAt extends BaseLoadingActivity {

    @BindView(R.id.lvFavorite)
    RecyclerView lvFavorite;

    FavoriteAdapter adapter;

    ArrayList<FavoriteEntity> alFavorite = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_lunar_main);

        ButterKnife.bind(this);
        
        // Perform injection so that when this call returns all dependencies will be available for use.
	    ((LunarCalendarApplication) getApplication()).getApplicationComponent().inject(this);

        adapter = new FavoriteAdapter(alFavorite, LunarMainAt.this);

        lvFavorite.setAdapter(adapter);
        lvFavorite.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDummyData();
    }

    public void getDummyData()
    {
        alFavorite.clear();

        RealmResults<FavoriteEntity> result = realm.where(FavoriteEntity.class)
                .findAllSorted("memo", Sort.DESCENDING);

        if(result != null && result.size() > 0)
            alFavorite.addAll(result);

        adapter.notifyDataSetChanged();

    }
}
