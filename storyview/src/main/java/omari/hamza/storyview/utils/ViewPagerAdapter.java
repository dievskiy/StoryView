package omari.hamza.storyview.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import omari.hamza.storyview.R;
import omari.hamza.storyview.callback.StoryCallbacks;
import omari.hamza.storyview.model.ImageStory;
import omari.hamza.storyview.model.SimpleStory;
import omari.hamza.storyview.model.Story;

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<Story> stories;
    private Context context;
    private StoryCallbacks storyCallbacks;
    private StoryClickListener storyClickListener;
    private boolean storiesStarted = false;

    public ViewPagerAdapter(ArrayList<Story> stories, Context context, StoryCallbacks storyCallbacks, StoryClickListener storyClickListener) {
        this.stories = stories;
        this.context = context;
        this.storyCallbacks = storyCallbacks;
        this.storyClickListener = storyClickListener;
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private void renderImageStory(View view, ImageStory currentStory) {
        final ImageView mImageView = view.findViewById(R.id.image);
        Glide.with(context)
                .load(currentStory.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        storyCallbacks.nextStory();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (!storiesStarted) {
                            storiesStarted = true;
                            storyCallbacks.startStories();
                        }
                        return false;
                    }
                })
                .into(mImageView);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Story currentStory = stories.get(position);
        View view = null;

        if (currentStory instanceof ImageStory) {
            view = inflater.inflate(R.layout.item_image_story, collection, false);
            renderImageStory(view, (ImageStory) currentStory);
        } else if (currentStory instanceof SimpleStory) {
            view = inflater.inflate(R.layout.item_simple_story, collection, false);
        }

        final TextView title = view.findViewById(R.id.title);
        final TextView description = view.findViewById(R.id.description);
        title.setText(currentStory.getTitle());
        description.setText(currentStory.getDescription());

        collection.addView(view);

        view.setOnTouchListener((view12, motionEvent) -> {
            storyClickListener.onClick(motionEvent);
            return true;
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }

    public interface StoryClickListener {
        void onClick(MotionEvent event);
    }
}
