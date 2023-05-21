package com.ttcs1.adapter;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttcs1.R;
import com.ttcs1.model.Leaf;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.HomeViewHoder>{
    private List<Leaf> list;
    private ItemListener itemListener;
    public RecycleViewAdapter() {
        list = new ArrayList<>();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<Leaf> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Leaf getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public HomeViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaf,parent,false);
        return new HomeViewHoder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull HomeViewHoder holder, int position) {
        Leaf leaf = list.get(position);
        holder.name.setText(leaf.getName());
        holder.inf.setText(leaf.getInf());
        byte[] outImage=leaf.getImgBitmap();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);

        holder.img.setImageBitmap(getRoundedCornerBitmap(theImage,10));
        holder.date.setText(leaf.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView name,inf,date;
        private ImageView img;

        public HomeViewHoder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.tvName);
            inf = view.findViewById(R.id.tvInf);
            img = view.findViewById(R.id.imageView);
            date = view.findViewById(R.id.tvDate);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemListener != null){
                itemListener.onItemClick(v,getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(itemListener != null){
                itemListener.onItemLongClick(v,getAdapterPosition());
            }
            return true;
        }
    }
    public interface ItemListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        // Thêm viền xanh lá cây
        final int greenColor = 0xff9CFF7B; // Xanh lá cây
        final int strokeWidth = 10; // Độ dày của viền

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // Vẽ viền xanh lá cây
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(greenColor);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        return output;
    }

}
