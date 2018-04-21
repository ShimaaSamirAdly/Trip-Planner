package project.iti.UI.Intro.Fragments;

import android.graphics.PointF;
import android.os.Bundle;
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

/**
 * Created by asmaa on 7/26/16.
 */
public class FourthPageFragment extends WalkerFragment {

    public static final String TAG = FourthPageFragment.class.getSimpleName();

    public static final int PAGE_POSITION = 3;
    private WalkerLayout walkerLayout;
    Animation animFadein , down;


    public static FourthPageFragment newInstance() {
        Bundle args = new Bundle();
        FourthPageFragment fragment = new FourthPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fourth, container, false);
    }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      down = AnimationUtils.loadAnimation(this.getActivity(), R.anim.down);
      RelativeLayout t = (RelativeLayout) view.findViewById(R.id.txt) ;
    //  t.setAnimation(down);
      animFadein = AnimationUtils.loadAnimation(this.getActivity(), R.anim.ro2);

      ImageView i = (ImageView) view.findViewById(R.id.route);
     // i.setAnimation(animFadein);

      walkerLayout = (WalkerLayout) view.findViewById(R.id.walker);
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

