package project.iti.UI.Intro.Fragments;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.goka.walker.WalkerFragment;
import com.goka.walker.WalkerLayout;

import java.util.Arrays;

import project.iti.R;

//import com.example.asmaa.walkthrough.listeners.AnimatorEndListener;

public class FirstPageFragment extends WalkerFragment  implements ViewPager.OnPageChangeListener{

    public static final String TAG = FirstPageFragment.class.getSimpleName();

    // application context
    Context mAppContext;

    RelativeLayout mRootLayout;
   // private DynamicViewPager2 viewPager ;

    // Animation
    Animation animFadein , down;

    public static final int PAGE_POSITION = 0;
    private WalkerLayout walkerLayout;

    public static FirstPageFragment newInstance() {
        Bundle args = new Bundle();
        FirstPageFragment fragment = new FirstPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.first, container, false);

        RelativeLayout t = (RelativeLayout) v.findViewById(R.id.txt) ;
        down = AnimationUtils.loadAnimation(this.getActivity(), R.anim.down);

        walkerLayout = (WalkerLayout) v.findViewById(R.id.walker);
        walkerLayout.setSpeedVariance(new PointF(0.0f, 2.0f));
        walkerLayout.setAnimationType(WalkerLayout.AnimationType.Zoom);
        walkerLayout.setIgnoredViewTags(Arrays.asList("1", "2"));

        walkerLayout.setup();
       return v;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        walkerLayout = (WalkerLayout) view.findViewById(R.id.walker);
         animFadein = AnimationUtils.loadAnimation(this.getActivity(), R.anim.ro2);

        ImageView i = (ImageView) view.findViewById(R.id.route);
       // i.setAnimation(animFadein);

        down = AnimationUtils.loadAnimation(this.getActivity(), R.anim.down);
        RelativeLayout t = (RelativeLayout) view.findViewById(R.id.txt) ;
     //   t.setAnimation(down);

        // animFadein = AnimationUtils.loadAnimation(mAppContext,  R.anim.slide_down);
        walkerLayout.setSpeedVariance(new PointF(0.0f, 2.0f));
        walkerLayout.setAnimationType(WalkerLayout.AnimationType.InOut);
        walkerLayout.setIgnoredViewTags(Arrays.asList("1", "2"));

        walkerLayout.setup();
    }

    @Override
    protected int getPagePosition() {
        return PAGE_POSITION;
    }

    @Override
    protected WalkerLayout getWalkerLayout() {
        return walkerLayout;
    }

}
