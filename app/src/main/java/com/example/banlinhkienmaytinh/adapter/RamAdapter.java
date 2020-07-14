package com.example.banlinhkienmaytinh.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.banlinhkienmaytinh.R;
import com.example.banlinhkienmaytinh.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RamAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arrayram;

    public RamAdapter(Context context, ArrayList<Sanpham> arrayram) {
        this.context = context;
        this.arrayram = arrayram;
    }

    @Override
    public int getCount() {
        return arrayram.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayram.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView txttenram,txtgiaram,txtmotaram;
        public ImageView imgram;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view ==null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_ram,null);
            viewHolder.txttenram = (TextView) view.findViewById(R.id.textviewram);
            viewHolder.txtgiaram = (TextView) view.findViewById(R.id.textviewgiaram);
            viewHolder.txtmotaram = (TextView) view.findViewById(R.id.textviewmotaram);
            viewHolder.imgram = (ImageView) view.findViewById(R.id.imageviewram);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttenram.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiaram.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham())+ "Đ");
        viewHolder.txtmotaram.setMaxLines(2);
        viewHolder.txtmotaram.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotaram.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgram);
        return view;
    }
}
