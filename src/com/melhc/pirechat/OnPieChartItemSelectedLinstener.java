package com.melhc.pirechat;

/**
 * ��״ͼ�Ŀ鱻�������Ϣ����
 * 
 */

public interface OnPieChartItemSelectedLinstener {

	/**
	 * �ص�
	 * 
	 * @param view
	 *            ��ǰ��PieChartView
	 * @param position
	 *            ��ǰ�鿴�Ŀ��id����id������˳�����㴫�������Ĵ�С��ֵ��˳�����
	 * @param colorRgb
	 *            ��ǰ�鿴�Ŀ����ɫ
	 * @param size
	 *            ��ǰ�鿴�Ŀ��ֵ
	 * @param rate
	 *            ��ǰ�鿴�Ŀ���ռ�ı����� 0 < rate < 1
	 * @param isFreePart
	 *            �Ƿ��Ƕ���Ŀ飻��Ϊtrueʱ˵��������ĸ�����Ĵ�С�ĺ�С�ڴ���������ֵ��������ⲿ�ּ�Ϊ�����ֵ��ȥ������ĺ͵Ĳ��֡�
	 * @param rotateTime
	 *            ��������ת������������ת���������ʱ�䣬��λ�����롣0Ϊ����ת��δ������ת������ <br/>
	 * <br/>
	 *            ʹ�÷���ͬButton��OnClickListener};
	 * 
	 */
	void onPieChartItemSelected(PieChartView view, int position, String colorRgb, float size, float rate, boolean isFreePart, float rotateTime);

	void onTriggerClicked();
}
