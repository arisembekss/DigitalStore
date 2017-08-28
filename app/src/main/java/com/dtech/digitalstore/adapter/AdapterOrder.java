package com.dtech.digitalstore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dtech.digitalstore.R;
import com.dtech.digitalstore.data.FieldMenu;
import com.dtech.digitalstore.data.FieldPesan;

import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 28/08/2017.
 */

public class AdapterOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<FieldPesan> data = Collections.emptyList();

    public AdapterOrder(Context context, List<FieldPesan> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_order, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        FieldPesan current = data.get(position);
        myHolder.itemmenu.setText(current.namamenu);
        myHolder.itemporsi.setText(current.jumlah);
        myHolder.itemcat.setText(current.keterangan);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView itemmenu, itemporsi, itemcat, itemkey;
        ImageButton itmbtn;

        public MyHolder(View itemView) {
            super(itemView);

            itemmenu = (TextView) itemView.findViewById(R.id.itemmenu);
            itemporsi = (TextView) itemView.findViewById(R.id.itemporsi);
            itemcat = (TextView) itemView.findViewById(R.id.itemcat);
            itemkey = (TextView) itemView.findViewById(R.id.tcat);
            itmbtn = (ImageButton) itemView.findViewById(R.id.itmbtn);
            itmbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }
}
