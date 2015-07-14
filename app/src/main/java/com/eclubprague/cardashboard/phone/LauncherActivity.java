package com.eclubprague.cardashboard.phone;

import com.eclubprague.cardashboard.phone.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LauncherActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    static String DEBUG_TAG = "CL";
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    protected View.OnTouchListener gestureListener;
    static int currentFragmentPosition = 0;
    static Fragment[] fragments = new Fragment[4];

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);

        Fragment calls = new MenuItemFragment();
        Bundle callsArgs = new Bundle();
        callsArgs.putString("name", getString(R.string.calls));
        calls.setArguments(callsArgs);

        Fragment navigation = new MenuItemFragment();
        Bundle navigationArgs = new Bundle();
        navigationArgs.putString("name", getString(R.string.navigation));
        navigation.setArguments(navigationArgs);

        Fragment media = new MenuItemFragment();
        Bundle mediaArgs = new Bundle();
        mediaArgs.putString("name", getString(R.string.media));
        media.setArguments(mediaArgs);

        Fragment settings = new MenuItemFragment();
        Bundle settingsArgs = new Bundle();
        settingsArgs.putString("name", getString(R.string.settings));
        settings.setArguments(settingsArgs);

        fragments[0] = calls;
        fragments[1] = navigation;
        fragments[2] = media;
        fragments[3] = settings;

        setContentView(R.layout.activity_launcher);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragments[0])
                    .commit();
        }
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    public static class MenuItemFragment extends Fragment {

        String name;

        public MenuItemFragment() {
        }

        @Override
        public void setArguments(Bundle args) {
            name = args.getString("name");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menuitem, container, false);
            TextView name = (TextView) rootView.findViewById(R.id.name);
            name.setText(this.name);
            rootView.setOnClickListener((View.OnClickListener) this.getActivity());
            rootView.setOnTouchListener(((LauncherActivity) this.getActivity()).gestureListener);
            return rootView;
        }
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int lastPosition = currentFragmentPosition;
                    currentFragmentPosition++;
                    if (currentFragmentPosition == fragments.length) currentFragmentPosition = 0;
                    getFragmentManager().beginTransaction()
                            .remove(fragments[lastPosition])
                            .add(R.id.container, fragments[currentFragmentPosition])
                            .commit();
                    Log.d(DEBUG_TAG, "" + currentFragmentPosition);

                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int lastPosition = currentFragmentPosition;
                    currentFragmentPosition--;
                    if (currentFragmentPosition < 0) currentFragmentPosition += fragments.length;

                    getFragmentManager().beginTransaction()
                            .remove(fragments[lastPosition])
                            .add(R.id.container, fragments[currentFragmentPosition])
                            .commit();


                    Log.d(DEBUG_TAG, "" + currentFragmentPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
