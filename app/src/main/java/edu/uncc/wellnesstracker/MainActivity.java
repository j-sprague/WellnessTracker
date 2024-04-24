package edu.uncc.wellnesstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.location.LocationRequestCompat;

import android.health.connect.datatypes.ExerciseRoute;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeListener, AddFragment.AddListener,
        SleepFragment.SleepListener, SelectExerciseFragment.ExerciseListener, SelectQualityFragment.QualityListener,
        VisualizeFragment.VisualizeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new HomeFragment())
                .commit();
    }

    @Override
    public void gotoNew() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new AddFragment(), "add-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoVisualize() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new VisualizeFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoSleep() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new SleepFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoQuality() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new SelectQualityFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoExercise() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new SelectExerciseFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSleepSelected(Double hours) {
        AddFragment fragment = (AddFragment) getSupportFragmentManager().findFragmentByTag("add-fragment");
        if (fragment != null) {
            fragment.selectedSleep = hours;
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onExerciseSelected(Double hours) {
        AddFragment fragment = (AddFragment) getSupportFragmentManager().findFragmentByTag("add-fragment");
        if (fragment != null) {
            fragment.selectedExercise = hours;
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onQualitySelected(int quality) {
        AddFragment fragment = (AddFragment) getSupportFragmentManager().findFragmentByTag("add-fragment");
        if (fragment != null) {
            fragment.selectedQuality = quality;
        }
        getSupportFragmentManager().popBackStack();
    }

}