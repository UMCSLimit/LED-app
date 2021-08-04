package umcs.robotics.umcsleds.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.naz013.colorslider.ColorSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import animations.Rainbow;
import games.pong.Pong;
import games.snake.Snake;
import games.snake.services.SnakeController;
import games.spaceShooter.SpaceShooter;
import games.spaceShooter.services.SpaceController;
import umcs.robotics.umcsleds.R;
import umcs.robotics.umcsleds.configFiles.Variables;
import umcs.robotics.umcsleds.dataTemplate.Animation;
import umcs.robotics.umcsleds.dataTemplate.Stage;
import umcs.robotics.umcsleds.service.AnimationCreator;
import umcs.robotics.umcsleds.service.StageSaver;
import umcs.robotics.umcsleds.service.StageSender;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public ColorSlider colorSlider;

    private LinearLayout animationOptionsBar;
    private SeekBar timeLine;

    private Snake snake;
    private SpaceShooter spaceShooter;
    private Pong pong;
    private Rainbow rainbow;

    private NavigationView mNavigationView;

    public int viewIdArr[] = {
            //R.id.view1, R.id.view2, R.id.view3, R.id.view4, R.id.view5, R.id.view6, R.id.view7, R.id.view8, R.id.view9, R.id.view10, R.id.view11,
            R.id.view12, R.id.view13,
            R.id.view14, R.id.view15, R.id.view16, R.id.view17, R.id.view18, R.id.view19, R.id.view20,
            R.id.view21, R.id.view22, R.id.view23, R.id.view24, R.id.view25, R.id.view26, R.id.view27,
            R.id.view29, R.id.view30, R.id.view31, R.id.view32, R.id.view33, R.id.view34,
            R.id.view35, R.id.view36, R.id.view37, R.id.view38, R.id.view39, R.id.view40, R.id.view41,
            R.id.view42, R.id.view43, R.id.view44, R.id.view45, R.id.view46, R.id.view47, R.id.view48,
            R.id.view49, R.id.view50, R.id.view51, R.id.view52, R.id.view53, R.id.view54, R.id.view55,
            R.id.view56, R.id.view57, R.id.view58, R.id.view59, R.id.view60, R.id.view61, R.id.view62,
            R.id.view63, R.id.view64, R.id.view65, R.id.view66, R.id.view67, R.id.view68, R.id.view69,
            R.id.view70, R.id.view71, R.id.view72, R.id.view73, R.id.view74, R.id.view75, R.id.view76,
            R.id.view77, R.id.view78, R.id.view79, R.id.view80, R.id.view81, R.id.view82, R.id.view83,
            R.id.view84, R.id.view85, R.id.view86, R.id.view87, R.id.view88, R.id.view89, R.id.view90,
            R.id.view91, R.id.view92, R.id.view93, R.id.view94, R.id.view95, R.id.view96, R.id.view97,
            R.id.view98, R.id.view99, R.id.view100, R.id.view101, R.id.view102, R.id.view103, R.id.view104,
            R.id.view105, R.id.view106, R.id.view107, R.id.view108, R.id.view109, R.id.view110, R.id.view111,
            R.id.view112, R.id.view113, R.id.view114, R.id.view115, R.id.view116, R.id.view117, R.id.view118,
            R.id.view119, R.id.view120, R.id.view121, R.id.view122, R.id.view123, R.id.view124, R.id.view125,
            R.id.view126, R.id.view127, R.id.view128, R.id.view129, R.id.view130, R.id.view131, R.id.view132,
            R.id.view133, R.id.view134, R.id.view135, R.id.view136, R.id.view137, R.id.view138, R.id.view139,
            R.id.view140};

    private ColorSlider.OnColorSelectedListener mListener = new ColorSlider.OnColorSelectedListener() {
        @Override
        public void onColorChanged(int position, int color) {
            updateView(color);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Context
        MainActivity.context = getApplicationContext();

        Variables.getInstance().scoreTextView = findViewById(R.id.scoreText);

        //Read saved stages
        StageSaver.getInstance();

        //Color slider
        setupColorSlider();

        // Hide the status bar.
        hideStatusBar();

        //Add listiners to views
        initViews();

        //Navigation Drawer
        mNavigationView = findViewById(R.id.nav_item);
        mNavigationView.setNavigationItemSelectedListener(this);

        //Hide Status Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animation options bar
        setupAnimationBar();

        initVariables();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //if(Variables.getInstance().isLiveMode)
                        StageSender.getInstance().sendActualStageToServer();
                        sleep(1000/25);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void initVariables() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Variables.getInstance().udpServerIP = sp.getString("pref_udpIpAddress", "192.168.1.5");
        Variables.getInstance().udpPort = sp.getString("pref_udpPort", "20001");
        Variables.getInstance().udpKey = sp.getString("pref_udpKey", "0000");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Variables.getInstance().isGameStoped = true;
        if (id == R.id.d_single_stage) {
            Variables.getInstance().isGameStoped = true;
            prepareDrawingStage();
            hideAnimationBar();
        } else if (id == R.id.d_animations) {
            Variables.getInstance().isGameStoped = true;
            prepareDrawingStage();
            showAndHideAnimationBar();
        } else if (id == R.id.d_snake) {
            Variables.getInstance().isGameStoped = true;
            startSnake();
        } else if (id == R.id.d_space_shooter) {
            Variables.getInstance().isGameStoped = true;
            startSpaceShooter();
        }else if(id == R.id.d_pong){
            Variables.getInstance().isGameStoped = true;
            startPONG();
        }else if (id == R.id.d_reset) {
            Variables.getInstance().isGameStoped = true;
            resetStage();
            Toast.makeText(this, "Reset is done!", Toast.LENGTH_LONG).show();
        } else if (id == R.id.d_save) {
            saveStage();
        } else if (id == R.id.d_open_stage) {
            Variables.getInstance().isGameStoped = true;
            openStage();
        } else if (id == R.id.d_open_animation) {
            Variables.getInstance().isGameStoped = true;
            openAnimation();
        } else if (id == R.id.d_remove) {
            Variables.getInstance().isGameStoped = true;
            remove();
        } else if (id == R.id.d_settings) {
            Variables.getInstance().isGameStoped = true;
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.stop_games) {
            Variables.getInstance().isGameStoped = true;
        } else if (id == R.id.raindow){
            Variables.getInstance().isGameStoped = true;
            startRainBow();
        }

        //Hide menu
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(Gravity.START);
        return false;
    }



    private void prepareDrawingStage() {
        colorSlider.setVisibility(View.VISIBLE);
        Variables.getInstance().scoreTextView.setVisibility(View.GONE);
    }


    private void startRainBow(){
            hideStatusBar();
            hideAnimationBar();
            rainbow = new Rainbow();
    }

    private void startSnake() {

        hideStatusBar();
        hideAnimationBar();

        Variables.getInstance().isGameStoped = false;

        //In the case of running 2 snakes in one time
        if(!Variables.getInstance().isGameRunning){
            if (snake != null) {
                if (snake.isGameOver()) {
                    snake = new Snake();
                }
            } else {
                snake = new Snake();
            }
        }

        Variables.getInstance().scoreTextView.setText("0 points");
        Variables.getInstance().isGameRunning = true;
        colorSlider.setVisibility(View.GONE);
        Variables.getInstance().scoreTextView.setVisibility(View.VISIBLE);
    }

    private void startSpaceShooter() {
        hideStatusBar();
        hideAnimationBar();

        Variables.getInstance().isGameStoped = false;

        //In the case of running 2 shooters in one time
        if (spaceShooter != null) {
            if (spaceShooter.isGameOver()) {
                spaceShooter = new SpaceShooter();
            }
        } else {
            spaceShooter = new SpaceShooter();
        }

        Variables.getInstance().scoreTextView.setText("0 points");
        Variables.getInstance().isGameRunning = true;
        colorSlider.setVisibility(View.GONE);
        Variables.getInstance().scoreTextView.setVisibility(View.VISIBLE);

    }

    private void startPONG(){
        hideStatusBar();
        hideAnimationBar();

        Variables.getInstance().isGameStoped = false;


        //In the case of running 2 shooters in one time
        if (pong != null) {
            if (pong.isGameOver()) {
                pong = new Pong();
                resetStage();
            }
        } else {
            pong = new Pong();
        }

        //Variables.getInstance().scoreTextView.setText("0 points");
        Variables.getInstance().isGameRunning = true;
        colorSlider.setVisibility(View.GONE);
        Variables.getInstance().scoreTextView.setVisibility(View.VISIBLE);

    }

    private void showAndHideAnimationBar() {
        if (Variables.getInstance().isAnimationBarShowed) {
            //Hide
            hideAnimationBar();
        } else {
            //Show
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Name of the animation");
            //alert.setMessage("Name of the stage");
            final EditText input = new EditText(MainActivity.this);
            input.setText("Animation" + AnimationCreator.getInstance().getAnimations().size() + " ");
            alert.setView(input);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    creatingNewAnimation(input.getText().toString());
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_LONG).show();
                }
            });
            alert.show();
        }
    }

    private void creatingNewAnimation(String name) {
        timeLine.setProgress(0);
        timeLine.setMax(0);
        AnimationCreator.getInstance().createNewAnimation(name);
        AnimationCreator.getInstance().addStageToAnimation(Variables.getInstance().awesomeViewsArr, 0);
        //Toast.makeText(MainActivity.this, "Saved! Stage named " + name, Toast.LENGTH_LONG).show();
        showAnimationBar();
    }

    private void showAnimationBar() {
        animationOptionsBar.setVisibility(View.VISIBLE);
        Variables.getInstance().isAnimationBarShowed = true;
    }

    private void hideAnimationBar() {
        AnimationCreator.getInstance().saveAnimationsOnDevice();
        animationOptionsBar.setVisibility(View.GONE);
        Variables.getInstance().isAnimationBarShowed = false;
    }

    private void remove() {
        if (Variables.getInstance().isAnimationBarShowed) {
            removeAnimation();
        } else {
            removeStage();
        }
    }

    private void removeAnimation() {
        List<String> listItems = new ArrayList<String>();
        for (Animation value : AnimationCreator.getInstance().getAnimations())
            listItems.add(value.getName());
        final CharSequence[] animations2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a animation to remove");
        builder.setSingleChoiceItems(animations2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (AnimationCreator.getInstance().getCurretAnimation() == AnimationCreator.getInstance().getAnimations().get(item)) {
                    //You cant remove currect animation
                    Toast.makeText(MainActivity.this, "You can not remove working animation", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Animation " + AnimationCreator.getInstance().getAnimations().get(item).getName() + " removed!", Toast.LENGTH_LONG).show();
                    AnimationCreator.getInstance().removeAnimation(item);
                }

                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void removeStage() {
        List<String> listItems = new ArrayList<String>();
        for (Stage value : StageSaver.getInstance().getStages())
            listItems.add(value.getName());
        final CharSequence[] stages2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a stage to remove");
        builder.setSingleChoiceItems(stages2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(MainActivity.this, "Stage " +
                        StageSaver.getInstance().getStages().get(item).getName() + " removed!", Toast.LENGTH_LONG).show();
                StageSaver.getInstance().removeStage(item);
                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void openAnimation() {

        //Dialog
        List<String> listItems = new ArrayList<String>();
        for (Animation value : AnimationCreator.getInstance().getAnimations()) {
            Log.i("Stage reader opener", " " + value.getName());
            listItems.add(value.getName());
        }
        final CharSequence[] animations2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a animation to open");
        builder.setSingleChoiceItems(animations2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                showAnimationBar();
                //Setting animation in animation creator
                AnimationCreator.getInstance().setCurretAnimation(
                        AnimationCreator.getInstance().getAnimations().get(item));
                //setup timeline bar
                timeLine.setMax(AnimationCreator.getInstance().getAnimations().get(item).getStages().size() - 1);
                timeLine.setProgress(0);
                //show animation in screen
                for (int i = 0; i <= Variables.numberOfWindows; i++) {
                    Variables.getInstance().changeColorOfView(i ,AnimationCreator.getInstance().getAnimations().get(item).getStages().get(timeLine.getProgress()).getRgbValue()[i]);
                }
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Animation " +
                        AnimationCreator.getInstance().getAnimations().get(item).getName() + " opened!", Toast.LENGTH_LONG).show();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void openStage() {
        List<String> listItems = new ArrayList<String>();
        for (Stage value : StageSaver.getInstance().getStages()) {
            Log.i("Stage reader opener", " " + value.getName());
            listItems.add(value.getName());
        }

        final CharSequence[] stages2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a stage to open");
        builder.setSingleChoiceItems(stages2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                for (int i = 0; i <= Variables.numberOfWindows; i++) {
                    Variables.getInstance().changeColorOfView(i, StageSaver.getInstance().getStages().get(item).getRgbValue()[i]);
                }
                if (Variables.getInstance().isAnimationBarShowed) {
                    AnimationCreator.getInstance().replaceStageInAnimation(
                            Variables.getInstance().awesomeViewsArr, timeLine.getProgress());
                }
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Stage " +
                        StageSaver.getInstance().getStages().get(item).getName() + " opened!", Toast.LENGTH_LONG).show();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void saveStage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Name of the stage");
        //alert.setMessage("Name of the stage");
        final EditText input = new EditText(MainActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable name = input.getText();
                StageSaver.getInstance().addStage(Variables.getInstance().awesomeViewsArr, name);
                Toast.makeText(MainActivity.this, "Saved! Stage named " + name, Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_LONG).show();
            }
        });
        alert.show();
    }

    private void resetStage() {
        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            Variables.getInstance().changeColorOfView(i, Color.BLACK);
        }
    }

    private void setupColorSlider() {
        colorSlider = findViewById(R.id.color_slider);
        colorSlider.setColors(new int[]{
                Color.parseColor("#000000"), Color.parseColor("#FC0FC0"),
                Color.parseColor("#F44336"), Color.parseColor("#FF0000"),
                Color.parseColor("#9C27B0"), Color.parseColor("#673AB7"),
                Color.parseColor("#3F51B5"), Color.parseColor("#0000FF"),
                Color.parseColor("#03A9F4"), Color.parseColor("#00BCD4"),
                Color.parseColor("#00FF00"), Color.parseColor("#4CAF50"),
                Color.parseColor("#8BC34A"), Color.parseColor("#CDDC39"),
                Color.parseColor("#FFEB3B"), Color.parseColor("#FFC107"),
                Color.parseColor("#FF9800"), Color.parseColor("#FF5722"),
                Color.parseColor("#795548"), Color.parseColor("#9E9E9E"),
                Color.parseColor("#607D8B"), Color.parseColor("#FFFFFF")});

        colorSlider.setListener(mListener);
        updateView(colorSlider.getSelectedColor());
    }

    private void setupAnimationBar() {
        animationOptionsBar = findViewById(R.id.animation_bar);

        //SeekBarSetup
        timeLine = findViewById(R.id.time_line_bar);
        timeLine.setMax(0);

        //Stages opener
        timeLine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (int i = 0; i <= Variables.numberOfWindows; i++) {
                    Variables.getInstance().changeColorOfView(i,
                            AnimationCreator.getInstance().curretAnimation.getStages().get(timeLine.getProgress()).getRgbValue()[i]);
                }
                Variables.getInstance().valueOfTimeLineBar = timeLine.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //AddButtonSetup
        Button addStageToAnimationButton = findViewById(R.id.an_add_stage);
        addStageToAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationCreator.getInstance().addStageToAnimation(Variables.getInstance().awesomeViewsArr, timeLine.getProgress());
                timeLine.setMax(timeLine.getMax() + 1);
                timeLine.setProgress(timeLine.getMax());
            }
        });

        //MinusButton
        Button minusStageToAnimationButton = findViewById(R.id.an_remove_stage);
        minusStageToAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((timeLine.getMax() - 1) < 0) {
                    resetStage();
                } else {
                    AnimationCreator.getInstance().removeStageFromAnimation(timeLine.getProgress());
                    timeLine.setMax(timeLine.getMax() - 1);
                    timeLine.setProgress(timeLine.getMax());
                }
            }
        });

        hideAnimationBar();
    }

    private void initViews() {
        for (int i = 0; i <= Variables.numberOfWindows; i++) {
            Variables.getInstance().awesomeViewsArr[i] = findViewById(viewIdArr[i]);
            //Variables.getInstance().awesomeViewsArr[i].setOnClickListener(new MyOnClickListiner(Variables.getInstance().awesomeViewsArr[i]));
        }
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void updateView(@ColorInt int color) {
        Variables.getInstance().sliderColor = color;
    }

    @Override
    protected void onDestroy() {
        StageSaver.getInstance().saveStagesOnDevice();
        AnimationCreator.getInstance().saveAnimationsOnDevice();

        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE
                || event.getAction() == MotionEvent.ACTION_DOWN) {
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
            float width = metrics.widthPixels;
            float height = metrics.heightPixels;

            Variables.getInstance().isTouched = true;

            float x = event.getRawX();
            float y = event.getRawY();

            x /= width;
            y /= height;

            Log.d("Touch,", "touch3  " + x + "   " + y);
            Log.d("Touch,", "touch3  " + width + "   " + height);

            if (Variables.getInstance().isGameRunning) {
                x -= 0.5;
                y -= 0.5;
                if(snake != null){
                    controlSnake(x, y);
                }
                if(spaceShooter != null){
                    controlSpaceShooter(x, y);
                }
            } else if (Variables.getInstance().isAnimationBarShowed) {
                //Changing color of view on slides
                x *= 28;
                y *= 6;

                Variables.getInstance().touchedX =  (int) x - 1;
                Variables.getInstance().touchedY =  (int) y;
                if(!Variables.getInstance().isRainbow)
                    changeColorsOfViews((int) x - 1, (int) y);
            } else {
                //Changing color of view on slides
                x *= 28;
                y *= 5;

                Variables.getInstance().touchedX =  (int) x - 1;
                Variables.getInstance().touchedY =  (int) y;
                if(!Variables.getInstance().isRainbow)
                    changeColorsOfViews((int) x - 1, (int) y);
            }

        } else {
            Variables.getInstance().isTouched = false;
        }
        return super.dispatchTouchEvent(event);
    }

    private void controlSpaceShooter(float x, float y) {
        if(x < 0){
            spaceShooter.setSpaceShipMove(SpaceController.Moves.SHOOT);
        } else if(x > 0 && y < 0){
            spaceShooter.setSpaceShipMove(SpaceController.Moves.UP);
        } else if(x > 0 && y > 0){
            spaceShooter.setSpaceShipMove(SpaceController.Moves.DOWN);
        }

    }

    private void changeColorsOfViews(int x, int y) {
        int mapX = 28;
        int noWindowsSpace = 12;

        int viewId = y * (mapX) + x;
        Log.d("Touch,", "touch3 id  " + viewId);
        if (y == 0 && x < noWindowsSpace) {
        } else {
            viewId -= noWindowsSpace;
            if (viewId <= 127) {
                Variables.getInstance().changeColorOfView(viewId);
            }
        }
    }


    public static Context getAppContext() {
        return MainActivity.context;
    }

    private void controlSnake(float x, float y) {
        if (Math.abs(x) - Math.abs(y) > 0) {
            if (x > 0) {
                snake.setSnakeDirection(SnakeController.Direction.RIGHT);
            } else {
                snake.setSnakeDirection(SnakeController.Direction.LEFT);
            }
        } else {
            if (y > 0) {
                snake.setSnakeDirection(SnakeController.Direction.UP);
            } else {
                snake.setSnakeDirection(SnakeController.Direction.DOWN);
            }
        }
    }

}

