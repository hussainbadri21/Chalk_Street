package com.hussain.chalkstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    ArrayList<String> name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.recycler);

        initViews();
  //Push
    }
    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<String>();
        name.add("1");
        //name.add("2");
        //name.add("3");

        RecyclerView.Adapter adapter = new Adapter(name);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback=new SwipeHelper((Adapter) adapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    Toast.makeText(getApplicationContext(), String.valueOf(name.get(position)), Toast.LENGTH_SHORT).show();



                }

 return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Toast.makeText(getApplicationContext(),"touched", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    });
}
}

