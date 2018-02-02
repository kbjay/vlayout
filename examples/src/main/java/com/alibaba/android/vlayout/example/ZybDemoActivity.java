package com.alibaba.android.vlayout.example;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import java.util.LinkedList;

public class ZybDemoActivity extends Activity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zyb_demo);

        mRv = (RecyclerView) this.findViewById(R.id.rv_demo);

        custom();
    }

    /**
     * 客制化vLayout
     *
     * @author kb_jay
     * created at 2018/2/2 下午2:55
     */
    private void custom() {

        //step1：初始化

        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);

        mRv.setLayoutManager(layoutManager);

        //step2：设置回收池

        RecyclerView.RecycledViewPool rvPool = new RecyclerView.RecycledViewPool();

        mRv.setRecycledViewPool(rvPool);

        rvPool.setMaxRecycledViews(5, 10);
        rvPool.setMaxRecycledViews(21, 30);

        //step3：设置代理adaper

        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);

        mRv.setAdapter(delegateAdapter);

        //step4：设置真实的adapters

        LinkedList<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setBgColor(Color.GRAY);
        linearLayoutHelper.setDividerHeight(10);


        adapters.add(new SubAdapter(this, linearLayoutHelper, 5));

        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4, 21);
        gridLayoutHelper.setBgColor(Color.GREEN);
        gridLayoutHelper.setWeights(new float[]{25f,25f,25f,25f});
        gridLayoutHelper.setGap(10);
        adapters.add(new SubAdapter(this, gridLayoutHelper, 21));

        delegateAdapter.setAdapters(adapters);


    }

    static class SubAdapter extends DelegateAdapter.Adapter<ZybViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;

        private VirtualLayoutManager.LayoutParams mLayoutParams;

        private int mCount = 0;

        public SubAdapter(Context context, LayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }

        public SubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public ZybViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ZybViewHolder(LayoutInflater.from(mContext).inflate(R.layout.zyb_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ZybViewHolder holder, int position) {
            holder.itemView.setLayoutParams(new VirtualLayoutManager.LayoutParams(mLayoutParams));
            holder.mTv.setText("*" + mCount + "*");
        }

        @Override
        protected void onBindViewHolderWithOffset(ZybViewHolder holder, int position, int offsetTotal) {
            super.onBindViewHolderWithOffset(holder, position, offsetTotal);
        }

        @Override
        public int getItemCount() {
            return mCount;
        }

        @Override
        public int getItemViewType(int position) {
            return mCount;
        }
    }

    static class ZybViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv;

        public ZybViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
