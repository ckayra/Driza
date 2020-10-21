package com.elelco.elelcomobilesalesdriza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class checkCaricoActivity extends Activity {

    Activity activity;
    CameraSource cameraSource;
    SurfaceView cameraView;
    CheckCaricoAdapter adapter;
    String lastBarCode="";

    Toast toast;

    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;


    int numBarCodeTrovati=0;


private void playSound(){



    if (loaded) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        soundPool.play(soundID, volume, volume, 1, 0, 1f);
        Log.e("Test", "Played sound");
    }
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        activity = this;
        setContentView(R.layout.checkcarico);

        //sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.pio, 1);


        cameraView = (SurfaceView) findViewById(R.id.checkcarico_cameraview);
        final BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                    //    .setBarcodeFormats(Barcode.ALL_FORMATS)//QR_CODE)
                        .setBarcodeFormats(Barcode.QR_CODE)//QR_CODE)
                        .build();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(width, height)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                if (lastBarCode.equals(barcodes.valueAt(0).displayValue)) return;
                    lastBarCode=barcodes.valueAt(0).displayValue;

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Prodotto prod=adapter.getItemByUdc(barcodes.valueAt(0).displayValue);
                            if (prod!=null && prod.getQuantita()==0){
                                numBarCodeTrovati+=1;
                                prod.setQuantita(1);
                                adapter.notifyDataSetChanged();
                                if (numBarCodeTrovati>=3){
                                    Button btnConferma=findViewById(R.id.checkcarico_btnConferma);
                                    btnConferma.setEnabled(true);
                                }
                            }


                            playSound();

                             toast=Toast.makeText(activity, barcodes.valueAt(0).displayValue, Toast.LENGTH_SHORT  );
                            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
               /*     barcodeInfoQR.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {


                          *//*  int format=  barcodes.valueAt(0).format;
                            if (format==2) barcodeInfo39.setText(  barcodes.valueAt(0).displayValue );
                            if (format==256) barcodeInfoQR.setText(  barcodes.valueAt(0).displayValue );*//*
                        }
                    });*/

                 ///TODO ricerca barcode all'nterno della lista


                }
            }
        });

        ///*****fine barcode detector***********

        Button btnConferma=findViewById(R.id.checkcarico_btnConferma);
        btnConferma.setEnabled(false);




        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                barcodeDetector.release();
                if (toast!=null) toast.cancel();
                activity.finish();
            }
        });


        DBHelper db;
        db = DBHelper.getInstance(this);
        ArrayList<Prodotto> carico=db.getCarico();
        for (Prodotto prod : carico)
        {
            prod.setQuantita(0);
        }
        ListView lv=(ListView) findViewById(R.id.checkcarico_listRow);
         adapter = new CheckCaricoAdapter(this, R.layout.checkcarico_righe, carico);
        lv.setAdapter(adapter);
    }


    @Override
    protected void onStop () {
        super.onStop();
       if (toast!=null) toast.cancel();
    }

    private class CheckCaricoAdapter extends ArrayAdapter<Prodotto> {

        private Context _context;
        List<Prodotto> _listFull;
        List<Prodotto> _list; // header titles
       // private FattureListAdapter.ItemFilter mFilter = new FattureListAdapter.ItemFilter();

        public CheckCaricoAdapter(Context context, int textViewResourceId, List<Prodotto> prodotti)
        {
            super(context, textViewResourceId, prodotti);
            this._context = context;
            this._list = prodotti;
            this._listFull = prodotti;
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler((Activity)context));
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checkcarico_righe, null);
            TextView txtUdc=(TextView)convertView.findViewById(R.id.checkcaricorighe_udc);
            Prodotto prod=getItem(position);
            txtUdc.setText(prod.getUdc());
            if (prod.getQuantita()==1){
                convertView.setBackgroundColor(Color.CYAN);
            }
            return convertView;
        }

        @Override
        public int getCount()
        {
            if (_list==null) return 0;
            return _list.size();
        }

        @Override
        public Prodotto getItem(int p1)
        {
            return _list.get(p1);
        }

        @Override
        public long getItemId(int p1)
        {
            return p1;
        }

        public Prodotto getItemByUdc(String udc) {

            for (int i=0; i<_list.size(); i++){

              if (_list.get(i).getUdc().equals(udc) ) return _list.get(i);
            }
            return null;

        }


    }


}
