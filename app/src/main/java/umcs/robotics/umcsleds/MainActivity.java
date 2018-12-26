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
import android.view.MenuItem;
import android.view.View;
import android.support.annotation.ColorInt;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.github.naz013.colorslider.ColorSlider;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public ColorSlider colorSlider;

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
        colorSlider = findViewById(R.id.color_slider);
//        colorSlider.setHexColors( new String[]{"0x808080", "0x0000ff", });
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

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Add listiners to views
        for(int i = 0; i <= Variables.numberOfWindows; i++){
            Variables.getInstance().awesomeViewsArr[i] = findViewById(viewIdArr[i]);
            Variables.getInstance().awesomeViewsArr[i].setOnClickListener(new MyOnClickListiner(Variables.getInstance().awesomeViewsArr[i]));
        }

        //Navigation Drawer
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_item);
        mNavigationView.setNavigationItemSelectedListener(this);

        //Hide Status Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void updateView(@ColorInt int color) {
        Variables.getInstance().sliderColor = color;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.d_reset) {
            resetStage();
            Toast.makeText(this, "Reset is done!", Toast.LENGTH_LONG).show();
        }else if(id == R.id.d_save) {
            saveStage();
        }else if(id == R.id.d_open){
            openStage();
        }else if(id == R.id.d_remove){
            remove();
        }else if(id == R.id.d_settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }else if(id == R.id.d_animations){

        }
        return false;
    }

    private void remove() {
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
                    Variables.getInstance().awesomeViewsArr[i].setBackgroundColor(StageSaver.getInstance().getStages().get(item).rgbValue[i]);
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

    @Override
    protected void onDestroy() {
        StageSaver.getInstance().saveStagesOnDevice();
        super.onDestroy();
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

}