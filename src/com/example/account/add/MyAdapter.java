package com.example.account.add;

import java.util.HashMap;
import java.util.List;

import com.melhc.xiji.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;

import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 自定义adapter
 * 
 * @Title:
 * @Description:
 * @Author:justlcw
 * @Since:2013-11-22
 * @Version:
 */
class MyAdapter extends BaseAdapter {
	private FlingListeber listener;
	private GestureDetector detector;
	private Context mContext;
	private int mScreentWidth;
	private List<HashMap<String, String>> list;

	@SuppressWarnings("deprecation")
	public MyAdapter(Context context, int screenWidth) {
		// 初始化
		mContext = context;
		mScreentWidth = screenWidth;
		listener = new FlingListeber();
		detector = new GestureDetector(listener);
	}

	public List<HashMap<String, String>> initData(
			List<HashMap<String, String>> list) {

		this.list = list;
		return this.list;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 final ViewHolder holder;
		if (convertView == null) {
			// 获得设置的view
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.add_item_list, parent, false);
			// 初始化holder
			holder = new ViewHolder();
			holder.hSView = (HorizontalScrollView) convertView
					.findViewById(R.id.hsv);
			holder.add_l1=(RelativeLayout)convertView.findViewById(R.id.add_l1);
			holder.tvContent1 = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.tvContent2 = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.tvContent3 = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.tvRemarks = (TextView) convertView
					.findViewById(R.id.remarks_id);
			// 设置内容view的大小为屏幕宽度,这样按钮就正好被挤出屏幕外
			holder.content = convertView.findViewById(R.id.ll_content);
			holder.action = convertView.findViewById(R.id.ll_action);
			LayoutParams lp = holder.content.getLayoutParams();
			lp.width = mScreentWidth;
			convertView.setTag(holder);
			listener.setHolder(holder);
		} else// 有直接获得ViewHolder
		{
			holder = (ViewHolder) convertView.getTag();
			listener.setHolder(holder);
		}
		holder.add_l1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent rechargeIntent = new Intent(mContext, AddActivity.class);
			rechargeIntent.putExtra("date", list.get(position).get("j7"));			
			rechargeIntent.putExtra("category", list.get(position).get("j3"));
			rechargeIntent.putExtra("sum", list.get(position).get("j1"));
			rechargeIntent.putExtra("project", list.get(position).get("j4"));
			rechargeIntent.putExtra("time", list.get(position).get("j2"));
			rechargeIntent.putExtra("remarks", list.get(position).get("j6"));
			rechargeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			mContext.startActivity(rechargeIntent);
			((Activity) mContext).overridePendingTransition(R.anim.zoomin,
					R.anim.zoomout);
		}
	});
		holder.add_l1.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		// 设置监听事件
		convertView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//
//					Log.i("TAG", "--------------->23");
//
//					return true;
//
//				case MotionEvent.ACTION_UP:
//
//					// 获得ViewHolder
//					ViewHolder viewHolder = (ViewHolder) v.getTag();
//
//					// 获得HorizontalScrollView滑动的水平方向值.
//					int scrollX = viewHolder.hSView.getScrollX();
//
//					// 获得操作区域的长度
//					int actionW = viewHolder.action.getWidth();
//
//					// 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
//					// 如果水平方向的移动值<操作区域的长度的一半,就复原
//					if (scrollX < actionW / 2) {
//						viewHolder.hSView.smoothScrollTo(0, 0);
//					} else// 否则的话显示操作区域
//					{
//						viewHolder.hSView.smoothScrollTo(actionW, 0);
//					}
//					return true;
//				}
//				return false;
		    return detector.onTouchEvent(event);
			}
		});

		if (holder.hSView.getScrollX() != 0) {
			holder.hSView.scrollTo(0, 0);
		}
		holder.tvContent1.setText(list.get(position).get("j2"));
		holder.tvContent2.setText(list.get(position).get("j4"));
		if(TextUtils.isEmpty(list.get(position).get("j6"))){
			holder.tvRemarks.setText("没有备注哦！");
		}else{
			holder.tvRemarks.setText("备注："+list.get(position).get("j6"));
		}
		holder.tvContent3.setText(list.get(position).get("j1") + "元");
		if (list.get(position).get("j3").equals("收入")) {
			holder.tvContent3.setTextColor(Color.parseColor("#FF3030"));
		}
		if (list.get(position).get("j3").equals("支出")) {
			holder.tvContent3.setTextColor(Color.parseColor("#43CD80"));
		}
		if (list.get(position).get("j3").equals("")) {
			holder.tvContent3.setTextColor(Color.parseColor("#000000"));
		}
		return convertView;
	}

	class ViewHolder {
		public HorizontalScrollView hSView;
        public RelativeLayout  add_l1;
		public View content;
		public TextView tvContent1, tvContent2, tvContent3, tvRemarks;

		public View action;
	}

	class FlingListeber implements OnGestureListener {
      
		ViewHolder holder;
		
  

		public ViewHolder getHolder() {
            return holder;
        }

        public void setHolder(ViewHolder holder) {
            this.holder = holder;
        }

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// 获得ViewHolder
		

			// 获得HorizontalScrollView滑动的水平方向值.
			int scrollX = holder.hSView.getScrollX();

			// 获得操作区域的长度
			int actionW = holder.action.getWidth();

			// 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
			// 如果水平方向的移动值<操作区域的长度的一半,就复原
			if (scrollX < actionW / 2) {
				holder.hSView.smoothScrollTo(0, 0);
			} else// 否则的话显示操作区域
			{
				holder.hSView.smoothScrollTo(actionW, 0);
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			

			return false;
		}

	}


}