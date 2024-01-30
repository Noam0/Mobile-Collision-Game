package com.example.colisiongame;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.colisiongame.Logic.GameManager;
import com.example.colisiongame.Model.Obstacle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView main_IMG_character;
    private  ShapeableImageView main_IMG_obstacle;
    private ShapeableImageView[] main_IMG_hearts;
    private GridLayout MAIN_LAYOUT_GRID;
    private RelativeLayout[][] relativeLayouts;
    private FloatingActionButton main_button_left;
    private FloatingActionButton main_button_right;
    private GameManager gameManager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isGameRunning = true;
    private static final String TAG = "ObstacleMovement";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        relativeLayouts = getAllRelativeLayouts(); //find all relativeLayouts from the grid layout matrix
        main_button_left.setOnClickListener(view -> moveLeft());
        main_button_right.setOnClickListener(view -> moveRight());
        gameManager = new GameManager();
        startGameLoop();


    }




    public void findViews(){
        main_IMG_character = findViewById(R.id.main_IMG_character);
       // main_IMG_obstacle = findViewById(R.id.main_IMG_Obstacle);
        MAIN_LAYOUT_GRID = findViewById(R.id.MAIN_LAYOUT_GRID);
        main_button_left = findViewById(R.id.main_button_left);
        main_button_right = findViewById(R.id.main_button_right);

        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

    }


    private RelativeLayout[][] getAllRelativeLayouts() {
        int rowCount = MAIN_LAYOUT_GRID.getRowCount();
        int columnCount = MAIN_LAYOUT_GRID.getColumnCount();

        RelativeLayout[][] relativeLayouts = new RelativeLayout[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                int linearIndex = i * columnCount + j;
                View childView = MAIN_LAYOUT_GRID.getChildAt(linearIndex);

                if (childView instanceof RelativeLayout) {
                    relativeLayouts[i][j] = (RelativeLayout) childView;

                }
            }
        }

        return relativeLayouts;
    }



    public void moveRight() {
        int currentLane = gameManager.getMainCharacter().getPositionX();
        int newLane = currentLane + 1;


        if (newLane < MAIN_LAYOUT_GRID.getColumnCount()) {
            RelativeLayout currentRelativeLayout = relativeLayouts[6][currentLane];
            currentRelativeLayout.removeView(main_IMG_character);
            RelativeLayout newRelativeLayout = relativeLayouts[6][newLane];
            newRelativeLayout.addView(main_IMG_character);
            gameManager.getMainCharacter().setPositionX(newLane);

        }
    }
    public void moveLeft() {

        int currentLane = gameManager.getMainCharacter().getPositionX();
        int newLane = currentLane - 1;


        if (newLane >= 0) {
            RelativeLayout currentRelativeLayout = relativeLayouts[6][currentLane];
            currentRelativeLayout.removeView(main_IMG_character);

            RelativeLayout newRelativeLayout = relativeLayouts[6][newLane];
            newRelativeLayout.addView(main_IMG_character);
            // Update the lane in the game manager
            gameManager.getMainCharacter().setPositionX(newLane);



        }


    }





    private void startGameLoop() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!isGameRunning) {
                    return; // Stop the loop if the game is not running
                }

                ObstacleViewMovement(); //move the obstacle one line below each time
                startGameLoop();

                int randomDisplay = generateRandomNumber();
                if (randomDisplay == 1 && gameManager.getObstacles().size() < gameManager.getMaxNumberOfObstacles()){
                    createNewObstacle();
                }
                Log.d(TAG, "number of obstacles right now: " + gameManager.getObstacles().size());
            }
        }, 750); // TIMIMG OF THE OBSTACLES
    }

    public void ObstacleViewMovement() {
        for (Obstacle obstacle : gameManager.getObstacles()) {
            int posX = obstacle.getPositionX();
            int posY = obstacle.getPositionY();

            if (posY < MAIN_LAYOUT_GRID.getRowCount()) {

                //remove the obstacle image from the grid layouts.
                RelativeLayout currentRelativeLayout = relativeLayouts[posY][posX];
                currentRelativeLayout.removeView(obstacle.getShapeableImageView());

                // Update the obstacle's position
                int newPosY = posY + 1;
                obstacle.setPositionY(newPosY);

                if (newPosY < gameManager.getNumberOfRows()) {
                    //add the obstacle image to next line in the grid layouts.
                    RelativeLayout newRelativeLayout = relativeLayouts[newPosY][posX];
                    newRelativeLayout.addView(obstacle.getShapeableImageView());

                }else if(newPosY == relativeLayouts.length){
                    //update the obstacle image to the start of the rows in the grid layouts.
                    obstacle.setToStartOfRoad();
                    int newXPos = obstacle.getPositionX();
                    RelativeLayout newRelativeLayout = relativeLayouts[0][newXPos];
                    newRelativeLayout.addView(obstacle.getShapeableImageView());
                }

                if(obstacle.getPositionY() == gameManager.getNumberOfRows()-2) {
                    //if the obstacle is in the same line of the main character, check collosion
                    checkCollosion(obstacle);
                }
            }
        }
    }

    public void createNewObstacle(){
        ShapeableImageView obstacleImageView = new ShapeableImageView(this);
        obstacleImageView.setImageResource(R.drawable.terrorist2); // Set your obstacle image resource here
        Obstacle obstacle = new Obstacle(generateRandomNumber(),obstacleImageView);

        // Set layout parameters as needed
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        obstacleImageView.setLayoutParams(layoutParams);

        // Get the position of the obstacle
        int posX = obstacle.getPositionX();
        int posY = obstacle.getPositionY();

        // Add the obstacle to the game manager
        gameManager.addObstacle(obstacle);

        // Add the ShapeableImageView to the layout at the appropriate position
        if (posY < relativeLayouts.length) {
            RelativeLayout newRelativeLayout = relativeLayouts[posY][posX];
            newRelativeLayout.addView(obstacleImageView);
        }


    }



    public void checkCollosion(Obstacle obstacle){

        boolean collosion = gameManager.checkCollosionInGameManager(obstacle);
        if(collosion) {
            refreshHeartImages();
            vibration();
            createToast();
            //sound
        }

    }


    public void refreshHeartImages(){

            if(gameManager.getLife() >= 0) {
                main_IMG_hearts[gameManager.getNumOfCollosions()-1].setVisibility(View.INVISIBLE);
            }

    }

    public int generateRandomNumber() {

        Random random = new Random();
        // Generate a random number between 0 and 2
        int randomNumber = random.nextInt(3);
        return randomNumber;
    }


    public void vibration(){

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }

    }

    public void createToast(){
        CharSequence text = "oops!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this , text, duration);
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isGameRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGameRunning) {
            isGameRunning = true;
            startGameLoop();
        }
    }
}


