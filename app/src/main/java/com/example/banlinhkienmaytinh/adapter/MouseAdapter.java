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

public class MouseAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraymouse;

    public MouseAdapter(Context context, ArrayList<Sanpham> arraymouse) {
        this.context = context;
        this.arraymouse = arraymouse;
    }

    @Override
    public int getCount() {
        return arraymouse.size();
    }

    @Override
    public Object getItem(int i) {
        return arraymouse.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView txttenmouse,txtgiamouse,txtmotamouse;
        public ImageView imgmouse;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view ==null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_mouse,null);
            viewHolder.txttenmouse = (TextView) view.findViewById(R.id.textviewmouse);
            viewHolder.txtgiamouse = (TextView) view.findViewById(R.id.textviewgiamouse);
            viewHolder.txtmotamouse = (TextView) view.findViewById(R.id.textviewmotamouse);
            viewHolder.imgmouse = (ImageView) view.findViewById(R.id.imageviewmouse);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttenmouse.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiamouse.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham())+ "Đ");
        viewHolder.txtmotamouse.setMaxLines(2);
        viewHolder.txtmotamouse.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotamouse.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgmouse);
        return view;
    }
}
