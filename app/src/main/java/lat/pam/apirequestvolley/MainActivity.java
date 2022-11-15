package lat.pam.apirequestvolley;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List list;
    ListView listView;
    Button connect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        connect = findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hubungi_server();
            }
        });
    }

    private void hubungi_server() {
        // URL API
        String url= Global.base_url;
        // make request
        StringRequest request = new StringRequest(
                // request method
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            // if connect
            public void onResponse(String response) {
                // get data from API
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list_data");
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject getData = jsonArray.getJSONObject(i);
                        String Provinsi = getData.getString("key");
                        String Case = getData.getString("jumlah_kasus");
                        String Death = getData.getString("jumlah_meninggal");
                        String Recover = getData.getString("jumlah_sembuh");
                        list.add("Provinsi : " + Provinsi + "\n\n" +
                                "Jumlah Kasus : " + Case + "\n\n" +
                                "Jumlah Kematian : " + Death + "\n\n" +
                                "Jumlah Sembuh : " + Recover);
                    }
                    // adapter for listview
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                // if not connect
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
}