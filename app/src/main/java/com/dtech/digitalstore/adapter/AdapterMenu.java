package com.dtech.digitalstore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dtech.digitalstore.ConfirmActivity;
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
        Dialog dialogPesanan;


        public MyHolder(View itemView) {
            super(itemView);

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
            String scat, sbayar, sporsi;
            final TextView tdmenu, tdharga, tdtotal;
            final EditText edcatatan, edbayar, edporsi;
            NumberPicker np;
            Button btproses;
            dialogPesanan = new Dialog(context);
            dialogPesanan.setContentView(R.layout.custom_dialog_order_menu);
            tdmenu = (TextView) dialogPesanan.findViewById(R.id.tdmenu);
            tdmenu.setText(nmmenu);
            tdharga = (TextView) dialogPesanan.findViewById(R.id.tdharga);
            tdharga.setText("Rp " + sharga);
            tdtotal = (TextView) dialogPesanan.findViewById(R.id.tdtotal);
            edcatatan = (EditText) dialogPesanan.findViewById(R.id.tdecatatan);
            edbayar = (EditText) dialogPesanan.findViewById(R.id.tdeuang);
            //edporsi = (EditText) dialogPesanan.findViewById(R.id.tdjml);
            btproses = (Button) dialogPesanan.findViewById(R.id.tdbtn);
            np = (NumberPicker) dialogPesanan.findViewById(R.id.tdnp);
            np.setMinValue(0);
            //Specify the maximum value/number of NumberPicker
            np.setMaxValue(20);

            //Gets whether the selector wheel wraps when reaching the min/max value.
            np.setWrapSelectorWheel(true);

            //Set a value change listener for NumberPicker
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                    //Display the newly selected number from picker
                    tdtotal.setText("Selected Number : " + newVal);
                }
            });



            dialogPesanan.show();

        }

    }
}