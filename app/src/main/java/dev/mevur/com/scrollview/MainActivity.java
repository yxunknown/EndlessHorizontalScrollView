package dev.mevur.com.scrollview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EndlessHorizontalScrollView scrollView;
    private List<Data> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        datas = new ArrayList<>();
        Data data = new Data();
        data.setDirection(13);
        data.setImg(R.drawable.logo_cinema);
        data.setText("电影院啊换还好还好");
        datas.add(data);
        data = new Data();
        data.setText("沃尔玛超市孙大伟哇阿信");
        data.setImg(R.drawable.logo_walmart);
        data.setDirection(46);
        datas.add(data);
        data = new Data();
        data.setDirection(182);
        data.setImg(R.drawable.logo_tip);
        data.setText("tip tak 旗舰店");
        datas.add(data);
        data = new Data();
        data.setText("肯德基南坪");
        data.setImg(R.drawable.logo_kfc);
        data.setDirection(236);
        datas.add(data);
        data = new Data();
        data.setImg(R.drawable.post);
        data.setDirection(80);
        data.setType(1);
        datas.add(data);
        data = new Data();
        data.setType(1);
        data.setImg(R.drawable.post2);
        data.setDirection(138);
        datas.add(data);
        MAdapter mAdapter = new MAdapter(datas, this);
        scrollView.setAdapter(mAdapter);
    }

    private class MAdapter extends EndlessHorizontalScrollViewAdapter {
        private List<Data> data;
        private Context context;
        private LayoutInflater layoutInflater;

        public MAdapter(@NonNull List<Data> data, @NonNull Context context) {
            this.data = data;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(this.context);
        }
        @Override
        double getDirection(int position) {
            return data.get(position).getDirection();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Data data = datas.get(position);
            if (data.getType() == 0) {
                convertView = layoutInflater.inflate(R.layout.data_item, null);
                TextView textView = convertView.findViewById(R.id.text);
                SimpleDraweeView img = convertView.findViewById(R.id.img);
                textView.setText(data.getText());
                img.setImageResource(data.getImg());
            } else if (data.getType() == 1) {
                convertView = layoutInflater.inflate(R.layout.data_item_img, null);
                SimpleDraweeView simpleDraweeView = convertView.findViewById(R.id.img_view);
                simpleDraweeView.setImageResource(data.getImg());
            }
            return convertView;
        }
    }
}
