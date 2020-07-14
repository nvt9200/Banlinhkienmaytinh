package com.example.banlinhkienmaytinh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.banlinhkienmaytinh.R;
import com.example.banlinhkienmaytinh.adapter.MouseAdapter;
import com.example.banlinhkienmaytinh.model.Sanpham;
import com.example.banlinhkienmaytinh.ultil.CheckConnection;
import com.example.banlinhkienmaytinh.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MouseActivity extends AppCompatActivity {

    Toolbar toolbarmouse;
    ListView lvmouse;
    MouseAdapter mouseAdapter;
    ArrayList<Sanpham> mangmouse;
    int idmouse = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    boolean limitadata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuot);

        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            Anhxa();
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();

        }else {
            CheckConnection.ShowToat_Short(getApplicationContext(),"Bạn Hãy kiểm tra lại internet");
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.banlinhkienmaytinh.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreData() {
        lvmouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangmouse.get(i));
                startActivity(intent);
            }
        });
        lvmouse.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem !=0 && isLoading == false && limitadata == false) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void Anhxa() {
        toolbarmouse = (Toolbar) findViewById(R.id.toolbarmouse);
        lvmouse = (ListView) findViewById(R.id.listviewmouse);
        mangmouse = new ArrayList<>();
        mouseAdapter = new MouseAdapter(getApplicationContext(),mangmouse);
        lvmouse.setAdapter(mouseAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();
    }

    private void GetIdloaisp() {
        idmouse = getIntent().getIntExtra("idloaisanpham",-1);
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbarmouse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmouse.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdanram+String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tenmouse = "";
                int Giamouse = 0;
                String Hinhanhmouse = "";
                String Motamouse = "";
                int Idspmouse = 0;
                if (response != null && response.length() !=2) {
                    lvmouse.removeFooterView(footerview);
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0 ; i < jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenmouse = jsonObject.getString("tensp");
                            Giamouse = jsonObject.getInt("giasp");
                            Hinhanhmouse = jsonObject.getString("hinhanhsp");
                            Motamouse = jsonObject.getString("motasp");
                            Idspmouse = jsonObject.getInt("idsanpham");
                            mangmouse.add(new Sanpham(id,Tenmouse,Giamouse,Hinhanhmouse,Motamouse,Idspmouse));
                            mouseAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    limitadata = true;
                    lvmouse.removeFooterView(footerview);
                    CheckConnection.ShowToat_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(idmouse));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvmouse.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;

            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }



}