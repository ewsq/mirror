package com.miyatu.mirror.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.bean.InformationDatabean;

import java.util.List;

/**
 * Created by yhd on 2019/11/22.
 * Email 1443160259@qq.com
 */

//消息列表适配器
public class InformationAdapter extends BaseQuickAdapter<InformationDatabean.DataBean, BaseViewHolder> {

    public InformationAdapter(int layoutResId, @Nullable List<InformationDatabean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InformationDatabean.DataBean item) {
        helper.setText(R.id.tv_title, item.getTitle());

        ImageView imageView = helper.getView(R.id.iv_img);
        Glide.with(mContext).load(MyApp.imageUrl + item.getCover()).into(imageView);

        helper.addOnClickListener(R.id.iv_img);
    }
}
//public class InformationAdapter extends BaseAdapter {
//
//    private Context mContext;
//    private List<informationDatabean> list = new ArrayList<>();
//    public InformationAdapter(Context mContext, List<informationDatabean> list) {
//        this.mContext = mContext;
//        this.list = list;
//    }
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return i;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
//        ViewHolder viewHolder;
//        if (view == null) {
//            viewHolder= new ViewHolder();
//            view = LayoutInflater.from(mContext).inflate(
//                    R.layout.item_information, null);
//            viewHolder.ivImg = view.findViewById(R.id.iv_img);
//            viewHolder.titleTv = view.findViewById(R.id.tv_title);
//            view.setTag(viewHolder);
//
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        return view;
//    }
//
//    class ViewHolder{
//       ImageView ivImg;
//       TextView titleTv;
//    }
//}
