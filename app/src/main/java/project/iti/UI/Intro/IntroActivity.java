package project.iti.UI.Intro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.goka.walker.WalkerFragment;

import project.iti.R;
import project.iti.UI.Login.LoginActivity;
import project.iti.UI.Intro.Fragments.FirstPageFragment;
import project.iti.UI.Intro.Fragments.FourthPageFragment;
import project.iti.UI.Intro.Fragments.SecondPageFragment;
import project.iti.UI.Intro.Fragments.ThirdPageFragment;

public class IntroActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener, View.OnClickListener /* ,
        Animation.AnimationListener*/ {

    private int currentPosition;
    private ImageView first, second, third, fourth;
    private Button  skip;
    private RelativeLayout walkerLayout;
    private RelativeLayout walkerLayout1;

    // application context
    Context mAppContext;
    // Animation
    Animation animFadein ,an2 , ro , ro1;

    private static final int MAX_PAGES = 10;
    private DynamicViewPager viewPager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

//        in = (Button) findViewById(R.id.left);
//        up = (Button) findViewById(R.id.right);

        skip= findViewById(R.id.skip);
        skip.setOnClickListener(this);

        ro = AnimationUtils.loadAnimation(this, R.anim.rotate2);
        ro1 = AnimationUtils.loadAnimation(this, R.anim.ro);
        walkerLayout = (RelativeLayout) findViewById(R.id.point);
        an2 =AnimationUtils.loadAnimation(this, R.anim.slide_down);

        walkerLayout1 = (RelativeLayout) findViewById(R.id.frame);

        viewPager = (DynamicViewPager) findViewById(R.id.pager);
        viewPager.setMaxPages(MAX_PAGES);
        viewPager.setBackgroundAsset(R.drawable.bb);

    //  walkerLayout.setAnimation(animFadein);

       animFadein = AnimationUtils.loadAnimation(this, R.anim.down);
        ro = AnimationUtils.loadAnimation(this, R.anim.ro);
       animFadein.setAnimationListener(animationInListener);
        ro.setAnimationListener(animationOutListener);
      walkerLayout1.startAnimation(animFadein);

    }



    Animation.AnimationListener animationInListener
            = new Animation.AnimationListener(){

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            //image.startAnimation(animationFadeOut);

         //   viewPager.startAnimation(ro);
            final WalkerFragment firstPageFragment = FirstPageFragment.newInstance();
            final WalkerFragment secondPageFragment = SecondPageFragment.newInstance();
            final WalkerFragment thirdPageFragment = ThirdPageFragment.newInstance();
            final WalkerFragment fourthPageFragment = FourthPageFragment.newInstance();


            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {


                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case FirstPageFragment.PAGE_POSITION:
                            return firstPageFragment;
                        case SecondPageFragment.PAGE_POSITION:
                            return secondPageFragment;
                        case ThirdPageFragment.PAGE_POSITION:
                            return thirdPageFragment;
                        case FourthPageFragment.PAGE_POSITION:
                            return fourthPageFragment;

                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 4;
                }
            });




            viewPager.addOnPageChangeListener(firstPageFragment);
            viewPager.addOnPageChangeListener(secondPageFragment);
            viewPager.addOnPageChangeListener(thirdPageFragment);
            viewPager.addOnPageChangeListener(fourthPageFragment);
            viewPager.addOnPageChangeListener(IntroActivity.this);


            //currentPosition = FirstPageFragment.PAGE_POSITION;

            first = (ImageView) findViewById(R.id.first);
            second = (ImageView) findViewById(R.id.second);
            third = (ImageView) findViewById(R.id.third);
            fourth = (ImageView) findViewById(R.id.fourth);

            first.setBackgroundResource(R.drawable.newselected);



        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

          walkerLayout1.startAnimation(animFadein);
//            in.startAnimation(animFadein);
//            up.startAnimation(animFadein);
            walkerLayout.startAnimation(animFadein);

        }};

    Animation.AnimationListener animationOutListener
            = new Animation.AnimationListener(){

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

            viewPager.startAnimation(ro);


        }};
    ////////////////////////////////////////////
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    ////////////////////////////////////////////////////////////
    @Override
    public void onPageSelected(int position) {


        currentPosition = position;
        switch (currentPosition) {
            case FirstPageFragment.PAGE_POSITION:
                first.setBackgroundResource(R.drawable.newselected);
                second.setBackgroundResource(R.drawable.unselected);
                third.setBackgroundResource(R.drawable.unselected);
                fourth.setBackgroundResource(R.drawable.unselected);

                break;
            case SecondPageFragment.PAGE_POSITION:
                second.setBackgroundResource(R.drawable.newselected);
                first.setBackgroundResource(R.drawable.selected);
                third.setBackgroundResource(R.drawable.unselected);
                fourth.setBackgroundResource(R.drawable.unselected);

                break;
            case ThirdPageFragment.PAGE_POSITION:
                third.setBackgroundResource(R.drawable.newselected);
                second.setBackgroundResource(R.drawable.selected);
                first.setBackgroundResource(R.drawable.selected);
                fourth.setBackgroundResource(R.drawable.unselected);

                break;
            case FourthPageFragment.PAGE_POSITION:
                fourth.setBackgroundResource(R.drawable.newselected);
                second.setBackgroundResource(R.drawable.selected);
                first.setBackgroundResource(R.drawable.selected);
                third.setBackgroundResource(R.drawable.selected);

                break;
            default:
                first.setBackgroundResource(R.drawable.selected);
                second.setBackgroundResource(R.drawable.unselected);
                third.setBackgroundResource(R.drawable.unselected);
                fourth.setBackgroundResource(R.drawable.unselected);
                break;
        }
    }
    //////////////////////////////////////////
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.skip:
                Intent switchToLogin = new Intent(this, LoginActivity.class);
                startActivity(switchToLogin);
                break;
        }
    }
}
