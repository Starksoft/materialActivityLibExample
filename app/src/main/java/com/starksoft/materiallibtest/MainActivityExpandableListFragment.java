package com.starksoft.materiallibtest;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.starksoft.commons.fragments.BaseRecyclerViewListFragment;

public class MainActivityExpandableListFragment extends BaseRecyclerViewListFragment implements SwipeRefreshLayout.OnRefreshListener {
	Handler mHandler;
	boolean isEmpty = false;

	public MainActivityExpandableListFragment() {
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Инициализация кнопки нужна здесь
		setFabEnabled(true);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		LinearLayoutManager m = new LinearLayoutManager(getActivity());
		getRecyclerListView().setLayoutManager(m);
		getSwipeRefreshLayout().setOnRefreshListener(this);

		Bundle b = getArguments();
		isEmpty = b != null && b.getBoolean("empty");

		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setEmptyText("Нет данных для отображения");

				if (isEmpty) setListAdapter(null);
				else loadAdapter(500);
			}
		}, 1000);

		getFloatingActionButton().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Snackbar.make(v, "TEST!", Snackbar.LENGTH_LONG).setAction("ACTION!", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getActivity(), "Fired action!", Toast.LENGTH_LONG).show();
					}
				}).show();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	private void loadAdapter(int count) {
		String items = "";
		for (int i = 0; i < count; i++)
			items += "Item  " + (i + 1) + ";";

		setListAdapter(new MyAdapter(items.split(";")));
		setHintText("Items: " + getRecyclerViewListAdapter().getItemCount());
	}

	@Override
	public void onRefresh() {
		// Это защищает от ложных срабатываний, при отстутствии адаптера
		if (!getSwipeRefreshLayout().isEnabled()) {
			return;
		}

		setListShown(false);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadAdapter(getRecyclerViewListAdapter().getItemCount() + 1);
				getSwipeRefreshLayout().setRefreshing(false);
				setListShown(true);
			}
		}, 2000);
	}

	class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
		private String[] mDataset;

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public class ViewHolder extends RecyclerView.ViewHolder {
			// each data item is just a string in this case
			public TextView mTextView;

			public ViewHolder(TextView v) {
				super(v);
				mTextView = v;
			}
		}

		// Provide a suitable constructor (depends on the kind of dataset)
		public MyAdapter(String[] myDataset) {
			mDataset = myDataset;
		}

		// Create new views (invoked by the layout manager)
		@Override
		public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			// create a new view


			TextView t = new TextView(getActivity());
			t.setTextSize(20);
			//			t.setPadding(30, 0, 0, 0);
			// set the view's size, margins, paddings and layout parameters

			ViewHolder vh = new ViewHolder(t);
			return vh;
		}

		// Replace the contents of a view (invoked by the layout manager)
		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			// - get element from your dataset at this position
			// - replace the contents of the view with that element
			holder.mTextView.setText(mDataset[position]);

		}

		// Return the size of your dataset (invoked by the layout manager)
		@Override
		public int getItemCount() {
			return mDataset.length;
		}
	}
}
