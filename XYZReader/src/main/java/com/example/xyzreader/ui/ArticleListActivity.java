package com.example.xyzreader.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;
import com.squareup.picasso.Picasso;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getSupportLoaderManager().initLoader(0,null,this);

        if (savedInstanceState == null) {

            refresh();

        }

    }

    private void refresh() {

        // Let the Swiper know we are swiping
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        startService(new Intent(this, UpdaterService.class));

    }

    @Override
    protected void onStart() {

        super.onStart();

        registerReceiver(mRefreshingReceiver, new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));

    }

    @Override
    protected void onStop() {

        super.onStop();

        unregisterReceiver(mRefreshingReceiver);

    }

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {

                boolean mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);

                mSwipeRefreshLayout.setRefreshing(mIsRefreshing);

            }

        }

    };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return ArticleLoader.newAllArticlesInstance(this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        Adapter adapter = new Adapter(cursor);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mRecyclerView.setAdapter(null);

    }

    public void onWhatsHot(View view) {

        Snackbar.make(view, "Example of declaring this item as HOT!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    public void onShare(View view) {

        // Extract the text saved to a tag via the cursor lookup within the fragment
        String tag = (String)view.getTag();

        // If there was no tag string found default to the name of the app
        if ((tag == null) || (tag.isEmpty())) {
            tag = getResources().getString(R.string.app_name);
        }

        // Give the user a chance to share the article title
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(tag)
                .getIntent(), getString(R.string.action_share)));

    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Cursor mCursor;

        public Adapter(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {

            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.list_item_article, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    // Get the position in the displayed list of items
                    int position = vh.getAdapterPosition();

                    // Start the activity by giving it the position in the DB of the item in question
                    startActivity(new Intent(Intent.ACTION_VIEW, ItemsContract.Items.buildItemUri(getItemId(position))));

                }

            });

            return vh;

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            mCursor.moveToPosition(position);
            holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            holder.subtitleView.setText( "by " + mCursor.getString(ArticleLoader.Query.AUTHOR));

            // Get the image URL
            String url = mCursor.getString(ArticleLoader.Query.THUMB_URL);
            if (url != null) {

                Picasso.with(getApplicationContext()).load(url).into(holder.thumbnailView);

            }

            // Set the tag for the share text
            holder.shareButton.setTag(mCursor.getString(ArticleLoader.Query.TITLE));

        }

        @Override
        public int getItemCount() {

            return mCursor.getCount();

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;
        public ImageButton shareButton;

        public ViewHolder(View view) {

            super(view);

            thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
            shareButton = (ImageButton) view.findViewById(R.id.share_button);

        }

    }

}

