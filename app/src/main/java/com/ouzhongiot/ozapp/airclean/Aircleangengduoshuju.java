package com.ouzhongiot.ozapp.airclean;

import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.tools.DensityUtil;
import com.ouzhongiot.ozapp.tools.LogTools;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Aircleangengduoshuju extends AppCompatActivity {
    LinearLayout chart,AirCleanchartgengduoshuju_shijian;
    GraphicalView chartView,chartView_shijian;
    private SharedPreferences preferencesdata;
    private String workmachineid;
    private String workmachinetype;
    private Aircleanlishishijian aircleanlishishijian;
    private TextView dami,leijishijian,fenchen,tv_aircleangengduoshuju_shineishiwai,tv_aircleangengduoshuju_mg,tv_aircleangengduoshuju_youyushiwai,
    tv_AirCleangengduoshuju_lvwangshouming_shuzhi,tv_AirCleangengduoshuju_lvwangshouming;
    private int pm25,value;
    public static final String PARAM_PM25 = "param_pm25";
    public static final String PARAM_OUTWIDE_PM25 = "param_dangqianshiwaipm25";
    public static final String PARAM_FENCHEN = "param_fenchen";
    public static final String PARAM_DAMI = "param_dami";
    public static final String PARAM_LEIJI = "param_leiji";


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:


                    //滤网寿命
                    tv_AirCleangengduoshuju_lvwangshouming_shuzhi.setText(AirCleanindex.changeFilterScreen + "");
                    tv_AirCleangengduoshuju_lvwangshouming.getLayoutParams().width = DensityUtil.dip2px(Aircleangengduoshuju.this, 320) * AirCleanindex.changeFilterScreen / 1600;


                    //绘制图表--对比
                    showChart();
                    //绘制图表--时间
                    showChart_shijian();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircleangengduoshuju);
        tv_aircleangengduoshuju_shineishiwai  = (TextView) findViewById(R.id.tv_aircleangengduoshuju_shineishiwai);
        tv_aircleangengduoshuju_mg  = (TextView) findViewById(R.id.tv_aircleangengduoshuju_mg);
        tv_aircleangengduoshuju_youyushiwai  = (TextView) findViewById(R.id.tv_aircleangengduoshuju_youyushiwai);
        tv_AirCleangengduoshuju_lvwangshouming_shuzhi = (TextView) findViewById(R.id.tv_AirCleangengduoshuju_lvwangshouming_shuzhi);
        tv_AirCleangengduoshuju_lvwangshouming = (TextView) findViewById(R.id.tv_AirCleangengduoshuju_lvwangshouming);
        preferencesdata = getSharedPreferences("data",MODE_PRIVATE);
        workmachineid = preferencesdata.getString("workmachineid","");
        workmachinetype  = preferencesdata.getString("workmachinetype","");
        if (LogTools.debug){
            LogTools.d("设备id->"+workmachineid+" 设备类型id->"+workmachinetype);
        }
        chart = (LinearLayout) findViewById(R.id.AirCleanchartgengduoshuju);
        AirCleanchartgengduoshuju_shijian = (LinearLayout) findViewById(R.id.AirCleanchartgengduoshuju_shijian);
        fenchen = (TextView) findViewById(R.id.airclean_genduoshuju_fenchen);
        dami = (TextView)findViewById(R.id.airclean_genduoshuju_dami);
        leijishijian = (TextView) findViewById(R.id.airclean_genduoshuju_leijishijian);
        if (getIntent().getExtras() != null) {
            pm25 = getIntent().getIntExtra(PARAM_PM25, 50);
            value = getIntent().getIntExtra(PARAM_OUTWIDE_PM25, 100);
            if (LogTools.debug) {
                //0
                LogTools.d("pm25->" + pm25 + "(默认50)");
                //106
                LogTools.d("室外pm25->" + value + "(默认100)");
            }
            fenchen.setText(getIntent().getStringExtra(PARAM_FENCHEN));
            dami.setText(getIntent().getStringExtra(PARAM_DAMI));
            leijishijian.setText(getIntent().getStringExtra(PARAM_LEIJI));
            if (pm25 > value) {
                tv_aircleangengduoshuju_mg.setVisibility(View.GONE);
                tv_aircleangengduoshuju_shineishiwai.setText("室外空气优于室内");
                tv_aircleangengduoshuju_youyushiwai.setText("请开窗");
            } else {
                DecimalFormat df = new DecimalFormat("######0.00");
                tv_aircleangengduoshuju_youyushiwai.setText(df.format((double) (value - pm25) / value * 100) + "");
            }
        }

        //初始化数据--历史时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = MainActivity.ip + "smarthome/air/queryDeviceHistory";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params2.add(new BasicNameValuePair("devSn", workmachineid));
                params2.add(new BasicNameValuePair("days", "7"));
                String str2 = Post.dopost(uriAPI2, params2);
                Log.wtf("这个是收到的历史时间",str2+workmachinetype+workmachineid);
                aircleanlishishijian = Aircleanlishishijian.objectFromData(str2);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

//        返回
        this.findViewById(R.id.airclean_gengduoshuju_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    onBack();
                    }
                });

    }


    private void showChart() {
        XYMultipleSeriesDataset mDataSet=getDataSet();
        XYMultipleSeriesRenderer mRefender=getRefender();
        chartView= ChartFactory.getLineChartView(this, mDataSet, mRefender);
        chart.addView(chartView);
    }
    private XYMultipleSeriesDataset getDataSet() {
        XYMultipleSeriesDataset seriesDataset=new XYMultipleSeriesDataset();
        XYSeries xySeries1=new XYSeries("室外空气质量");
//        xySeries1.add(1, 36);
//        xySeries1.add(2, 30);
//        xySeries1.add(3, 27);
//        xySeries1.add(4, 29);
//        xySeries1.add(5, 34);
//        xySeries1.add(6, 28);
//        xySeries1.add(7, 33);
//        xySeries1.add(8, 33);
//        xySeries1.add(9, 33);
//        xySeries1.add(10, 33);

        for(int i = AirCleanindex.kongqinumber,j = 1;i<12;i++,j++)
        {
//            Log.wtf("室外空气的Y轴",AirCleanindex.shiwaikongqizhiliang.get(i)+"----------------------"+AirCleanindex.kongqinumber+"i:"+i+"   j:"+j+"      这个是长度"+AirCleanindex.shiwaikongqizhiliang.size());
            xySeries1.add(j, Double.valueOf(AirCleanindex.shiwaikongqizhiliang.get(i)));
        }

        seriesDataset.addSeries(xySeries1);

        XYSeries xySeries2=new XYSeries("室内空气质量");

        for(int i = AirCleanindex.kongqinumber,j = 1;i<12;i++,j++) {
//            Log.wtf("室外空气的x轴", AirCleanindex.shineikongqizhiliang.get(i) + "~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            if (AirCleanindex.shineikongqizhiliang.get(i) == null){

            }else{
                xySeries2.add(j,AirCleanindex.shineikongqizhiliang.get(i)/4);
            }


        }
//        xySeries2.add(1, 27);
//        xySeries2.add(2, 22);
//        xySeries2.add(3, 20);
//        xySeries2.add(4, 21);
//        xySeries2.add(5, 25);
//        xySeries2.add(6, 22);
//        xySeries2.add(7, 23);
//        xySeries2.add(8, 23);
//        xySeries2.add(9, 23);
//        xySeries2.add(10, 23);
        seriesDataset.addSeries(xySeries2);

        return seriesDataset;
    }
    private XYMultipleSeriesRenderer getRefender() {
        /*描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置*/
        XYMultipleSeriesRenderer seriesRenderer=new XYMultipleSeriesRenderer();

        seriesRenderer.setChartTitleTextSize(20);//设置图表标题的字体大小(图的最上面文字)
        seriesRenderer.setMargins(new int[] { 20, 80, 60, 60 });//设置外边距，顺序为：上左下右
        //坐标轴设置
        seriesRenderer.setAxisTitleTextSize(25);//设置坐标轴标题字体的大小
        seriesRenderer.setYAxisMin(0);//设置y轴的起始值
        seriesRenderer.setYAxisMax(200);//设置y轴的最大值
        seriesRenderer.setXAxisMin(0.5);//设置x轴起始值
        seriesRenderer.setXAxisMax(12.5);//设置x轴最大值
//        seriesRenderer.setXTitle("日期");//设置x轴标题
//        seriesRenderer.setYTitle("温度");//设置y轴标题
        //颜色设置
        seriesRenderer.setApplyBackgroundColor(true);//是应用设置的背景颜色
        seriesRenderer.setLabelsColor(0xFF85848D);//设置标签颜色
        seriesRenderer.setBackgroundColor(Color.WHITE);//设置图表的背景颜色
        //缩放设置
        seriesRenderer.setZoomButtonsVisible(false);//设置缩放按钮是否可见
        seriesRenderer.setZoomEnabled(false); //图表是否可以缩放设置
        seriesRenderer.setZoomInLimitX(7);
//        seriesRenderer.setZoomRate(2);//缩放比例设置
        //图表移动设置
        seriesRenderer.setPanEnabled(false);//图表是否可以移动



        //legend(最下面的文字说明)设置
        seriesRenderer.setShowLegend(true);//控制legend（说明文字 ）是否显示
        seriesRenderer.setLegendHeight(80);//设置说明的高度，单位px
        seriesRenderer.setLegendTextSize(30);//设置说明字体大小
        //坐标轴标签设置
        seriesRenderer.setXLabelsColor(Color.GRAY);
        seriesRenderer.setYLabelsColor(0,Color.GRAY);
        seriesRenderer.setLabelsTextSize(25);//设置标签字体大小
        seriesRenderer.setXLabelsAlign(Paint.Align.CENTER);

        seriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        seriesRenderer.setShowGrid(true);
        seriesRenderer.setAxesColor(Color.GRAY);
        seriesRenderer.setGridColor(Color.LTGRAY);
        seriesRenderer.setXLabels(0);//显示的x轴标签的个数
//        seriesRenderer.addXTextLabel(1, "0点");//针对特定的x轴值增加文本标签
//        seriesRenderer.addXTextLabel(6, "12点");
//        seriesRenderer.addXTextLabel(12, "24点");

        for(int i = AirCleanindex.kongqinumber,j = 1;i<AirCleanindex.shiwaikongqishijian.size();i++,j++)
        {
//            Log.wtf("这个是时间---------------------",AirCleanindex.shiwaikongqishijian.get(i).toString()+"            "+i);
            seriesRenderer.addXTextLabel(j,AirCleanindex.shiwaikongqishijian.get(i).toString());
        }
//        seriesRenderer.addXTextLabel(4, "6/27");
//        seriesRenderer.addXTextLabel(5, "6/28");
//        seriesRenderer.addXTextLabel(6, "6/29");
//        seriesRenderer.addXTextLabel(7, "今天");
        seriesRenderer.setPointSize(6);//设置坐标点大小


        seriesRenderer.setMarginsColor(Color.WHITE);//设置外边距空间的颜色
        seriesRenderer.setClickEnabled(false);
//        seriesRenderer.setChartTitle("北京最近7天温度变化趋势图");

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer1=new XYSeriesRenderer();
        xySeriesRenderer1.setColor(0xFFFF0000);//设置注释（注释可以着重标注某一坐标）的颜色
        xySeriesRenderer1.setChartValuesTextAlign(Paint.Align.CENTER);//设置注释的位置
        xySeriesRenderer1.setChartValuesTextSize(12);//设置注释文字的大小
        xySeriesRenderer1.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer1.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer1.setLineWidth(3);
        xySeriesRenderer1.setFillPoints(true);
        xySeriesRenderer1.setColor(0xFFF46C48);//表示该组数据的图或线的颜色
        xySeriesRenderer1.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer1.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer2=new XYSeriesRenderer();
        xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer2.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer2.setColor(0xFF00C8FF);//表示该组数据的图或线的颜色
        xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer2.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小
        xySeriesRenderer2.setFillPoints(true);
        xySeriesRenderer2.setLineWidth(3);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer1);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer2);
        return seriesRenderer;
    }


    private void showChart_shijian() {
        XYMultipleSeriesDataset mDataSet=getDataSet_shijian();
        XYMultipleSeriesRenderer mRefender=getRefender_shijian();
        chartView_shijian= ChartFactory.getLineChartView(this, mDataSet, mRefender);
        AirCleanchartgengduoshuju_shijian.addView(chartView_shijian);
    }
    private XYMultipleSeriesDataset getDataSet_shijian() {
        XYMultipleSeriesDataset seriesDataset=new XYMultipleSeriesDataset();
//        XYSeries xySeries1=new XYSeries("室外空气质量");
//        xySeries1.add(1, 36);
//        xySeries1.add(2, 30);
//        xySeries1.add(3, 27);
//        xySeries1.add(4, 29);
//        xySeries1.add(5, 34);
//        xySeries1.add(6, 28);
//        xySeries1.add(7, 33);
//        seriesDataset.addSeries(xySeries1);

        XYSeries xySeries2=new XYSeries("历史使用时间");

        if (aircleanlishishijian.getState()==1||aircleanlishishijian.getState()==2){
            xySeries2.add(1, 0);
        }
        else {
            for (int i = 0; i < aircleanlishishijian.getData().size(); i++) {
                xySeries2.add(i + 1, aircleanlishishijian.getData().get(i).getUseTime() / 1000 / 3600);
            }
        }
//        xySeries2.add(1, 1);
//        xySeries2.add(2, 6);
//        xySeries2.add(3, 8);
//        xySeries2.add(4, 2);
//        xySeries2.add(5, 18);
//        xySeries2.add(6, 10);
//        xySeries2.add(7, 16);
        seriesDataset.addSeries(xySeries2);

        return seriesDataset;
    }
    private XYMultipleSeriesRenderer getRefender_shijian() {
        /*描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置*/
        XYMultipleSeriesRenderer seriesRenderer=new XYMultipleSeriesRenderer();

        seriesRenderer.setChartTitleTextSize(20);//设置图表标题的字体大小(图的最上面文字)
        seriesRenderer.setMargins(new int[] { 20, 80, 60, 60 });//设置外边距，顺序为：上左下右
        //坐标轴设置
        seriesRenderer.setAxisTitleTextSize(25);//设置坐标轴标题字体的大小
        seriesRenderer.setYAxisMin(0);//设置y轴的起始值
        seriesRenderer.setYAxisMax(24);//设置y轴的最大值
        seriesRenderer.setXAxisMin(0.5);//设置x轴起始值
        seriesRenderer.setXAxisMax(7.5);//设置x轴最大值
//        seriesRenderer.setXTitle("日期");//设置x轴标题
//        seriesRenderer.setYTitle("温度");//设置y轴标题
        //颜色设置
        seriesRenderer.setApplyBackgroundColor(true);//是应用设置的背景颜色
        seriesRenderer.setLabelsColor(0xFF85848D);//设置标签颜色
        seriesRenderer.setBackgroundColor(Color.WHITE);//设置图表的背景颜色
        //缩放设置
        seriesRenderer.setZoomButtonsVisible(false);//设置缩放按钮是否可见
        seriesRenderer.setZoomEnabled(false); //图表是否可以缩放设置
        seriesRenderer.setZoomInLimitX(7);
//        seriesRenderer.setZoomRate(0.5f);//缩放比例设置
        //图表移动设置
        seriesRenderer.setPanEnabled(false);//图表是否可以移动

        //legend(最下面的文字说明)设置
        seriesRenderer.setShowLegend(true);//控制legend（说明文字 ）是否显示
        seriesRenderer.setLegendHeight(80);//设置说明的高度，单位px
        seriesRenderer.setLegendTextSize(30);//设置说明字体大小
        //坐标轴标签设置
        seriesRenderer.setXLabelsColor(Color.GRAY);
        seriesRenderer.setYLabelsColor(0,Color.GRAY);
        seriesRenderer.setLabelsTextSize(25);//设置标签字体大小
        seriesRenderer.setXLabelsAlign(Paint.Align.CENTER);
        seriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        seriesRenderer.setShowGrid(true);
        seriesRenderer.setAxesColor(Color.GRAY);
        seriesRenderer.setGridColor(Color.LTGRAY);
        seriesRenderer.setXLabels(0);//显示的x轴标签的个数
        if (aircleanlishishijian.getState()==1||aircleanlishishijian.getState()==2){
            seriesRenderer.addXTextLabel(1, "暂无数据");
        }
        else{
        for(int i = 0;i<aircleanlishishijian.getData().size();i++){
            seriesRenderer.addXTextLabel(i+1,aircleanlishishijian.getData().get(i).getUseDate().substring(5,10) );
        }
        }
//        seriesRenderer.addXTextLabel(1, "6/24");//针对特定的x轴值增加文本标签
//        seriesRenderer.addXTextLabel(2, "6/25");
//        seriesRenderer.addXTextLabel(3, "6/26");
//        seriesRenderer.addXTextLabel(4, "6/27");
//        seriesRenderer.addXTextLabel(5, "6/28");
//        seriesRenderer.addXTextLabel(6, "6/29");
//        seriesRenderer.addXTextLabel(7, "今天");
        seriesRenderer.setPointSize(6);//设置坐标点大小


        seriesRenderer.setMarginsColor(Color.WHITE);//设置外边距空间的颜色
        seriesRenderer.setClickEnabled(false);
//        seriesRenderer.setChartTitle("北京最近7天温度变化趋势图");

//        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
//        XYSeriesRenderer xySeriesRenderer1=new XYSeriesRenderer();
//        xySeriesRenderer1.setColor(0xFFFF0000);//设置注释（注释可以着重标注某一坐标）的颜色
//        xySeriesRenderer1.setChartValuesTextAlign(Paint.Align.CENTER);//设置注释的位置
//        xySeriesRenderer1.setChartValuesTextSize(12);//设置注释文字的大小
//        xySeriesRenderer1.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
//        xySeriesRenderer1.setPointStrokeWidth(8);//坐标点的大小
//        xySeriesRenderer1.setLineWidth(3);
//        xySeriesRenderer1.setFillPoints(true);
//        xySeriesRenderer1.setColor(0xFFF46C48);//表示该组数据的图或线的颜色
//        xySeriesRenderer1.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
//        xySeriesRenderer1.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer2=new XYSeriesRenderer();
        xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer2.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer2.setColor(0xFF00C8FF);//表示该组数据的图或线的颜色
        xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer2.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小
        xySeriesRenderer2.setFillPoints(true);
        xySeriesRenderer2.setLineWidth(3);
//        seriesRenderer.addSeriesRenderer(xySeriesRenderer1);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer2);
        return seriesRenderer;
    }

    //点击返回上个页面
    public void onBack() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {


        super.onResume();
    }
}
