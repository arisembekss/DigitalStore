package com.dtech.digitalstore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtech.digitalstore.R;
import com.dtech.digitalstore.data.FieldMenu;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by aris on 24/08/17.
 */

public class AdapterMenu extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<FieldMenu> data = Collections.emptyList();
    FieldMenu current;
    int currentPos = 0;

    public AdapterMenu(Context context, List<FieldMenu> data) {
        this.context = context;
        //inflater = LayoutInflater.from(context);

        this.data = data;
        Log.d("data size", String.valueOf(data.size()));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_menu, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        FieldMenu current = data.get(position);
        myHolder.keterangan.setText(current.keterangan);
        myHolder.nama.setText(current.nama);
        myHolder.harga.setText(current.harga);
        Picasso.with(context).load(current.foto)
                .error(R.mipmap.ic_launcher_round)
                .placeholder(R.mipmap.ic_launcher)
                .into(myHolder.imgmenu);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView keterangan, nama, harga;
        ImageView imgmenu;
        ImageButton imgPesan;


        public MyHolder(View itemView) {
            super(itemView);

            keterangan = (TextView) itemView.findViewById(R.id.tketerangan);
            nama = (TextView) itemView.findViewById(R.id.tnama);
            harga = (TextView) itemView.findViewById(R.id.tharga);
            imgmenu = (ImageView) itemView.findViewById(R.id.imgmenu);
            imgPesan = (ImageButton) itemView.findViewById(R.id.imgPesan);

        }

    }
}