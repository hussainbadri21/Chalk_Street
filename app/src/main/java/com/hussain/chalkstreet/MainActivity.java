package com.hussain.chalkstreet;

import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity which contains the recycler view
 * and where recording and playback of video is done
 */

public class MainActivity extends AppCompatActivity  {
    ArrayList<String> name;
    FloatingActionButton play;
    int i = 2,z=0;

  private int currentCameraId=0;

Map <Integer,String> m;
    Boolean playFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.recycler);
       //Function to initialize UI components
        initViews();

    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton delete= (FloatingActionButton) findViewById(R.id.delete);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<String>();
        /*Hashmap to store position of recycler view item
         and location where the video is stored*/
        m = new HashMap<Integer, String>();

        name.add("1");

        final RecyclerView.Adapter adapter = new Adapter(name);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SwipeHelper((Adapter) adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding first element of recycler view
                name.add(String.valueOf(i));
                i++;
                adapter.notifyDataSetChanged();
            }
        });


        /*int position=0;

                play=(FloatingActionButton) child.findViewById(R.id.play);
                FloatingActionButton pause=(FloatingActionButton) child.findViewById(R.id.stop);
                FloatingActionButton delete=(FloatingActionButton) child.findViewById(R.id.delete);
                FloatingActionButton change=(FloatingActionButton) child.findViewById(R.id.change);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                z=position+1;
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFlag=true;
                    }
                });
                //Event which occurs when camera change button is clicked
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recorded) {
                            mCamera.stopPreview();

                        }

                        mCamera.release();


                        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        }
                        else {
                            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        mCamera = Camera.open(currentCameraId);


                        try {

                            mCamera.setPreviewDisplay(mHolder);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mCamera.startPreview();
                        recorded=false;
                    }


                });



            }*/

            // Toast.makeText(getApplicationContext(), String.valueOf(name.get(position)), Toast.LENGTH_SHORT).show();

        }















//Initializing Camera



    }



