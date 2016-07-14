package ys.bup.lunarcalendar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.adapter.FavoriteAdapter;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarMainAt extends Activity {

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

        getDummyData();
    }

    public void getDummyData()
    {
        FavoriteEntity data = new FavoriteEntity();
        data.setMemo("누구 제사");
        data.setConvertDate("2016-04-01");
        alFavorite.add(data);

        data = new FavoriteEntity();
        data.setMemo("누구 생일");
        data.setConvertDate("2016-05-01");
        alFavorite.add(data);

        data = new FavoriteEntity();
        data.setMemo("누구 기타");
        data.setConvertDate("2016-06-21");
        alFavorite.add(data);

        data = new FavoriteEntity();
        data.setMemo("이게 무슨");
        data.setConvertDate("2016-07-16");
        alFavorite.add(data);

        adapter.notifyDataSetChanged();

    }
}
