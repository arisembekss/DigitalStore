package com.dtech.digitalstore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtech.digitalstore.ConfirmActivity;
import com.dtech.digitalstore.R;
import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.data.FieldMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        DatabaseReference statusRef;
        String status, prefToko, prefMeja;
        SharedPreferences sharedPreferences;


        public MyHolder(View itemView) {
            super(itemView);

            sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
            prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
            prefMeja = (sharedPreferences.getString(Config.DECVALUE, ""));
            statusRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("statuspesan");
            statusRef.keepSynced(true);
            statusRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    status = String.valueOf(dataSnapshot.getValue());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            keterangan = (TextView) itemView.findViewById(R.id.tketerangan);
            nama = (TextView) itemView.findViewById(R.id.tnama);
            harga = (TextView) itemView.findViewById(R.id.tharga);
            imgmenu = (ImageView) itemView.findViewById(R.id.imgmenu);
            imgPesan = (ImageButton) itemView.findViewById(R.id.imgPesan);
            imgPesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nmmenu = nama.getText().toString();
                    String sharga = harga.getText().toString();
                    /*prosesPesanan(nmmenu, sharga);*/
                    Intent confirm = new Intent(context, ConfirmActivity.class);
                    confirm.putExtra("nmmenu", nmmenu);
                    confirm.putExtra("sharga", sharga);
                    context.startActivity(confirm);
                }
            });
        }

        private void prosesPesanan(String nmmenu, final String sharga) {

        }

    }
}