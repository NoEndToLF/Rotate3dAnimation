package com.aice.rotate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.aice.rotate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止屏幕截屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setCameraDistance();
        setAnimators();
        mBinding.positiveLl.setOnClickListener(v -> {
            if (mRightOutSet.isRunning() || mLeftInSet.isRunning()) {
                return;
            }
            Toast.makeText(MainActivity.this, "正面", Toast.LENGTH_SHORT).show();
        });
        mBinding.positiveTv.setOnClickListener(v -> {
            if (mRightOutSet.isRunning() || mLeftInSet.isRunning()) {
                return;
            }
            Toast.makeText(MainActivity.this, "正面111", Toast.LENGTH_SHORT).show();
        });
        mBinding.negativeLl.setOnClickListener(v -> {
            if (mRightOutSet.isRunning() || mLeftInSet.isRunning()) {
                return;
            }
            Toast.makeText(MainActivity.this, "反面", Toast.LENGTH_SHORT).show();
        });
        mBinding.negativeTv.setOnClickListener(v -> {
            if (mRightOutSet.isRunning() || mLeftInSet.isRunning()) {
                return;
            }
            Toast.makeText(MainActivity.this, "反面111", Toast.LENGTH_SHORT).show();
        });
        mBinding.openBtn.setOnClickListener(v -> {
//                flip(mBinding.positiveLl, mBinding.negativeLl, 700);
            if (mRightOutSet.isRunning() || mLeftInSet.isRunning()) {
                return;
            }
            isOpen = true;
            mBinding.positiveLl.setVisibility(View.VISIBLE);
            mBinding.negativeLl.setVisibility(View.VISIBLE);
            mRightOutSet.setTarget(mBinding.positiveLl);
            mLeftInSet.setTarget(mBinding.negativeLl);
            mRightOutSet.start();
            mLeftInSet.start();
        });
        mBinding.closeBtn.setOnClickListener(v -> {
//                flip(mBinding.negativeLl, mBinding.positiveLl, 700);
            if (mRightOutSet.isRunning() || mLeftInSet.isRunning()) {
                return;
            }
            isOpen = false;
            mBinding.positiveLl.setVisibility(View.VISIBLE);
            mBinding.negativeLl.setVisibility(View.VISIBLE);
            mRightOutSet.setTarget(mBinding.negativeLl);
            mLeftInSet.setTarget(mBinding.positiveLl);
            mRightOutSet.start();
            mLeftInSet.start();
        });
    }

    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_in);
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.positiveLl.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                mBinding.negativeLl.setVisibility(isOpen ? View.VISIBLE : View.GONE);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mBinding.positiveLl.setCameraDistance(scale);
        mBinding.negativeLl.setCameraDistance(scale);
    }

    private AnimatorSet flip(final View outView, final View inView, long duration) {
        final int degreeOut = 90;
        final int degreeIn = -degreeOut;
        final ObjectAnimator animOut, animIn;
        animOut = ObjectAnimator.ofFloat(outView, "rotationY", 0, degreeOut);
        animIn = ObjectAnimator.ofFloat(inView, "rotationY", degreeIn, 0);
        animOut.setDuration(duration);
        animIn.setDuration(duration);
        animOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                outView.setVisibility(View.GONE);
                inView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        outView.setVisibility(View.VISIBLE);
        inView.setVisibility(View.GONE);
        AnimatorSet set = new AnimatorSet();
        set.play(animIn).after(animOut);
        set.start();
        return set;
    }
}