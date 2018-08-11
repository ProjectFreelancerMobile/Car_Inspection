package com.car_inspection.library.commonview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.car_inspection.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import google.com.carinspection.DisposableImpl;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SearchSuggestView extends FrameLayout {

    private MaxHeightRecyclerView rvSuggestionsListView;
    private CTextView mSearchSrcTextView;
    private OnSearchSuggestListener onSearchSuggestListener;
    private SearchSuggestAdapter suggestAdapter;
    private ImageView btnShowListSuggest;

    private List<String> suggests = new ArrayList<>();
    private boolean submit = false;
    private Context mContext;
    private String currentText = "";

    public void setOnSearchSuggestListener(OnSearchSuggestListener onSearchSuggestListener) {
        this.onSearchSuggestListener = onSearchSuggestListener;
    }

    public SearchSuggestView(@NonNull Context context) {
        this(context, null);
    }

    public SearchSuggestView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchSuggestView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initiateView();
        initStyle(attrs, defStyleAttr);
    }

    @SuppressLint("WrongConstant")
    private void initiateView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_search_suggest_view, this, true);
        mSearchSrcTextView = rootView.findViewById(R.id.searchTextView);
        rvSuggestionsListView = rootView.findViewById(R.id.suggestion_list);
        rvSuggestionsListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        btnShowListSuggest = rootView.findViewById(R.id.btnShowListSuggest);
        btnShowListSuggest.setOnClickListener(v -> {
            if (rvSuggestionsListView.getVisibility() == View.VISIBLE)
                rvSuggestionsListView.setVisibility(GONE);
            else rvSuggestionsListView.setVisibility(VISIBLE);
        });
        rootView.findViewById(R.id.searchTextView).setOnClickListener(v -> {
            if (rvSuggestionsListView.getVisibility() == View.VISIBLE)
                rvSuggestionsListView.setVisibility(GONE);
            else rvSuggestionsListView.setVisibility(VISIBLE);
        });
        suggestAdapter = new SearchSuggestAdapter();
        rvSuggestionsListView.setAdapter(suggestAdapter);

//        RxTextView.textChangeEvents(mSearchSrcTextView)
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<TextViewTextChangeEvent>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
//                        if (onSearchSuggestListener != null)
//                            onSearchSuggestListener.onSearchTextChange(textViewTextChangeEvent.text().toString(), submit);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }

    private void setText(String text) {
        currentText = text;
        mSearchSrcTextView.setText(text);
        mSearchSrcTextView.setTextColor(getContext().getResources().getColor(R.color.black));
    }

    public void setHint(String hint) {
        currentText = "";
        mSearchSrcTextView.setText(hint);
        mSearchSrcTextView.setTextColor(getContext().getResources().getColor(R.color.black_text_color_hint));
    }

    public void closeSuggest() {
        rvSuggestionsListView.setVisibility(GONE);
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, defStyleAttr, 0);

        if (a != null) {
            if (a.hasValue(R.styleable.MaterialSearchView_searchBackground)) {
                setBackground(a.getDrawable(R.styleable.MaterialSearchView_searchBackground));
            }

            a.recycle();
        }
    }

    public void setSelectedItem(String itemName) {
        for (String name : suggests)
            if (name.toLowerCase().equals(itemName.toLowerCase())) {
                setText(name);
                return;
            }
    }

    public void setSelectedItem(int position) {
        if (position >= suggests.size())
            return;
        setText(suggests.get(position));
    }

    public void setSuggest(List<String> suggests) {
        try {
            rvSuggestionsListView.setVisibility(GONE);
            this.suggests = suggests;
            if (suggests.size() <= 1) {
                btnShowListSuggest.setVisibility(GONE);
                // setText(suggests.get(0));
            } else {
                // setText(suggests.get(0));
                suggestAdapter.notifyDataSetChanged();
                btnShowListSuggest.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getText() {
        return currentText;
    }

    public class SearchSuggestAdapter extends RecyclerView.Adapter<SearchSuggestViewHolder> {
        @Override
        public SearchSuggestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_suggest_item, parent, false);

            return new SearchSuggestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchSuggestViewHolder holder, int position) {
            try {
//                if (position == 0)
//                    holder.line.setVisibility(GONE);
                holder.tvTitle.setText(suggests.get(position));
                holder.tvTitle.setOnClickListener(v -> {
                    submit = true;
                    setText(suggests.get(position));
                    if (onSearchSuggestListener != null)
                        onSearchSuggestListener.onItemSuggestClicked(position, suggests.get(position));
                    rvSuggestionsListView.setVisibility(GONE);
                    Observable.just(1L).delay(1, TimeUnit.SECONDS).subscribeOn(io.reactivex.schedulers.Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableImpl<Long>() {
                                @Override
                                public void onNext(Long aLong) {
                                    submit = false;
                                }
                            });
                });
                holder.rootView.setOnClickListener(v -> {
                    submit = true;
                    setText(suggests.get(position));
                    if (onSearchSuggestListener != null && position<getItemCount())
                        onSearchSuggestListener.onItemSuggestClicked(position, suggests.get(position));
                    rvSuggestionsListView.setVisibility(GONE);
                    Observable.just(1L).delay(1, TimeUnit.SECONDS).subscribeOn(io.reactivex.schedulers.Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableImpl<Long>() {
                                @Override
                                public void onNext(Long aLong) {
                                    submit = false;
                                }
                            });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return suggests != null ? suggests.size() : 0;
        }
    }

    public class SearchSuggestViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        CTextView tvTitle;
        @BindView(R.id.rootView)
        RelativeLayout rootView;
        @BindView(R.id.line)
        View line;

        public SearchSuggestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSearchSuggestListener {
        void onItemSuggestClicked(int position, String text);
    }

}