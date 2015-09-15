package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class BBXActivity extends BaseActivity {
	private String texts[] = null;
	private int images[] = null;
	private String userName;
	private String encodePassWord, idCard;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_bbx);

		images = new int[] { R.drawable.bbx__select, R.drawable.bbx_search,
				R.drawable.bbx_kaihu, R.drawable.bbx_deal,
				R.drawable.bbx_information, R.drawable.bbx_zdtj,
				R.drawable.bbx_dct, R.drawable.bbx_yjt };
		texts = new String[] { "自选", "查询", "开户", "交易", "资讯", "重点推荐", "点财通",
				"一键通" };
		userName = App.getContext().getUserName();

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("百宝箱");

		GridView gridview = (GridView) findViewById(R.id.gv_bbx);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 8; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", images[i]);
			map.put("itemText", texts[i]);
			lstImageItem.add(map);
		}

		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,// 数据源
				R.layout.item_bbx,// 显示布局
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.bbx_image, R.id.bbx_text });
		gridview.setAdapter(saImageItems);
		gridview.setOnItemClickListener(new ItemClickListener());
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long rowid) {
			HashMap<String, Object> item = (HashMap<String, Object>) parent
					.getItemAtPosition(position);
			// 获取数据源的属性值
			String itemText = (String) item.get("itemText");
			Object object = item.get("itemImage");
			// Toast.makeText(GvActivity.this, itemText,
			// Toast.LENGTH_LONG).show();

			// 根据图片进行相应的跳转
			switch (images[position]) {
			case R.drawable.bbx__select:

				// 基金自选
				if (userName == null) {
					showToast("未登录，请先登录！");
					startActivity(MyMeansActivity.class);
					return;
				} else {

					Intent myFund = new Intent(BBXActivity.this,
							FundSelectActivity.class);
					myFund.putExtra("UserName", userName);
					startActivity(myFund);
				}

				break;
			case R.drawable.bbx_search:
				// 基金查询
				if (userName == null) {
					Intent myFund = new Intent(BBXActivity.this,
							QueryFundActivity.class);
					myFund.putExtra("UserName", "");
					startActivity(myFund);
				} else {
					Intent myFund = new Intent(BBXActivity.this,
							QueryFundActivity.class);
					myFund.putExtra("UserName", userName);
					startActivity(myFund);
				}
				break;
			case R.drawable.bbx_kaihu:
				// 开户
//				JavaScriptInterface jspi = new JavaScriptInterface(
//						BBXActivity.this);
//				jspi.showDetial("https://apptrade.myfund.com:8383/appweb/page/frame/frame.htm?test=y");
				Intent intent1 = new Intent(BBXActivity.this,WriteInforActivity.class);
				startActivity(intent1);
				break;
			case R.drawable.bbx_deal:
				// 交易
				idCard = App.getContext().getIdCard();
				encodePassWord = App.getContext().getEncodePassWord();

				System.out.println("++++++++++++++++++++++++" + idCard);
				if (encodePassWord == null) {
					startActivity(MyMeansActivity.class);
					return;
				} else {

					RequestParams params = new RequestParams(
							getApplicationContext());
					params.put("id", idCard);
					// params.put(RequestParams.MOBILE, username);
					params.put("passwd", encodePassWord);
					execApi(ApiType.GET_DEALLOGIN, params);
					showProgressDialog("正在加载");

				}
				// JavaScriptInterface jspi1 = new
				// JavaScriptInterface(BBXActivity.this);
				// jspi1.showDetial("https://apptrade.myfund.com:8383/appweb/");
				// startActivity(DealActivity.class);
				break;
			case R.drawable.bbx_information:
				// 快讯
				startActivity(FundNewsActivity.class);
				break;
			case R.drawable.bbx_zdtj:
				// 重点推荐
				startActivity(ExpertSelectedActivity.class);
				break;
			case R.drawable.bbx_dct:

				// 点财通
				Intent intent = new Intent(BBXActivity.this,
						DianCaiTongActivity.class);
				intent.putExtra("ImageID", "01");
				startActivity(intent);
				break;
			case R.drawable.bbx_yjt:
				// 一键通
				startActivity(YiJianTongActivity.class);
				break;
			}

		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// 在这里会收到数据,一定要判断是否为空
		if (json == null) {
			disMissDialog();
			showToast("获取失败!");
			return;
		}
		if (api == ApiType.GET_DEALLOGIN) {

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
								try {
									String xmlReturn = parser.nextText();
									System.out.println("<><><><><><><><><>"
											+ xmlReturn);

									try {
										JSONObject jsonObj = new JSONObject(
												xmlReturn);
										if (xmlReturn.contains("custname")) {

											// App.getContext().setEncodePassWord(encodePassWord);
											// App.getContext().setIdCard(idCard);
											//
											// SharedPreferences sPreferences =
											// getSharedPreferences("Setting",
											// MODE_PRIVATE);
											// Editor editor =
											// sPreferences.edit();
											// editor.putString("EncodePassWord",
											// encodePassWord);
											// editor.putString("IDCard",
											// idCard);
											// editor.commit();

											disMissDialog();

											if (jsonObj.getString("risklevel")
													.equals("o")
													|| jsonObj.getString(
															"expiredflag")
															.equals("Y")) {
												showToast("您的风险等级为0，请进行风险评估");
												Intent intent = new Intent(
														BBXActivity.this,
														RiskAssessmentActivity.class);
												intent.putExtra("IDCard",
														idCard);
												intent.putExtra("PassWord",
														encodePassWord);
												intent.putExtra(
														"CustomRiskLevel",
														jsonObj.getString("risklevel"));
												intent.putExtra(
														"DepositacctName",
														jsonObj.getString("depositacctname"));
												intent.putExtra(
														"TotalFundMarketValue",
														jsonObj.getString("totalfundmarketvalue"));
												intent.putExtra(
														"CountFund",
														jsonObj.getString("countfund"));
												startActivity(intent);

											} else {
												showToast("登陆成功！！");
												Intent intent = new Intent(
														BBXActivity.this,
														DealInforActivity.class);
												intent.putExtra("IDCard",
														idCard);
												intent.putExtra("PassWord",
														encodePassWord);
												intent.putExtra(
														"CustomRiskLevel",
														jsonObj.getString("risklevel"));
												intent.putExtra(
														"DepositacctName",
														jsonObj.getString("depositacctname"));
												intent.putExtra(
														"TotalFundMarketValue",
														jsonObj.getString("totalfundmarketvalue"));
												intent.putExtra(
														"CountFund",
														jsonObj.getString("countfund"));
												startActivity(intent);
											}

										} else {
											showToast("登录失败，请重新登陆！！");
											return;
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// String returnResult =
									// jsonObj.getString("loginflag");

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

			}
			disMissDialog();
		}
	}
}
