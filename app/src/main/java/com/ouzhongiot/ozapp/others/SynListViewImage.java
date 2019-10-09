//package com.ouzhongiot.ozapp.others;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ListView;
//
//import com.ouzhongiot.ozapp.R;
//
//import java.util.ArrayList;
//import java.util.List;
////异步加载viewlist
//public class SynListViewImage extends AppCompatActivity {
//    /** Called when the activity is first created. */
//    public List<String> URL;
//    ListView lv;
//    Myadapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_syn_list_view_image);
//        URL = new ArrayList<String>();
//        addurl();
//        lv = (ListView) findViewById(R.id.list);
//        adapter = new Myadapter(SynListViewImage.this, URL,lv);
//        lv.setAdapter(adapter);
//
//    }
//
//    public void addurl() {
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//        URL.add("http://114.55.5.92:8080/fan/image/A.jpg");
//    }
//}
