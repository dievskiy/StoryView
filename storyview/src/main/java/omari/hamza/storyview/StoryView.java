package omari.hamza.storyview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import omari.hamza.storyview.callback.OnStoryChangedCallback;
import omari.hamza.storyview.callback.StoryCallbacks;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;
import omari.hamza.storyview.progress.StoriesProgressView;
import omari.hamza.storyview.utils.ViewPagerAdapter;

public class StoryView extends DialogFragment implements StoriesProgressView.StoriesListener,
        StoryCallbacks {

    private static final String TAG = StoryView.class.getSimpleName();

    private ArrayList<MyStory> storiesList = new ArrayList<>();

    private final static String IMAGES_KEY = "IMAGES";

    private long duration = 2000; //Default Duration

    private static final String DURATION_KEY = "DURATION";

    private static final String HEADER_INFO_KEY = "HEADER_INFO";

    private static final String STARTING_INDEX_TAG = "STARTING_INDEX";

    private StoriesProgressView storiesProgressView;

    private ViewPager mViewPager;

    private int counter = 0;

    private int startingIndex = 0;

    private Drawable background;

    private ImageButton closeImageButton;

    private String author;

    //Touch Events
    private boolean isPaused = false;
    private int width, height;

    private StoryClickListeners storyClickListeners;
    private OnStoryChangedCallback onStoryChangedCallback;

    private StoryView() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_stories, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        // Get field from view
        readArguments();
        setupViews(view);
        setupStories();

    }

    private void setupStories() {
        storiesProgressView.setStoriesCount(storiesList.size());
        storiesProgressView.setStoryDuration(duration);
        mViewPager.setAdapter(new ViewPagerAdapter(storiesList, getContext(), this, this::storyClicked));
    }

    private void storyClicked(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getAxisValue(MotionEvent.AXIS_X);
            int third = width / 3;
            if (x <= third) {
                previousStory();
            } else if (x > third && x <= 2 * third) {
                if (!isPaused) {
                    isPaused = true;
                    storiesProgressView.pause();
                }
            } else {
                nextStory();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isPaused) {
                isPaused = false;
                storiesProgressView.resume();
            }
        }
    }

    private void readArguments() {
        assert getArguments() != null;
        storiesList = new ArrayList<>((ArrayList<MyStory>) getArguments().getSerializable(IMAGES_KEY));
        duration = getArguments().getLong(DURATION_KEY, 2000);
        startingIndex = getArguments().getInt(STARTING_INDEX_TAG, 0);
    }

    private void setupViews(View view) {
        storiesProgressView = view.findViewById(R.id.storiesProgressView);
        if (author != null) {
            TextView author = view.findViewById(R.id.author);
            author.setText(this.author);
        }

        if (background != null) {
            view.findViewById(R.id.background).setBackground(background);
        }
        mViewPager = view.findViewById(R.id.storiesViewPager);
        closeImageButton = view.findViewById(R.id.imageButton);
        storiesProgressView.setStoriesListener(this);
        mViewPager.setOnTouchListener((v, event) -> true);
        closeImageButton.setOnClickListener(v -> dismissAllowingStateLoss());

        if (onStoryChangedCallback != null) {
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    onStoryChangedCallback.storyChanged(position);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onNext() {
        mViewPager.setCurrentItem(++counter, false);
    }

    @Override
    public void onPrev() {
        if (counter <= 0) return;
        mViewPager.setCurrentItem(--counter, false);
    }

    @Override
    public void onComplete() {
        dismissAllowingStateLoss();
    }

    @Override
    public void startStories() {
        isPaused = false;
        counter = startingIndex;
        storiesProgressView.startStories(startingIndex);
        mViewPager.setCurrentItem(startingIndex, false);
    }

    @Override
    public void pauseStories() {
        isPaused = true;
        storiesProgressView.pause();
    }

    private void previousStory() {
        if (counter - 1 < 0) return;
        mViewPager.setCurrentItem(--counter, false);
        storiesProgressView.setStoriesCount(storiesList.size());
        storiesProgressView.setStoryDuration(duration);
        storiesProgressView.startStories(counter);
    }

    private void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void nextStory() {
        if (counter + 1 >= storiesList.size()) {
            dismissAllowingStateLoss();
            return;
        }
        mViewPager.setCurrentItem(++counter, false);
        storiesProgressView.startStories(counter);
    }

    @Override
    public void onDescriptionClickListener(int position) {
        if (storyClickListeners == null) return;
        storyClickListeners.onDescriptionClickListener(position);
    }

    @Override
    public void onDestroy() {
        storiesList = null;
        storiesProgressView.destroy();
        super.onDestroy();
    }

    public void setStoryClickListeners(StoryClickListeners storyClickListeners) {
        this.storyClickListeners = storyClickListeners;
    }

    public void setOnStoryChangedCallback(OnStoryChangedCallback onStoryChangedCallback) {
        this.onStoryChangedCallback = onStoryChangedCallback;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public static class Builder {

        private StoryView storyView;
        private FragmentManager fragmentManager;
        private Bundle bundle;
        private String author;
        private Drawable background;
        private StoryClickListeners storyClickListeners;
        private OnStoryChangedCallback onStoryChangedCallback;

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            this.bundle = new Bundle();
        }

        public Builder setBackground(Drawable background) {
            this.background = background;
            return this;
        }

        public Builder setStoriesList(ArrayList<MyStory> storiesList) {
            bundle.putSerializable(IMAGES_KEY, storiesList);
            return this;
        }

        public Builder setTitleText(String title) {
            author = title;
            return this;
        }

        public Builder setStoryDuration(long duration) {
            bundle.putLong(DURATION_KEY, duration);
            return this;
        }

        public Builder setStartingIndex(int index) {
            bundle.putInt(STARTING_INDEX_TAG, index);
            return this;
        }

        public Builder build() {
            if (storyView != null) {
                Log.e(TAG, "The StoryView has already been built!");
                return this;
            }
            storyView = new StoryView();
            storyView.setArguments(bundle);
            if (storyClickListeners != null) {
                storyView.setStoryClickListeners(storyClickListeners);
            }
            if (onStoryChangedCallback != null) {
                storyView.setOnStoryChangedCallback(onStoryChangedCallback);
            }
            if (background != null) storyView.setBackground(background);
            if (author != null) storyView.setAuthor(author);
            return this;
        }

        public Builder setOnStoryChangedCallback(OnStoryChangedCallback onStoryChangedCallback) {
            this.onStoryChangedCallback = onStoryChangedCallback;
            return this;
        }

        public Builder setStoryClickListeners(StoryClickListeners storyClickListeners) {
            this.storyClickListeners = storyClickListeners;
            return this;
        }

        public void show() {
            storyView.show(fragmentManager, TAG);
        }

        public void dismiss() {
            storyView.dismiss();
        }

        public Fragment getFragment() {
            return storyView;
        }

    }

}
