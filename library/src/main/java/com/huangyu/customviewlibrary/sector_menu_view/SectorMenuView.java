package com.huangyu.customviewlibrary.sector_menu_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.huangyu.customviewlibrary.R;

/**
 * Created by huangyu on 2017/4/5.
 */
public class SectorMenuView extends FrameLayout {

	private static final int diameter = 240;
	private static final int totalAngle = 360;
	private static final int sectorAngle = 40;
	private static final int intervalTime = 10;

	private boolean isOpen = false;
	private boolean canTouch = true;
	private int selectPosition = -1;

	private SectorView[] sectorViewArray;
	private CenterView centerView;
	private SectorButtonClickListener sectorButtonClickListener;

	public interface SectorButtonClickListener {
		void onSectorButtonClick(int position);
	}

	public SectorMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnSectorButtonClick(SectorButtonClickListener sectorButtonClickListener) {
		this.sectorButtonClickListener = sectorButtonClickListener;
	}

	public void init(final Context context, String[] textArray) {
		centerView = new CenterView(context);
		addView(centerView);

		sectorViewArray = new SectorView[textArray.length];
		SectorView sectorView;
		for (int i = 0; i < sectorViewArray.length; i++) {
			sectorView = new SectorView(context, textArray[i], i, totalAngle, sectorAngle);
			sectorView.setVisibility(View.INVISIBLE);
			sectorViewArray[i] = sectorView;
			addView(sectorView);
		}

		bringChildToFront(centerView);
	}

	public void openOrCloseMenu(boolean isShowDialog) {
		Animation animation = getCenterAnim(isShowDialog);
		if (animation != null) {
			centerView.startAnimation(animation);
		}
	}

	private Animation getCenterAnim(boolean isShowDialog) {
		Animation animation;
		if (isOpen) {
			if (isShowDialog) {
				animation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
			} else {
				animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
			}
		} else {
			animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		}
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				if (!isOpen) {
					centerView.setBitmapResourse(R.drawable.btn_tool_hover);
					centerView.invalidate();
				}
				startSectorAnim();
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isOpen) {
					centerView.setBitmapResourse(R.drawable.btn_tool_nor);
					centerView.invalidate();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		animation.setDuration(intervalTime * sectorViewArray.length);
		return animation;
	}

	private void startSectorAnim() {
		int length = sectorViewArray.length;
		if (length > 0) {
			if (isOpen) {
				sectorViewArray[length - 1].startAnimation(getSectorAnim(length - 1));
			} else {
				sectorViewArray[0].startAnimation(getSectorAnim(0));
			}
		}
	}

	private Animation getSectorAnim(final int position) {
		Animation animation;
		if (isOpen) {
			animation = new AlphaAnimation(1, 0);
		} else {
			animation = new AlphaAnimation(0, 1);
		}
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int newPosition;
				if (isOpen) {
					sectorViewArray[position].setVisibility(View.GONE);

					newPosition = position - 1;
					if (newPosition >= 0) {
						sectorViewArray[newPosition].startAnimation(getSectorAnim(newPosition));
					} else {
						isOpen = !isOpen;
						canTouch = true;
					}
				} else {
					sectorViewArray[position].setVisibility(View.VISIBLE);

					newPosition = position + 1;
					if (newPosition < sectorViewArray.length) {
						sectorViewArray[newPosition].startAnimation(getSectorAnim(newPosition));
					} else {
						isOpen = !isOpen;
						canTouch = true;
					}
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		animation.setDuration(intervalTime);
		return animation;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!canTouch) {
					return true;
				}
				canTouch = false;
				selectPosition = touchJudge(event.getX(), event.getY());
				switch (selectPosition) {
					// 选中中心
					case -1:
						openOrCloseMenu(false);
						selectPosition();
						break;
					// 没有选中
					case -2:
						canTouch = true;
						break;
					// 选中扇形
					default:
//						canTouch = true;
						selectPosition();
						sectorButtonClickListener.onSectorButtonClick(selectPosition);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * 选中的扇形改变颜色
	 */
	private void selectPosition() {
		for (int i = 0; i < sectorViewArray.length; i++) {
			if (i == selectPosition) {
				sectorViewArray[i].setIsSelect(true);
			} else {
				sectorViewArray[i].setIsSelect(false);
			}
		}
	}

	/**
	 * 判断选中状态
	 * 
	 * @param touchX 点击的x坐标
	 * @param touchY 点击的y坐标
	 * @return
	 */
	private int touchJudge(double touchX, double touchY) {
		// 实际按钮并没有填充全部布局，乘以一个系数避免点击冲突
		double radius = (centerView.bitmapWidth + centerView.bitmapHeight) / 2 / 2 * 5 / 6;

		double x = touchX - diameter / 2;
		double y = touchY - diameter / 2;
		double distance = Math.sqrt(x * x + y * y);

		// 判断是否点击了中心
		if (distance <= radius) {
			return -1; // 选中中心
		} else {
			if (!isOpen) {
				return -2; // 没有选中
			}
			x = touchX - diameter / 2;
			y = touchY - diameter / 2;
			distance = Math.sqrt(x * x + y * y);
			// 判断是否超过了扇形半径
			if (distance > diameter / 2) {
				return -2; // 没有选中
			}
			// 判断属于哪个扇形
			else {
				double angle = totalAngle - Math.round(Math.atan2(y, x) / Math.PI * totalAngle / 2);
				angle = angle > totalAngle ? angle - totalAngle : angle;
				for (int i = 0; i < sectorViewArray.length; i++) {
					if (isSelect(angle, i)) {
						return i; // 选中第i个扇形
					}
				}
			}
			return -2; // 没有选中
		}
	}

	private boolean isSelect(double angle, int position) {
		return angle > (position * sectorAngle) && angle < ((position + 1) * sectorAngle);
	}

	private void layoutCenterView(CenterView centerView) {
		int childWidth = centerView.bitmapWidth;
		int childHeight = centerView.bitmapHeight;
		centerView.layout((diameter - childWidth) / 2, (diameter - childHeight) / 2, (diameter - childWidth) / 2 + childWidth, (diameter - childHeight) / 2
				+ childHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureDimension(diameter, widthMeasureSpec);
		int height = measureDimension(diameter, heightMeasureSpec);
		measureChildren(width, height);
		setMeasuredDimension(width, height);
	}

	private int measureDimension(int defaultSize, int measureSpec) {
		int result = defaultSize;
		int specMode = MeasureSpec.getMode(measureSpec); 
		switch (specMode) {
			case MeasureSpec.EXACTLY:
			case MeasureSpec.AT_MOST:
			case MeasureSpec.UNSPECIFIED:
				result = defaultSize;
		}
		return result;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);

			if (childView instanceof CenterView) {
				layoutCenterView((CenterView) childView);
			} else {
				childView.layout(0, 0, diameter, diameter);
			}
		}
	}

}
