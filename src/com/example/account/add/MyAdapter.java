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
 * �Զ���adapter
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
		// ��ʼ��
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
			// ������õ�view
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.add_item_list, parent, false);
			// ��ʼ��holder
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
			// ��������view�Ĵ�СΪ��Ļ���,������ť�����ñ�������Ļ��
			holder.content = convertView.findViewById(R.id.ll_content);
			holder.action = convertView.findViewById(R.id.ll_action);
			LayoutParams lp = holder.content.getLayoutParams();
			lp.width = mScreentWidth;
			convertView.setTag(holder);
			listener.setHolder(holder);
		} else// ��ֱ�ӻ��ViewHolder
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
		// ���ü����¼�
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
//					// ���ViewHolder
//					ViewHolder viewHolder = (ViewHolder) v.getTag();
//
//					// ���HorizontalScrollView������ˮƽ����ֵ.
//					int scrollX = viewHolder.hSView.getScrollX();
//
//					// ��ò�������ĳ���
//					int actionW = viewHolder.action.getWidth();
//
//					// ע��ʹ��smoothScrollTo,����Ч���������Ƚ�Բ��,����Ӳ
//					// ���ˮƽ������ƶ�ֵ<��������ĳ��ȵ�һ��,�͸�ԭ
//					if (scrollX < actionW / 2) {
//						viewHolder.hSView.smoothScrollTo(0, 0);
//					} else// ����Ļ���ʾ��������
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
			holder.tvRemarks.setText("û�б�עŶ��");
		}else{
			holder.tvRemarks.setText("��ע��"+list.get(position).get("j6"));
		}
		holder.tvContent3.setText(list.get(position).get("j1") + "Ԫ");
		if (list.get(position).get("j3").equals("����")) {
			holder.tvContent3.setTextColor(Color.parseColor("#FF3030"));
		}
		if (list.get(position).get("j3").equals("֧��")) {
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
			// ���ViewHolder
		

			// ���HorizontalScrollView������ˮƽ����ֵ.
			int scrollX = holder.hSView.getScrollX();

			// ��ò�������ĳ���
			int actionW = holder.action.getWidth();

			// ע��ʹ��smoothScrollTo,����Ч���������Ƚ�Բ��,����Ӳ
			// ���ˮƽ������ƶ�ֵ<��������ĳ��ȵ�һ��,�͸�ԭ
			if (scrollX < actionW / 2) {
				holder.hSView.smoothScrollTo(0, 0);
			} else// ����Ļ���ʾ��������
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