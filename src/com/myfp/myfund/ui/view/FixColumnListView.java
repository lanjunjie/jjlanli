package com.myfp.myfund.ui.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.MySelectFund;
import com.myfp.myfund.ui.DealActivity;
import com.myfp.myfund.ui.DealApplyActivity;
import com.myfp.myfund.ui.DealBuyActivity;
import com.myfp.myfund.ui.MyMeansActivity;


public class FixColumnListView extends BaseFixColumnListView implements
		OnScrollListener {

	private final static String DATE_FORMAT_STR = "yyyy年MM月dd日 HH:mm";

	/** 实际的padding的距离与界面上偏移距离的比例 */
	private final static int RATIO = 3;

	private final static int RELEASE_TO_REFRESH = 0;
	private final static int PULL_TO_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	/** 手动完成刷新 */
	private final static int ENDINT_MANUAL_LOAD_DONE = 2;
	/** 自动完成刷新 */
	private final static int ENDINT_AUTO_LOAD_DONE = 3;

	/**
	 * 0:RELEASE_TO_REFRESH;
	 * <p>
	 * 1:PULL_To_REFRESH;
	 * <p>
	 * 2:REFRESHING;
	 * <p>
	 * 3:DONE;
	 * <p>
	 * 4:LOADING
	 */
	private int mHeadState;
	/**
	 * 0:完成/等待刷新 ;
	 * <p>
	 * 1:加载中
	 */
	private int mEndState;

	// ================================= 功能设置Flag
	// ================================

	/** 可以加载更多？ */
	private boolean mCanLoadMore = false;
	/** 可以下拉刷新？ */
	private boolean mCanRefresh = false;
	/** 可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义） */
	private boolean mIsAutoLoadMore = true;
	/** 下拉刷新后是否显示第一条Item */
	private boolean mIsMoveToFirstItemAfterRefresh = false;

	public boolean isCanLoadMore() {
		return mCanLoadMore;
	}

	public void setCanLoadMore(boolean pCanLoadMore) {
		mCanLoadMore = pCanLoadMore;
		if (mCanLoadMore && getFooterViewsCount() == 0) {
			addFooterView();
		}
	}

	public boolean isCanRefresh() {
		return mCanRefresh;
	}

	public void setCanRefresh(boolean pCanRefresh) {
		mCanRefresh = pCanRefresh;
	}

	public boolean isAutoLoadMore() {
		return mIsAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		mIsAutoLoadMore = pIsAutoLoadMore;
	}

	public boolean isMoveToFirstItemAfterRefresh() {
		return mIsMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(
			boolean pIsMoveToFirstItemAfterRefresh) {
		mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
	}

	// ============================================================================

	private LayoutInflater mInflater;

	private LinearLayout mHeadView;
	private TextView mTipsTextView;
	private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;

	private View mEndRootView;
	private ProgressBar mEndLoadProgressBar;
	private TextView mEndLoadTipsTextView;

	/** headView动画 */
	private RotateAnimation mArrowAnim;
	/** headView反转动画 */
	private RotateAnimation mArrowReverseAnim;

	/** 用于保证startY的值在一个完整的touch事件中只被记录一次 */
	private boolean mIsRecored;

	private int mHeadViewWidth;
	private int mHeadViewHeight;

	private int mStartY;
	private boolean mIsBack;

	private int mFirstItemIndex;
	private int mLastItemIndex;
	private int mCount;
	private boolean mEnoughCount;// 足够数量充满屏幕？

	private OnRefreshListener mRefreshListener;
	private OnLoadMoreListener mLoadMoreListener;

	List<MySelectFund> mList = null;
	private BaseActivity activity;
	private String userName;
	public HorizontalScrollView mTouchView;
	private int index;
	private String encodePassWord, idCard;
	ByteArrayInputStream tInputStringStream = null;

	public FixColumnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FixColumnListView(Context context) {
		super(context);
		init(context);
	}

	public FixColumnListView(Context context, AttributeSet pAttrs, int pDefStyle) {
		super(context, pAttrs, pDefStyle);
		init(context);
	}

	/**
	 * 初始化操作
	 * 
	 * @param pContext
	 * @date 2013-11-20 下午4:10:46
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void init(Context pContext) {
		setCacheColorHint(pContext.getResources().getColor(R.color.transparent));
		mInflater = LayoutInflater.from(pContext);

		addHeadView();

		setOnScrollListener(this);

		initPullImageAnimation(0);
	}

	/**
	 * 添加下拉刷新的HeadView
	 * 
	 * @date 2013-11-11 下午9:48:26
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void addHeadView() {
		mHeadView = (LinearLayout) mInflater.inflate(R.layout.head, null);

		mArrowImageView = (ImageView) mHeadView
				.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(70);
		mArrowImageView.setMinimumHeight(50);
		mProgressBar = (ProgressBar) mHeadView
				.findViewById(R.id.head_progressBar);
		mTipsTextView = (TextView) mHeadView
				.findViewById(R.id.head_tipsTextView);
		mLastUpdatedTextView = (TextView) mHeadView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(mHeadView);
		mHeadViewHeight = mHeadView.getMeasuredHeight();
		mHeadViewWidth = mHeadView.getMeasuredWidth();

		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
		mHeadView.invalidate();

		Log.v("size", "width:" + mHeadViewWidth + " height:" + mHeadViewHeight);

		addHeaderView(mHeadView, null, false);

		mHeadState = DONE;
	}

	/**
	 * 添加加载更多FootView
	 * 
	 * @date 2013-11-11 下午9:52:37
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void addFooterView() {
		mEndRootView = mInflater.inflate(R.layout.listfooter_more, null);
		mEndRootView.setVisibility(View.VISIBLE);
		mEndLoadProgressBar = (ProgressBar) mEndRootView
				.findViewById(R.id.pull_to_refresh_progress);
		mEndLoadTipsTextView = (TextView) mEndRootView
				.findViewById(R.id.load_more);
		mEndRootView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCanLoadMore) {
					if (mCanRefresh) {
						// 当可以下拉刷新时，如果FootView没有正在加载，并且HeadView没有正在刷新，才可以点击加载更多。
						if (mEndState != ENDINT_LOADING
								&& mHeadState != REFRESHING) {
							mEndState = ENDINT_LOADING;
							onLoadMore();
						}
					} else if (mEndState != ENDINT_LOADING) {
						// 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
						mEndState = ENDINT_LOADING;
						onLoadMore();
					}
				}
			}
		});

		addFooterView(mEndRootView);

		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		} else {
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
	}

	/**
	 * 实例化下拉刷新的箭头的动画效果
	 * 
	 * @param pAnimDuration
	 *            动画运行时长
	 * @date 2013-11-20 上午11:53:22
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void initPullImageAnimation(final int pAnimDuration) {

		int _Duration;

		if (pAnimDuration > 0) {
			_Duration = pAnimDuration;
		} else {
			_Duration = 250;
		}
		// Interpolator _Interpolator;
		// switch (pAnimType) {
		// case 0:
		// _Interpolator = new AccelerateDecelerateInterpolator();
		// break;
		// case 1:
		// _Interpolator = new AccelerateInterpolator();
		// break;
		// case 2:
		// _Interpolator = new AnticipateInterpolator();
		// break;
		// case 3:
		// _Interpolator = new AnticipateOvershootInterpolator();
		// break;
		// case 4:
		// _Interpolator = new BounceInterpolator();
		// break;
		// case 5:
		// _Interpolator = new CycleInterpolator(1f);
		// break;
		// case 6:
		// _Interpolator = new DecelerateInterpolator();
		// break;
		// case 7:
		// _Interpolator = new OvershootInterpolator();
		// break;
		// default:
		// _Interpolator = new LinearInterpolator();
		// break;
		// }

		Interpolator _Interpolator = new LinearInterpolator();

		mArrowAnim = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mArrowAnim.setInterpolator(_Interpolator);
		mArrowAnim.setDuration(_Duration);
		mArrowAnim.setFillAfter(true);

		mArrowReverseAnim = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mArrowReverseAnim.setInterpolator(_Interpolator);
		mArrowReverseAnim.setDuration(_Duration);
		mArrowReverseAnim.setFillAfter(true);
	}

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
	 * 
	 * @param pChild
	 * @date 2013-11-20 下午4:12:07
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected View getCustomerView(int position, View convertView,
			ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_query_fund_list, null);
			// ��һ�γ�ʼ����ʱ��װ����
			ListItemHorizontalScrollView hScrollView = (ListItemHorizontalScrollView) convertView
					.findViewById(R.id.item_scroll);
			hScrollView.setListView(FixColumnListView.this);
			addHViews(hScrollView);
			holder = new ViewHolder();
			holder.tv_fundName = (TextView) convertView
					.findViewById(R.id.tv_select_fundName);
			holder.tv_fundCode = (TextView) convertView
					.findViewById(R.id.tv_select_fundCode);
			holder.tv_unitEquity = (TextView) convertView
					.findViewById(R.id.tv_select_unitEquity);
			holder.tv_dealDate = (TextView) convertView
					.findViewById(R.id.tv_select_dealDate);
			holder.tv_dayBenefit = (TextView) convertView
					.findViewById(R.id.tv_select_dayBenefit);
			holder.tv_totalEquity = (TextView) convertView
					.findViewById(R.id.tv_select_totalEquity);

			holder.tv_weekRedound = (TextView) convertView
					.findViewById(R.id.tv_select_weekRedound);
			holder.tv_monthRedound = (TextView) convertView
					.findViewById(R.id.tv_select_monthRedound);
			holder.tv_seasonRedound = (TextView) convertView
					.findViewById(R.id.tv_select_seasonRedound);
			holder.tv_halfYearRedound = (TextView) convertView
					.findViewById(R.id.tv_select_halfYearRedound);
			holder.tv_yearRedound = (TextView) convertView
					.findViewById(R.id.tv_select_YearRedound);
			holder.tv_toyearRedound = (TextView) convertView
					.findViewById(R.id.tv_select_toyearRedound);
			holder.img_selected = (ImageView) convertView
					.findViewById(R.id.img_select_isSelected);
			holder.bt_buy = (Button) convertView
					.findViewById(R.id.bt_select_operate);
			holder.img_selected.setVisibility(convertView.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MySelectFund fund = mList.get(position);
		holder.tv_fundName.setText(fund.getFundName() + "");
		holder.tv_fundCode.setText(fund.getFundCode());
		holder.tv_unitEquity.setText(fund.getUnitEquity() + "");
		holder.tv_unitEquity.setTextColor(Color.RED);
		holder.tv_dealDate.setText(fund.getDealDate());
		holder.tv_dayBenefit.setText(fund.getDayBenefit() + "%");
		holder.tv_totalEquity.setText(fund.getTotalEquity().toString());

		if (index == 7) {
			holder.tv_dayBenefit.setVisibility(View.GONE);
			holder.tv_weekRedound.setVisibility(View.GONE);
			holder.tv_monthRedound.setVisibility(View.GONE);
			holder.tv_seasonRedound.setVisibility(View.GONE);
			holder.tv_halfYearRedound.setVisibility(View.GONE);
			holder.tv_yearRedound.setVisibility(View.GONE);
			holder.tv_toyearRedound.setVisibility(View.GONE);

			holder.tv_totalEquity.setText(fund.getTotalEquity() + "%");
			holder.tv_totalEquity.setTextColor(Color.RED);

		} else {

			if (fund.getDayBenefit() < 0) {
				holder.tv_dayBenefit.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.tv_dayBenefit.setTextColor(Color.RED);
			}

			holder.tv_weekRedound.setText(fund.getOneWeekRedound() + "%");
			if (fund.getOneWeekRedound() < 0) {
				holder.tv_weekRedound.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.tv_weekRedound.setTextColor(Color.RED);
			}

			holder.tv_monthRedound.setText(fund.getOneMonthRedound() + "%");
			if (fund.getOneMonthRedound() < 0) {
				holder.tv_monthRedound.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.tv_monthRedound.setTextColor(Color.RED);
			}

			holder.tv_seasonRedound.setText(fund.getThreeMonthRedound() + "%");
			if (fund.getThreeMonthRedound() < 0) {
				holder.tv_seasonRedound.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.tv_seasonRedound.setTextColor(Color.RED);
			}

			holder.tv_halfYearRedound.setText(fund.getSixMonthRedound() + "%");
			if (fund.getSixMonthRedound() < 0) {
				holder.tv_halfYearRedound.setTextColor(Color.rgb(4, 151, 1));
			} else {
				holder.tv_halfYearRedound.setTextColor(Color.RED);
			}

			holder.tv_yearRedound.setText(fund.getOneyearRedound() + "%");
			if (fund.getOneyearRedound() < 0) {
				holder.tv_yearRedound.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.tv_yearRedound.setTextColor(Color.RED);
			}

			holder.tv_toyearRedound.setText(fund.getThisYearRedound() + "%");
			if (fund.getThisYearRedound() < 0) {
				holder.tv_toyearRedound.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.tv_toyearRedound.setTextColor(Color.RED);
			}
		}

		View.OnClickListener clickListener = new View.OnClickListener() {
			private String sessionid;

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.img_select_isSelected:
					if (TextUtils.isEmpty(userName)) {
						Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT)
								.show();
						activity.startActivity(new Intent(mContext,
								MyMeansActivity.class));
					} else {
						RequestParams params = new RequestParams(mContext);
						params.put("UserName", userName);
						params.put("FundCode", fund.getFundCode().trim());
						params.put("FundName", fund.getFundName().trim());
						activity.execApi(ApiType.GET_MY_FUND_CENTER, params,
								new OnDataReceivedListener() {

									@Override
									public void onReceiveData(ApiType api,
											String json) {
										if (json == null) {
											activity.showToast("请求失败!");
											return;
										}
										try {
											JSONArray array = new JSONArray(
													json);
											if (api == ApiType.GET_MY_FUND_CENTER) {
												int returnResult = array
														.getJSONObject(0)
														.getInt("ReturnResult");
												switch (returnResult) {
												case 0:
													activity.showToast("添加成功!");
													holder.img_selected
															.setImageResource(R.drawable.red_star_solid_big);

													break;
												case 1:
													activity.showToast("用户名不能为空!");
													break;
												case 2:
													activity.showToast("取消自选!");
													holder.img_selected
															.setImageResource(R.drawable.red_star_hollow_big);
													break;
												case 3:
													activity.showToast("添加成功!");
													holder.img_selected
															.setImageResource(R.drawable.red_star_solid_big);
													break;
												case 4:
													activity.showToast("添加失败!");
													break;
												case 5:
													activity.showToast("系统参数错误!");
													break;

												}
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
					}
					break;  
				case R.id.bt_select_operate:
					sessionid = App.getContext().getSessionid();

					System.out.println("++++++++++++++++++++++++" + idCard);
					if (sessionid == null) {
						Intent intent = new Intent(activity.getApplicationContext(),
								MyMeansActivity.class);
						intent.putExtra("FundCode", fund.getFundCode());
						mContext.startActivity(intent);

						return;
					} else {
						activity.showProgressDialog("正在加载");
						RequestParams params = new RequestParams(activity.getApplicationContext());
						//params.put("id", idCard);
						//params.put("passwd", encodePassWord);
						params.put("sessionId", sessionid);
						params.put("condition", fund.getFundCode());
						params.put("fundType", null);
						params.put("company", null);
						activity.execApi(ApiType.GET_DEALSEARCHONETWO, params,
								new OnDataReceivedListener() {

									@Override
									public void onReceiveData(ApiType api,
											String json) {
										// TODO Auto-generated method stub
										if (json == null) {
											activity.showToast("请求失败");
											activity.disMissDialog();
											return;
										}
										if (json != null && !json.equals("")) {
											tInputStringStream = new ByteArrayInputStream(json.getBytes());
											XmlPullParser parser = Xml.newPullParser();
											try {
												parser.setInput(tInputStringStream, "UTF-8");
												int event = parser.getEventType();
												while (event != XmlPullParser.END_DOCUMENT) {
													Log.i("start_document", "start_document");
													switch (event) {
													case XmlPullParser.START_TAG:
														if ("return".equals(parser.getName())) {
															String xmlReturn;
															try {
																xmlReturn = parser.nextText();
																System.out.println("<><><><><><><><><>"
																		+ xmlReturn);
																List<DealSearchResult> list;
																list = JSON.parseArray(xmlReturn,
																			DealSearchResult.class);
																DealSearchResult res = list.get(0);
																Intent intent = new Intent(activity.getApplicationContext(),
																		DealApplyActivity.class);
																Bundle bundle = new Bundle();
																//bundle.putString("IDCard", idCard);
																//bundle.putString("PassWord", encodePassWord);
																bundle.putString("sessionId", sessionid);
																bundle.putSerializable("DealSearchResult", res);
																// intent.putExtra("IDCard", encodeIdCard);
																// intent.putExtra("PassWord", encodePassWord);
																intent.putExtras(bundle);
																activity.startActivity(intent);
																
														

															} catch (IOException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}

														}
														break;

													}
													try {
														event = parser.next();

													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

												}
												try {
													tInputStringStream.close();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

											} catch (XmlPullParserException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											activity.disMissDialog();
										}


									}
								});

					}

					break;
				default:
					break;
				}
			}
		};
		// 判断是否为选中，选中图片为红色，else 要把符合条件的view的重新赋值，即不符合条件的为白色。
		if (fund.getIsFlag() != null && fund.getIsFlag().equals("1")) {
			holder.img_selected.setImageResource(R.drawable.red_star_solid_big);
		} else {
			holder.img_selected
					.setImageResource(R.drawable.red_star_hollow_big);
		}
		String isOpenToBuy = fund.getIsOpenToBuy();
		System.out.println("isOpenToBuy============>"+isOpenToBuy);
		holder.img_selected.setOnClickListener(clickListener);
		if (fund.getIsOpenToBuy() != null && fund.getIsOpenToBuy().equals("0")) {
			holder.bt_buy.setText("购买");
			holder.bt_buy.setTextSize(15);
			holder.bt_buy.setOnClickListener(clickListener);
			holder.bt_buy.setBackgroundResource(R.drawable.selector_red_button);
		}else if (fund.getIsOpenToBuy() != null && fund.getIsOpenToBuy().equals("1")) {
			holder.bt_buy.setText("购买");
			holder.bt_buy.setTextSize(15);
			holder.bt_buy.setOnClickListener(clickListener);
			holder.bt_buy.setBackgroundResource(R.drawable.selector_red_button);
		} else if (fund.getIsOpenToBuy() != null && fund.getIsOpenToBuy().equals("6")) {
			holder.bt_buy.setText("购买");
			holder.bt_buy.setTextSize(15);
			holder.bt_buy.setOnClickListener(clickListener);
			holder.bt_buy.setBackgroundResource(R.drawable.selector_red_button);
		}else  {
			holder.bt_buy.setText("购买");
			holder.bt_buy.setTextSize(15);
			holder.bt_buy.setBackgroundColor(Color.GRAY);
			holder.bt_buy.setClickable(false);

		} 
		return convertView;
	}

	@Override
	protected List<MySelectFund> setData() {
		return mList;
	}

	public void setList(List<MySelectFund> list, String userName,
			BaseActivity activity, int index) {
		if (list != null) {
			mList = list;
		} else {
			mList = new ArrayList<MySelectFund>();
		}
		this.userName = userName;
		this.activity = activity;
		this.index = index;
	}

	class ViewHolder {
		TextView tv_fundName;
		TextView tv_fundCode;
		TextView tv_dealDate;
		TextView tv_unitEquity;
		TextView tv_totalEquity;
		TextView tv_dayBenefit;
		TextView tv_weekRedound;
		TextView tv_monthRedound;
		TextView tv_seasonRedound;
		TextView tv_halfYearRedound;
		TextView tv_yearRedound;
		TextView tv_toyearRedound;

		ImageView img_selected;
		Button bt_buy;
	}

	@Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
		if (mCanLoadMore) {// 存在加载更多功能
			if (mLastItemIndex == mCount && pScrollState == SCROLL_STATE_IDLE) {
				// SCROLL_STATE_IDLE=0，滑动停止
				if (mEndState != ENDINT_LOADING) {
					if (mIsAutoLoadMore) {// 自动加载更多，我们让FootView显示 “更 多”
						if (mCanRefresh) {
							// 存在下拉刷新并且HeadView没有正在刷新时，FootView可以自动加载更多。
							if (mHeadState != REFRESHING) {
								// FootView显示 : 更 多 ---> 加载中...
								mEndState = ENDINT_LOADING;
								onLoadMore();
								changeEndViewByState();
							}
						} else {// 没有下拉刷新，我们直接进行加载更多。
								// FootView显示 : 更 多 ---> 加载中...
							mEndState = ENDINT_LOADING;
							onLoadMore();
							changeEndViewByState();
						}
					} else {// 不是自动加载更多，我们让FootView显示 “点击加载”
							// FootView显示 : 点击加载 ---> 加载中...
						mEndState = ENDINT_MANUAL_LOAD_DONE;
						changeEndViewByState();
					}
				}
			}
		} else if (mEndRootView != null
				&& mEndRootView.getVisibility() == VISIBLE) {
			// 突然关闭加载更多功能之后，我们要移除FootView。
			System.out.println("this.removeFooterView(endRootView);...");
			mEndRootView.setVisibility(View.GONE);
			this.removeFooterView(mEndRootView);
		}

	}

	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem,
			int pVisibleItemCount, int pTotalItemCount) {
		mFirstItemIndex = pFirstVisibleItem;
		mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
		mCount = pTotalItemCount - 2;
		if (pTotalItemCount > pVisibleItemCount) {
			mEnoughCount = true;
			// endingView.setVisibility(View.VISIBLE);
		} else {
			mEnoughCount = false;
		}

	}

	/**
	 * 改变加载更多状态
	 * 
	 * @date 2013-11-11 下午10:05:27
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void changeEndViewByState() {
		if (mCanLoadMore) {
			// 允许加载更多
			switch (mEndState) {
			case ENDINT_LOADING:// 刷新中

				// 加载中...
				if (mEndLoadTipsTextView.getText().equals(
						R.string.p2refresh_doing_end_refresh)) {
					break;
				}
				mEndLoadTipsTextView
						.setText(R.string.p2refresh_doing_end_refresh);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.VISIBLE);
				break;
			case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成

				// 点击加载
				mEndLoadTipsTextView
						.setText(R.string.p2refresh_end_click_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.VISIBLE);
				break;
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成

				// 更 多
				mEndLoadTipsTextView.setText(R.string.p2refresh_end_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.VISIBLE);
				break;
			default:
				// 原来的代码是为了： 当所有item的高度小于ListView本身的高度时，
				// 要隐藏掉FootView，大家自己去原作者的代码参考。

				// if (enoughCount) {
				// endRootView.setVisibility(View.VISIBLE);
				// } else {
				// endRootView.setVisibility(View.GONE);
				// }
				break;
			}
		}
	}

	/**
	 * 原作者的，我没改动，请读者自行优化。
	 */
	public boolean onTouchEvent(MotionEvent event) {

		if (mCanRefresh) {
			if (mCanLoadMore && mEndState == ENDINT_LOADING) {
				// 如果存在加载更多功能，并且当前正在加载更多，默认不允许下拉刷新，必须加载完毕后才能使用。
				return super.onTouchEvent(event);
			}

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				if (mFirstItemIndex == 0 && !mIsRecored) {
					mIsRecored = true;
					mStartY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (mHeadState != REFRESHING && mHeadState != LOADING) {
					if (mHeadState == DONE) {

					}
					if (mHeadState == PULL_TO_REFRESH) {
						mHeadState = DONE;
						changeHeaderViewByState();
					}
					if (mHeadState == RELEASE_TO_REFRESH) {
						mHeadState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				mIsRecored = false;
				mIsBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!mIsRecored && mFirstItemIndex == 0) {
					mIsRecored = true;
					mStartY = tempY;
				}

				if (mHeadState != REFRESHING && mIsRecored
						&& mHeadState != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，
					// 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (mHeadState == RELEASE_TO_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - mStartY) / RATIO < mHeadViewHeight)
								&& (tempY - mStartY) > 0) {
							mHeadState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - mStartY <= 0) {
							mHeadState = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (mHeadState == PULL_TO_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - mStartY) / RATIO >= mHeadViewHeight) {
							mHeadState = RELEASE_TO_REFRESH;
							mIsBack = true;
							changeHeaderViewByState();
						} else if (tempY - mStartY <= 0) {
							mHeadState = DONE;
							changeHeaderViewByState();
						}
					}

					if (mHeadState == DONE) {
						if (tempY - mStartY > 0) {
							mHeadState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (mHeadState == PULL_TO_REFRESH) {
						mHeadView.setPadding(0, -1 * mHeadViewHeight
								+ (tempY - mStartY) / RATIO, 0, 0);

					}

					if (mHeadState == RELEASE_TO_REFRESH) {
						mHeadView.setPadding(0, (tempY - mStartY) / RATIO
								- mHeadViewHeight, 0, 0);
					}
				}
				break;
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当HeadView状态改变时候，调用该方法，以更新界面
	 * 
	 * @date 2013-11-20 下午4:29:44
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void changeHeaderViewByState() {
		switch (mHeadState) {
		case RELEASE_TO_REFRESH:
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);

			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mArrowAnim);
			// 松开刷新
			mTipsTextView.setText(R.string.p2refresh_release_refresh);

			break;
		case PULL_TO_REFRESH:
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (mIsBack) {
				mIsBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mArrowReverseAnim);
				// 下拉刷新
				mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			} else {
				// 下拉刷新
				mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			}
			break;

		case REFRESHING:
			mHeadView.setPadding(0, 0, 0, 0);

			// 华生的建议：
			// 实际上这个的setPadding可以用动画来代替。我没有试，但是我见过。其实有的人也用Scroller可以实现这个效果，
			// 我没时间研究了，后期再扩展，这个工作交给小伙伴你们啦~ 如果改进了记得发到我邮箱噢~
			// 本人邮箱： xxzhaofeng5412@gmail.com

			mProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			// 正在刷新...
			mTipsTextView.setText(R.string.p2refresh_doing_head_refresh);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

			// 此处可以改进，同上所述。

			mProgressBar.setVisibility(View.GONE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.arrow);
			// 下拉刷新
			mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

	/**
	 * 下拉刷新监听接口
	 * 
	 * @date 2013-11-20 下午4:50:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 加载更多监听接口
	 * 
	 * @date 2013-11-20 下午4:50:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
		if (pRefreshListener != null) {
			mRefreshListener = pRefreshListener;
			mCanRefresh = true;
		}
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			mCanLoadMore = true;
			if (mCanLoadMore && getFooterViewsCount() == 0) {
				addFooterView();
			}
		}
	}

	/**
	 * 正在下拉刷新
	 * 
	 * @date 2013-11-20 下午4:45:47
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void onRefresh() {
		if (mRefreshListener != null) {
			mRefreshListener.onRefresh();
		}
	}

	/**
	 * 下拉刷新完成
	 * 
	 * @date 2013-11-20 下午4:44:12
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void onRefreshComplete() {
		// 下拉刷新后是否显示第一条Item
		// if(mIsMoveToFirstItemAfterRefresh)setSelection(0);

		mHeadState = DONE;
		// 最近更新: Time
		mLastUpdatedTextView.setText(getResources().getString(
				R.string.p2refresh_refresh_lasttime)
				+ new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA)
						.format(new Date()));
		changeHeaderViewByState();
	}

	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 * 
	 * @date 2013-11-20 下午4:35:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			// 加载中...
			mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
			mEndLoadTipsTextView.setVisibility(View.VISIBLE);
			mEndLoadProgressBar.setVisibility(View.VISIBLE);

			mLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * 加载更多完成
	 * 
	 * @date 2013-11-11 下午10:21:38
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void onLoadMoreComplete() {
		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		} else {
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
		changeEndViewByState();
	}

	/**
	 * 主要更新一下刷新时间啦！
	 * 
	 * @param adapter
	 * @date 2013-11-20 下午5:35:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void setAdapter(BaseAdapter adapter) {
		// 最近更新: Time
		mLastUpdatedTextView.setText(getResources().getString(
				R.string.p2refresh_refresh_lasttime)
				+ new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA)
						.format(new Date()));
		
		
		super.setAdapter(adapter);
	}

}
