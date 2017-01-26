package es.cice.dynamiclistjavathreading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView numberListTV;
    private String TAG="MainActivity";
    //NumberlistHandler es una nueva clase
    private NumberListHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberListTV =(TextView) findViewById(R.id.numericListTV);
        mHandler=new NumberListHandler();
    }

    public void starRandomList(View v){
        //lanzamos un nuevo hilo, que no es el ppal
        new Thread( new NumberListRunnable()).start();

    }

    //clase interna
    private class NumberListRunnable implements Runnable{


        @Override
        public void run() {
            Random rnd = new Random();
            //esto se ejecuta en otro hilo, al intentar acceder a numberlistTV falla, pq no es el hilo principal
            while (true){
                int value= rnd.nextInt(1000000);
                Log.d(TAG, "value: " +value);
                //numberListTV.append("\n" + value);
                Message m=mHandler.obtainMessage(mHandler.SHOW_VALUE, value);
                mHandler.sendMessage(m);
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }

    public class NumberListHandler extends Handler{
        public static final int SHOW_VALUE=1;
        public static final int SHOW_PROGRESS=2;
        public static final int HIDE_PROGRES=3;
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        //qué hace el handler cuando encuentre un nuevo mensaje en la cola
        //el parámetro es el nuevo mensaje de la cola
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //lo que el handler va a hacer depende del what
            //Esto se ejecuta en el ppal, por eso no da error al acceder al widget
            switch (msg.what){
                case SHOW_VALUE:
                        Integer value=(Integer) msg.obj;
                        numberListTV.append("\n" + value);
                    break;
                case SHOW_PROGRESS:
                    break;
                case HIDE_PROGRES:
                    break;

            }
        }
    }
}
