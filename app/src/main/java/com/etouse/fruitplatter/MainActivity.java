package com.etouse.fruitplatter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FruitPlatterView fruitplatterview = (FruitPlatterView) findViewById(R.id.fruitplatterview);
        Map<String,Integer> mMap = new HashMap<>();
        mMap.put("苹果", 30);
        mMap.put("香蕉", 70);
        mMap.put("梨子", 50);
        mMap.put("荔枝", 80);
        mMap.put("黄瓜", 80);
        mMap.put("橘子", 80);
        mMap.put("板栗", 80);
        mMap.put("瓜子", 80);

        Random random = new Random(255);
        List<Integer> colrs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int r = random.nextInt();
            int g = random.nextInt();
            int b = random.nextInt();
            colrs.add(Color.rgb(r,g,b));
        }
        fruitplatterview.setTitle("水果拼盘");
        fruitplatterview.setColor(colrs);
        fruitplatterview.setData(mMap);
        fruitplatterview.startDraw();
    }
}
