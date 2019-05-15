package com.example.phuocsneaker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.phuocsneaker.R;
import com.example.phuocsneaker.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FilaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arrayfila;

    public FilaAdapter(Context context, ArrayList<Sanpham> arrayfila) {
        this.context = context;
        this.arrayfila = arrayfila;
    }

    @Override
    public int getCount() {
        return arrayfila.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayfila.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public TextView txttenfila,txtgiafila,txtmotafila;
        public ImageView imgfila;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FilaAdapter.ViewHolder viewHolder = null;
        if (view ==  null){
            viewHolder = new FilaAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_fila,null);
            viewHolder.txttenfila = view.findViewById(R.id.textviewfila);
            viewHolder.txtgiafila = view.findViewById(R.id.textviewgiafila);
            viewHolder.txtmotafila = view.findViewById(R.id.textviewmotafila);
            viewHolder.imgfila = view.findViewById(R.id.imageviewfila);
            view.setTag(viewHolder);
        }else {
            viewHolder = (FilaAdapter.ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttenfila.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiafila.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham())+ " Đ");
        viewHolder.txtmotafila.setMaxLines(2);
        viewHolder.txtmotafila.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotafila.setText(sanpham.getMotasanpham());
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgfila);

        return view;
    }
}
