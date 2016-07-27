package com.example.android.scarnesdice;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import java.util.Random ;
import android.os.Message;



public class DiceActivity extends Activity implements OnClickListener {

    public int user_score;
    public int computer_score;
    public int user_turn_score;
    public int computer_turn_score;
    public int turn;
    public String text_label;
    ImageView image;
    Button b1, b2, b3;
    TextView editText;
    Random r = new Random();
    Handler h2 = new Handler();

    private final int rollAnimations = 25;
    private final int delayTime = 15;
    private int roll;
    boolean trr=false;
    Thread thread;
    Runnable run = new Runnable() {

        @Override
        public void run() {
            rollDice() ;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    int i = r.nextInt(6) + 1;
                    ImageUpdate(i);
                    update(i);
                    text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
                    editText.setText(text_label);
                    if (turn == 1) {
                        rollDice() ;
                        h2.postDelayed(this, 1000);
                    }
                    else {
                        b1.setClickable(true);
                        b1.setEnabled(true);
                        b2.setClickable(true);
                        b2.setEnabled(true);
                    }
                }
            },500);
        }
    };

     Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            ImageUpdate(roll);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trr=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);

        editText = (TextView) this.findViewById(R.id.textView);
        text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
        editText.setText(text_label);
        image = (ImageView) findViewById(R.id.imageView);
        ImageUpdate(1);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

    }

    private void rollDice() {

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    roll = r.nextInt(6);
                    synchronized (getLayoutInflater()) {
                        animationHandler.sendEmptyMessage(0);
                    }
                    try {
                        Thread.sleep(delayTime);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }) ;

        thread.start();

    }

   public void ImageUpdate(int i) {

       switch (i) {
           case 1:
               image.setImageResource(R.drawable.dice1);
               break;
           case 2:
               image.setImageResource(R.drawable.dice2);
               break;
           case 3:
               image.setImageResource(R.drawable.dice3);
               break;
           case 4:
               image.setImageResource(R.drawable.dice4);
               break;
           case 5:
               image.setImageResource(R.drawable.dice5);
               break;
           case 6:
               image.setImageResource(R.drawable.dice6);
               break;
       }
   }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button:
                rollDice();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        roll = r.nextInt(6) + 1;
                        synchronized (getLayoutInflater()) {
                            animationHandler.sendEmptyMessage(0);
                        }
                        update(roll);
                        text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
                        editText.setText(text_label);
                        if (turn == 1) {

                            b1.setClickable(false);
                            b1.setEnabled(false);
                            b2.setClickable(false);
                            b2.setEnabled(false);
                            h2.postDelayed(run, 1000);
                        }
                        text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
                        editText.setText(text_label);
                    }
                },500);
                break;

            case R.id.button2:
                user_score += user_turn_score;
                user_turn_score = 0;
                turn = 1;
                text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
                editText.setText(text_label);

                b1.setClickable(false);
                b1.setEnabled(false);
                b2.setClickable(false);
                b2.setEnabled(false);
                h2.postDelayed(run, 1000);
                break;

            case R.id.button3:
                user_score =0;
                user_turn_score=0;
                computer_score = 0;
                computer_turn_score =0;
                turn = 0;
                text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
                editText.setText(text_label);
                ImageUpdate(1);
                break;

            default:

                break;
        }

    }

public void update(int i){
    if(turn==0 && i>1){
        user_turn_score +=i;
        return;
    }
    else if(turn ==0)
    {
        user_turn_score=0;
        turn=1;
        return;
    }

    if(turn==1 && i>1 && computer_turn_score <20){
        computer_turn_score+=i;
    }
    else if(turn==1 && i>1 && computer_turn_score >=20){
        computer_score+=computer_turn_score ;
        computer_turn_score=0;
        turn=0;
    }
    else{
        computer_turn_score=0;
        turn=0;
    }
    text_label = "Your Score" + "=" + user_score + "  " + "Computer Score" + "=" + computer_score;
    editText.setText(text_label);

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
