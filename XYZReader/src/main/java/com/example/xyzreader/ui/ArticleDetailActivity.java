package com.example.xyzreader.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ItemsContract;


/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
//public class ArticleDetailActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
public class ArticleDetailActivity extends AppCompatActivity {

//    private Cursor mCursor;
//    private long mStartId;
//
//    private long mSelectedItemId;
//    private int mTopInset;
//
//    private ViewPager mPager;
//    private MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {

//            Bundle arguments = new Bundle();
//            arguments.putString(CoderDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(CoderDetailFragment.ARG_ITEM_ID));
//            CoderDetailFragment fragment = new CoderDetailFragment();
//            fragment.setArguments(arguments);

            long startId = 0;

            // Extract the parameter used during the intent creation of this activity
            if ((getIntent() != null) && (getIntent().getData() != null)) {

                startId = ItemsContract.Items.getItemId(getIntent().getData());

            }

            // Create the detail fragment and add it to the activity using a fragment transaction.
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.article_detail_container, ArticleDetailFragment.newInstance(startId))
                    .commit();

        }


//        super.onCreate(savedInstanceState);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
//        setContentView(R.layout.activity_article_detail);
//
//        getLoaderManager().initLoader(0, null, this);
//
//        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
//        mPager = (ViewPager) findViewById(R.id.pager);
//        mPager.setAdapter(mPagerAdapter);
//        mPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
//        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));
//
//        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                if (mCursor != null) {
//                    mCursor.moveToPosition(position);
//                }
//                mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
//            }
//
//        });
//
//        if (savedInstanceState == null) {
//            if (getIntent() != null && getIntent().getData() != null) {
//                mStartId = ItemsContract.Items.getItemId(getIntent().getData());
//                mSelectedItemId = mStartId;
//            }
//        }
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return ArticleLoader.newAllArticlesInstance(this);
//    }

//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        mCursor = cursor;
//        mPagerAdapter.notifyDataSetChanged();
//
//        // Select the start ID
//        if (mStartId > 0) {
//            mCursor.moveToFirst();
//            // TODO: optimize
//            while (!mCursor.isAfterLast()) {
//                if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
//                    final int position = mCursor.getPosition();
//                    mPager.setCurrentItem(position, false);
//                    break;
//                }
//                mCursor.moveToNext();
//            }
//            mStartId = 0;
//        }
//    }

//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//        mCursor = null;
//        mPagerAdapter.notifyDataSetChanged();
//    }

//    private class MyPagerAdapter extends FragmentStatePagerAdapter {
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//            ArticleDetailFragment fragment = (ArticleDetailFragment) object;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            mCursor.moveToPosition(position);
//            return ArticleDetailFragment.newInstance(mCursor.getLong(ArticleLoader.Query._ID));
//        }
//
//        @Override
//        public int getCount() {
//            return (mCursor != null) ? mCursor.getCount() : 0;
//        }
//    }
}
