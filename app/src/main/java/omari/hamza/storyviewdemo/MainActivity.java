package omari.hamza.storyviewdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.model.MyStory;
import omari.hamza.storyviewdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setActivity(this);
    }

    public void showStories() {

        final ArrayList<MyStory> myStories = new ArrayList<>();

        MyStory story1 = new MyStory(
                "https://static-yoosh.s3.eu-north-1.amazonaws.com/story-2.png",
                "This is testing title",
                "description"
        );
        myStories.add(story1);

        MyStory story2 = new MyStory(
                "https://static-yoosh.s3.eu-north-1.amazonaws.com/story-3.png",
                "This is testing title",
                "description"
        );
        myStories.add(story2);

        MyStory story3 = new MyStory(
                "https://static-yoosh.s3.eu-north-1.amazonaws.com/story-4.png",
                "This is testing title",
                "description"
        );
        myStories.add(story3);

        new StoryView.Builder(getSupportFragmentManager())
                .setStoriesList(myStories)
                .setStoryDuration(5000)
                .setTitleText("Introduction to Yoosh")
                .setStartingIndex(0)
                .setBackground(getDrawable(R.drawable.background))
                .build()
                .show();

    }
}
