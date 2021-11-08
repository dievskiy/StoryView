package omari.hamza.storyviewdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.model.ImageStory;
import omari.hamza.storyview.model.SimpleStory;
import omari.hamza.storyview.model.Story;
import omari.hamza.storyviewdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setActivity(this);
    }

    public void showStories() {

        final ArrayList<Story> myStories = new ArrayList<>();

        Story story1 = new SimpleStory(
                "60-sec guide",
                "Let’s quickly cover the basic elements and functions."
        );

        Story story2 = new ImageStory(
                "https://static-yoosh.s3.eu-north-1.amazonaws.com/story-2.png",
                "Channels",
                "Channels works like chats in messengers, but instead of messages they store notes."
        );

        Story story3 = new ImageStory(
                "https://static-yoosh.s3.eu-north-1.amazonaws.com/story-3.png",
                "Spaces",
                "Spaces work like folders. Each one can contain child-spaces and channels. Space structures helps you keep notes organized."
        );

        Story story4 = new ImageStory(
                "https://static-yoosh.s3.eu-north-1.amazonaws.com/story-4.png",
                "Collaborating",
                "You can invite collaborators to take notes on shared projects together."
        );

        Story story5 = new SimpleStory(
                "Done!",
                "We’ve super briefly covered the basics. Find out more useful info on our blog."
        );

        myStories.add(story1);
        myStories.add(story2);
        myStories.add(story3);
        myStories.add(story4);
        myStories.add(story5);

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
