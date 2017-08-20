package com.hussain.chalkstreet;

import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private ArrayList<String> name;
    int i=0;

    /**
     * Adapter to load elements into recycler view
     * @param name -  Used to store title of the name file
     */


    public Adapter(ArrayList<String> name) {
        this.name = name;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter.ViewHolder viewHolder, int i) {

        viewHolder.tv.setText(name.get(i));


    }

    /**
     * Returns size of Recycler view
     * @return
     */
    @Override
    public int getItemCount() {
        return name.size();
    }
    public void dismiss(int pos)
    {
        name.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        SurfaceView sv;

        public ViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.title);
            sv = (SurfaceView) view.findViewById(R.id.CameraView);


        }
    }


}

