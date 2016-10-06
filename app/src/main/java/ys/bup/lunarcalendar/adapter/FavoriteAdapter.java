package ys.bup.lunarcalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.hfrecyclerview.HFRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.common.CommUtils;
import ys.bup.lunarcalendar.entity.FavoriteEntity;
import ys.bup.lunarcalendar.helper.ItemTouchHelperAdapter;
import ys.bup.lunarcalendar.helper.ItemTouchHelperViewHolder;
import ys.bup.lunarcalendar.helper.OnStartDragListener;
import ys.bup.lunarcalendar.image.CameraImageEXIF;

/**
 * Created by ys on 2016. 7. 13..
 */
public class FavoriteAdapter extends HFRecyclerView<FavoriteEntity> implements ItemTouchHelperAdapter {

    private Context context;
    private final List<FavoriteEntity> items = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;

    public FavoriteAdapter(Context context, List<FavoriteEntity> data, OnStartDragListener dragStartListener) {
        // With Header & With Footer
        super(data, true, false);

        items.clear();
        items.addAll(data);

        mDragStartListener = dragStartListener;
        this.context = context;
    }

    //region Override Get ViewHolder
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.cell_lunar_main, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
//        return new HeaderViewHolder(inflater.inflate(R.layout.cell_lunar_add, parent, false));
        return new HeaderViewHolder(inflater.inflate(R.layout.header_lunar_main, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
//        return new FooterViewHolder(inflater.inflate(R.layout.cell_lunar_add, parent, false));
        return null;
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d("ItemTouch onItemDismiss", "position: " + position);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        try {
            Log.d("ItemTouch onItemMove", "fromPosition: " + fromPosition + " toPosition: " + toPosition);
            Collections.swap(items, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

//        return true;
    }

    //endregion

    //region ViewHolder Header and Footer
    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.ivHeaderBg)
        ImageView ivHeaderBg;

        @BindView(R.id.tvHeaderTitle)
        TextView tvHeaderTitle;

        @BindView(R.id.tvHeaderDate)
        TextView tvHeaderDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLunarDate)
        TextView tvLunarDate;

        @BindView(R.id.tvSolarDate)
        TextView tvSolarDate;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlplus)
        RelativeLayout rlplus;

        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            // Start a drag whenever the handle view it touched
            itemViewHolder.ivHeaderBg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(itemViewHolder);
                    }
                    return false;
                }
            });

            FavoriteEntity item = getItem(position);

            int month = Integer.parseInt(item.showSolarDate().substring(6, 8));
            new CameraImageTask(context, itemViewHolder).execute(month);

            itemViewHolder.tvHeaderTitle.setText(item.getMemo());
            itemViewHolder.tvHeaderDate.setText(item.showSolarDate());

        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder hHolder = (HeaderViewHolder) holder;

            // 오늘날짜 음력/양력 날짜 가져오기
            final HashMap<String, String> today = CommUtils.getTodayLunarSolar();

            ((HeaderViewHolder) holder).tvLunarDate.setText(today.get("lunarDate"));
            ((HeaderViewHolder) holder).tvSolarDate.setText(today.get("solarDate"));
//            hHolder.rlplus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(context, LunarSearchAt.class);
//                    context.startActivity(i);
//                }
//            });
        } else if (holder instanceof FooterViewHolder) {

//            FooterViewHolder fHolder = (FooterViewHolder) holder;
//            fHolder.rlplus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(context, LunarSearchAt.class);
//                    context.startActivity(i);
//                }
//            });
        }
    }


    public class CameraImageTask extends AsyncTask<Integer, Void, CameraImageEXIF> {

        private Context _context;
        private ItemViewHolder _itemViewHolder;

        public CameraImageTask(Context context, ItemViewHolder itemViewHolder) {
            this._context = context;
            this._itemViewHolder = itemViewHolder;
        }

        @Override
        protected CameraImageEXIF doInBackground(Integer... params) {
            return new CameraImageEXIF(context, null, params[0]);
        }

        @Override
        protected void onPostExecute(CameraImageEXIF cameraImageEXIF) {
            super.onPostExecute(cameraImageEXIF);

            Glide.with(_context).load(cameraImageEXIF.getImageInfo()).into(_itemViewHolder.ivHeaderBg);
        }
    };

//    public FavoriteAdapter(Context context) {
//        this.context = context;
//    }
//
//    public void clearItems() {
//        this.items = new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    public void addItem(FavoriteEntity item) {
//        items.add(item);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.cell_lunar_main, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//
//        FavoriteEntity item = getItem(position);
//
//        holder.tvHeaderTitle.setText(item.getMemo());
//        holder.tvHeaderDate.setText(item.getConvertDate());
//    }
//
//    public FavoriteEntity getItem(int position) {
//        return items.get(position);
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.tvHeaderTitle)
//        TextView tvHeaderTitle;
//
//        @BindView(R.id.tvHeaderDate)
//        TextView tvHeaderDate;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//
//    public static interface HeaderRecyclerView {
//        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);
//
//        public void onBindHeaderView(RecyclerView.ViewHolder holder, int position);
//    }

}
