package com.example.phuocsneaker.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.example.phuocsneaker.R;
import com.example.phuocsneaker.adapter.AdidasAdapter;
import com.example.phuocsneaker.adapter.NikeAdapter;
import com.example.phuocsneaker.model.Sanpham;
import com.example.phuocsneaker.ultil.CheckConnection;
import com.example.phuocsneaker.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NikeActivity extends AppCompatActivity {
    Toolbar toolbarnike;
    ListView lvnike ;
    NikeAdapter nikeAdapter;
    ArrayList<Sanpham> mangnike;
    int idnike = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    boolean limitdata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nike);
        Anhxa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            Anhxa();
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại internet");
            finish();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.phuocsneaker.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void LoadMoreData() {

        lvnike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), com.example.phuocsneaker.activity.ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangnike.get(i));
                startActivity(intent);

            }
        });
        lvnike.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem !=0 && isLoading == false && limitdata == false){
                    isLoading = true;
                    NikeActivity.ThreadData threadData = new NikeActivity.ThreadData();
                    threadData.start();


                }
            }
        });
    }

    private void Anhxa() {
        toolbarnike = findViewById(R.id.toolbarnike);
        lvnike = findViewById(R.id.listviewnike);
        mangnike = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler ();
        nikeAdapter = new NikeAdapter(getApplicationContext(),mangnike);
        lvnike.setAdapter(nikeAdapter);
    }
    private void GetIdloaisp() {
        idnike = getIntent().getIntExtra("idloaisanpham",-1);
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbarnike);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarnike.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan= Server.Duongdannike+String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tennike = "";
                int Gianike = 0;
                String Hinhanhnike = "";
                String Motanike = "";
                int Idspnike = 0 ;
                if (response !=null && response.length() != 2){
                    lvnike.removeFooterView(footerview);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0 ; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tennike = jsonObject.getString("tensp");
                            Gianike = jsonObject.getInt("giasp");
                            Hinhanhnike = jsonObject.getString("hinhanhsp");
                            Motanike = jsonObject.getString("motasp");
                            Idspnike = jsonObject.getInt("idsanpham");
                            mangnike.add(new Sanpham(id,Tennike,Gianike,Hinhanhnike,Motanike,Idspnike));
                            nikeAdapter.notifyDataSetChanged();

                        }
                        nikeAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    limitdata = true;
                    lvnike.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("idsanpham",String.valueOf(idnike));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lvnike.addFooterView(footerview);
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
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
