package umcs.robotics.umcsleds;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.annotation.ColorInt;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.naz013.colorslider.ColorSlider;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public ColorSlider colorSlider;

    private View tv1;
    private LinearLayout animationOptionsBar;
    private SeekBar timeLine;


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
        StageSaver.getInstance();

        //Color slider
        setupColorSlider();

        // Hide the status bar.
        hideStatusBar();

        //Add listiners to views
        addListinerToViews();

        //Navigation Drawer
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_item);
        mNavigationView.setNavigationItemSelectedListener(this);

        //Hide Status Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animation options bar
        setupAnimationBar();

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.d_reset) {
            resetStage();
            Toast.makeText(this, "Reset is done!", Toast.LENGTH_LONG).show();
        }else if(id == R.id.d_save) {
            saveStage();
        }else if(id == R.id.d_open_stage){
            openStage();
        }else if(id == R.id.d_open_animation){
            openAnimation();
        }else if(id == R.id.d_remove){
            remove();
        }else if(id == R.id.d_settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }else if(id == R.id.d_animations){
            showAndHideAnimationBar();
        } else if(id == R.id.d_single_stage){
            hideAnimationBar();
        }
        return false;
    }

    private void showAndHideAnimationBar() {
        if(Variables.getInstance().isAnimationBarShowed){
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

    private void creatingNewAnimation(String name){
        timeLine.setProgress(0);
        timeLine.setMax(0);
        AnimationCreator.getInstance().createNewAnimation(name);
        AnimationCreator.getInstance().addStageToAnimation(Variables.getInstance().awesomeViewsArr, 0);
        //Toast.makeText(MainActivity.this, "Saved! Stage named " + name, Toast.LENGTH_LONG).show();
        showAnimationBar();
    }

    private void showAnimationBar(){
        animationOptionsBar.setVisibility(View.VISIBLE);
        Variables.getInstance().isAnimationBarShowed = true;
    }

    private void hideAnimationBar(){
        AnimationCreator.getInstance().saveAnimationsOnDevice();
        animationOptionsBar.setVisibility(View.GONE);
        Variables.getInstance().isAnimationBarShowed = false;
    }

    private void remove() {
        if(Variables.getInstance().isAnimationBarShowed) {
            removeAnimation();
        } else {
            removeStage();
        }
    }

    private void removeAnimation() {
        List<String> listItems = new ArrayList<String>();
        for(Animation value : AnimationCreator.getInstance().getAnimations())
            listItems.add(value.getName());
        final CharSequence[] animations2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a animation to remove");
        builder.setSingleChoiceItems(animations2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(AnimationCreator.getInstance().getCurretAnimation() == AnimationCreator.getInstance().getAnimations().get(item)){
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
        for(Stage value : StageSaver.getInstance().getStages())
            listItems.add(value.name);
        final CharSequence[] stages2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a stage to remove");
        builder.setSingleChoiceItems(stages2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(MainActivity.this, "Stage " + StageSaver.getInstance().getStages().get(item).name + " removed!", Toast.LENGTH_LONG).show();
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
        for(Animation value : AnimationCreator.getInstance().getAnimations()){
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
                    Variables.getInstance().awesomeViewsArr[i].setBackgroundColor(
                            AnimationCreator.getInstance().getAnimations().get(item).getStages().get(timeLine.getProgress()).rgbValue[i]);
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
        for(Stage value : StageSaver.getInstance().getStages()){
            Log.i("Stage reader opener", " " + value.name);
            listItems.add(value.name);
        }

        final CharSequence[] stages2 = listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog levelDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select a stage to open");
        builder.setSingleChoiceItems(stages2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                for (int i = 0; i <= Variables.numberOfWindows; i++) {
                    Variables.getInstance().awesomeViewsArr[i].setBackgroundColor(
                            StageSaver.getInstance().getStages().get(item).rgbValue[i]);
                }
                if(Variables.getInstance().isAnimationBarShowed){
                    AnimationCreator.getInstance().replaceStageInAnimation(
                            Variables.getInstance().awesomeViewsArr, timeLine.getProgress());
                }
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Stage " +
                        StageSaver.getInstance().getStages().get(item).name + " opened!", Toast.LENGTH_LONG).show();
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
        for(int i = 0; i <= Variables.numberOfWindows; i++){
            Variables.getInstance().awesomeViewsArr[i].setBackgroundColor(Color.BLACK);
        }
    }

    private void setupColorSlider(){
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
                for(int i = 0; i <= Variables.numberOfWindows; i++) {
                    Variables.getInstance().awesomeViewsArr[i].setBackgroundColor(
                            AnimationCreator.getInstance().curretAnimation.getStages().get(timeLine.getProgress()).rgbValue[i]);
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
                if((timeLine.getMax() - 1) < 0) {
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

    private void addListinerToViews() {
        for(int i = 0; i <= Variables.numberOfWindows; i++){
            Variables.getInstance().awesomeViewsArr[i] = findViewById(viewIdArr[i]);
            Variables.getInstance().awesomeViewsArr[i].setOnClickListener(new MyOnClickListiner(Variables.getInstance().awesomeViewsArr[i]));
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

    public static Context getAppContext() {
        return MainActivity.context;
    }
}