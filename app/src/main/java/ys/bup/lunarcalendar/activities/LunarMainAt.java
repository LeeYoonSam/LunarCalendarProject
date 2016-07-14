package ys.bup.lunarcalendar.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
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
        RealmResults<FavoriteEntity> result = realm.where(FavoriteEntity.class)
                .findAll();

        if(result != null && result.size() > 0)
            alFavorite.addAll(result);
        else
        {
            FavoriteEntity data = new FavoriteEntity();
            data.setMemo("누구 제사");
            data.setYear("2016");
            data.setMonth("04");
            data.setDay("01");
            alFavorite.add(data);

            data = new FavoriteEntity();
            data.setMemo("누구 생일");
            data.setYear("2016");
            data.setMonth("05");
            data.setDay("01");
            alFavorite.add(data);

            data = new FavoriteEntity();
            data.setMemo("누구 기타");
            data.setYear("2016");
            data.setMonth("06");
            data.setDay("21");
            alFavorite.add(data);

            data = new FavoriteEntity();
            data.setMemo("이게 무슨");
            data.setYear("2016");
            data.setMonth("07");
            data.setDay("16");
            alFavorite.add(data);
        }

        adapter.notifyDataSetChanged();

    }
}
