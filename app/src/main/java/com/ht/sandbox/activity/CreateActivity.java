package com.ht.sandbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ht.sandbox.R;
import com.ht.sandbox.activity.fragment.CreateContentFragment;
import com.ht.sandbox.activity.fragment.CreateTitleFragment;

import static com.ht.sandbox.activity.CreateNoteActivity.EXTRA_REPLY_CONTENT;
import static com.ht.sandbox.activity.CreateNoteActivity.EXTRA_REPLY_TITLE;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    private static final int NUM_PAGES = 2;
    private ViewPager mViewPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup ViewPager.
        mViewPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        // Setup FAB.
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(1, true);
                } else {
                    // Get the title of fragment.
                    CreateTitleFragment titleFragment = (CreateTitleFragment) mPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem() - 1);
                    String title = titleFragment.titleText.getText().toString();

                    // Get content of fragment.
                    CreateContentFragment contentFragment = (CreateContentFragment) mPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
                    String content = contentFragment.editText.getText().toString();

                    // Go back to MainActivity.
                    Intent intent = new Intent();
                    if (TextUtils.isEmpty(title)) {
                        setResult(RESULT_CANCELED, intent);
                    } else {
                        intent.putExtra(EXTRA_REPLY_TITLE, title);
                        intent.putExtra(EXTRA_REPLY_CONTENT, content);
                        setResult(RESULT_OK, intent);
                    }

                    finish();
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        mFab.setImageDrawable(ContextCompat.getDrawable(CreateActivity.this, R.drawable.ic_keyboard_arrow_right_white_24dp));
                        break;
                    case 1:
                        mFab.setImageDrawable(ContextCompat.getDrawable(CreateActivity.this, R.drawable.ic_add_white_24dp));
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                item.setEnabled(true);
                Toast.makeText(getApplicationContext(), R.string.action_favourite, Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A simple pager adapter that represents 2 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CreateTitleFragment.newInstance();
                case 1:
                    return CreateContentFragment.newInstance();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
