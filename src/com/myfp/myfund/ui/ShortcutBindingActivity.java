package com.myfp.myfund.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.BankTypeResult;
import com.myfp.myfund.views.ArrayWheelAdapter;
import com.myfp.myfund.views.OnWheelChangedListener;
import com.myfp.myfund.views.WheelView;

public class ShortcutBindingActivity extends BaseActivity{
	public String category1[] = new String[] { "福建", "山东", "四川", "云南", "北京",
			"河北", "江苏", "台湾", "辽宁", "吉林", "浙江", "西藏", "青海", "澳门", "湖北",
			"其他", "广西", "河南", "山西", "重庆", "江西", "天津", "宁夏", "陕西", "上海", "香港",
			"内蒙古", "黑龙江", "广东", "甘肃", "贵州", "新疆", "湖南", "海南", "安徽" };

	public String category2[][] = new String[][] {
			new String[] { "福州市", "龙岩市", "南平市", "宁德市", "莆田市", "泉州市", "三明市",
					"厦门市", "漳州市" },
			new String[] { "济南市", "东营市", "滨州市", "淄博市", "德州市", "济宁市", "聊城市",
					"临沂市", "莱芜市", "青岛市", "日照市", "威海市", "泰安市", "潍坊市", "烟台市",
					"菏泽市", "枣庄市" },
			new String[] { "成都市", "甘孜藏族自治州", "自贡市", "阿坝藏族羌族自治州", "巴中市", "德阳市",
					"广安市", "广元市", "凉山彝族自治州", "乐山市", "攀枝花市", "南充市", "内江市",
					"泸州市", "绵阳市", "遂宁市", "雅安市", "宜宾市", "眉山市", "资阳市" },
			new String[] { "昆明市", "大理白族自治州", "昭通市", "保山市", "德宏傣族景颇族自治州",
					"迪庆藏族自治州", "楚雄彝族自治州", "临沧地区", "丽江市", "怒江傈僳族自治州", "曲靖市",
					"思茅地区", "西双版纳傣族自治州", "文山壮族苗族自治州", "红河哈尼族彝族自治州", "玉溪市" },
			new String[] { "北京市" },
			new String[] { "石家庄市", "邯郸市", "邢台市", "保定市", "张家口市", "沧州市", "承德市",
					"廊坊市", "秦皇岛市", "唐山市", "衡水市" },
			new String[] { "南京市", "淮安市", "常州市", "连云港市", "南通市", "徐州市", "苏州市",
					"无锡市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市" },
			new String[] { "台湾" },
			new String[] { "沈阳市", "大连市", "阜新市", "抚顺市", "本溪市", "鞍山市", "丹东市",
					"锦州市", "朝阳市", "辽阳市", "盘锦市", "铁岭市", "营口市", "葫芦岛市" },
			new String[] { "长春市", "白城市", "白山市", "吉林市", "辽源市", "四平市", "松原市",
					"通化市", "延边朝鲜族自治州" },
			new String[] { "杭州市", "嘉兴市", "金华市", "衢州市", "丽水市", "宁波市", "绍兴市",
					"台州市", "温州市", "湖州市", "舟山市" },
			new String[] { "拉萨市", "阿里地区", "昌都市", "林芝地区", "那曲地区", "山南地区",
					"日喀则地区" },
			new String[] { "西宁市", "海东地区", "海南藏族自治州", "海北藏族自治州", "黄南藏族自治州",
					"果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州" },
			new String[] { "澳门" },
			new String[] { "武汉市", "黄冈市", "黄石市", "恩施土家族苗族自治州", "鄂州市", "荆门市",
					"荆州市", "孝感市", "省直辖县级行政单位", "十堰市", "襄樊市", "咸宁市", "宜昌市",
					"随州市" },
			new String[] { "北美洲", "南美洲", "大洋洲", "欧洲", "亚洲", "非洲", "虚拟社团" },
			new String[] { "南宁市", "防城港市", "北海市", "百色市", "贺州市", "桂林市", "来宾市",
					"柳州市", "崇左市", "钦州市", "贵港市", "梧州市", "河池市", "玉林市" },
			new String[] { "郑州市", "开封市", "驻马店市", "安阳市", "焦作市", "洛阳市", "濮阳市",
					"漯河市", "南阳市", "平顶山市", "新乡市", "信阳市", "许昌市", "商丘市", "三门峡市",
					"鹤壁市", "周口市", "济源市" },
					
			new String[] { "太原市", "大同市", "晋城市", "晋中市", "长治市", "临汾市", "吕梁地区",
					"忻州市", "朔州市", "阳泉市", "运城市" },
			new String[] { "重庆市", "涪陵市", "黔江市", "万县市" },
			new String[] { "南昌市", "抚州市", "赣州市", "吉安市", "景德镇市", "九江市", "萍乡市",
					"新余市", "上饶市", "鹰潭市", "宜春市" },
			new String[] { "天津市" },
			new String[] { "银川市", "固原市", "石嘴山市", "吴忠市", "中卫市" },
			new String[] { "西安市", "宝鸡市", "安康市", "商洛市", "铜川市", "渭南市", "咸阳市",
					"延安市", "汉中市", "榆林市" },
			new String[] { "上海市" },
			new String[] { "香港" },
			new String[] { "呼和浩特市", "阿拉善盟", "巴彦淖尔盟", "包头市", "赤峰市", "兴安盟",
					"乌兰察布盟", "乌海市", "锡林郭勒盟", "呼伦贝尔盟", "伊克昭盟", "通辽市" },
			new String[] { "哈尔滨市", "大庆市", "大兴安岭地区", "鸡西市", "佳木斯市", "牡丹江市",
					"齐齐哈尔市", "七台河市", "双鸭山市", "绥化市", "伊春市", "鹤岗市", "黑河市" },
			new String[] { "广州市", "佛山市", "惠州市", "东莞市", "江门市", "揭阳市", "潮州市",
					"茂名市", "梅州市", "清远市", "汕头市", "汕尾市", "深圳市", "韶关市", "阳江市",
					"河源市", "云浮市", "中山市", "珠海市", "湛江市", "肇庆市" },
			new String[] { "兰州市", "甘南藏族自治州", "定西地区", "白银市", "嘉峪关市", "金昌市",
					"酒泉市", "临夏回族自治州", "陇南地区", "平凉市", "庆阳市", "天水市", "武威市", "张掖市" },
			new String[] { "贵阳市", "毕节地区", "遵义市", "安顺市", "六盘水市", "黔东南苗族侗族自治州",
					"黔南布依族苗族自治州", "黔西南布依族苗族自治", "铜仁地区" },
			new String[] { "乌鲁木齐市", "喀什地区", "克孜勒苏柯尔克孜自治", "克拉玛依市", "阿克苏地区",
					"阿勒泰地区", "巴音郭楞蒙古自治州", "哈密地区", "博尔塔拉蒙古自治州", "昌吉回族自治州",
					"塔城地区", "吐鲁番地区", "和田地区", "石河子市", "伊犁哈萨克自治州" },
			new String[] { "长沙市", "怀化市", "郴州市", "常德市", "娄底市", "邵阳市", "湘潭市",
					"湘西土家族苗族自治州", "衡阳市", "永州市", "益阳市", "岳阳市", "株洲市", "张家界市" },
			new String[] { "海口市", "三亚市", "省直辖行政单位" },
			new String[] { "合肥市", "淮北市", "淮南市", "黄山市", "安庆市", "蚌埠市", "巢湖市",
					"池州市", "滁州市", "六安市", "马鞍山市", "宣城市", "宿州市", "铜陵市", "芜湖市",
					"阜阳市", "亳州市", "达州市" }, };

	private TextView shortcut_bank_name;
	private TextView shortcut_bank_idcard;
	private TextView shortcut_text_DBK;
	private TextView shortcut_text_provinces;
	private TextView shortcut_text_Openingbank;
	private String vLeft;
	private String vRight;
	private String channelid;
	private String paycenterid;
	private Bundle bundle;
	public static ShortcutBindingActivity instance = null;

	private String channelname;
	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_binding_bank);
		bundle = getIntent().getExtras();
		instance=this;
		
	}

	@Override
	protected void initViews() {
		setTitle("绑定银行卡");
		shortcut_bank_name = (TextView) findViewById(R.id.shortcut_bank_name);
		shortcut_bank_idcard = (TextView) findViewById(R.id.shortcut_bank_idcard);
		shortcut_text_DBK = (TextView) findViewById(R.id.shortcut_text_DBK);
		shortcut_text_provinces = (TextView) findViewById(R.id.shortcut_text_provinces);
		shortcut_text_Openingbank = (TextView) findViewById(R.id.shortcut_text_Openingbank);
		findViewAddListener(R.id.shortcut_new_DBK);
		findViewAddListener(R.id.shortcut_new_Provinces);
		findViewAddListener(R.id.shortcut_new_Openingbank);
		findViewAddListener(R.id.shortcut_Binding_confirm);
		shortcut_bank_name.setText(getIntent().getStringExtra("SDName"));
		shortcut_bank_idcard.setText(getIntent().getStringExtra("SDIdCard").replace(getIntent().getStringExtra("SDIdCard").substring(8, 14), "*****"));
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_new_DBK:
			Intent intent = new Intent(this,
					BankTypeActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.shortcut_new_Provinces:
			if (shortcut_text_DBK.getText().length() == 0||shortcut_text_DBK.getText().toString().equals("点此选择")) {
				showToast("请选择银行卡类型");
				return;
			} else {
				showSelectDialog(this, "选择所在省市", category1,category2);
				System.out.println("category1----------->"+category1);
				System.out.println("category2----------->"+category2);
			}
			break;
		case R.id.shortcut_new_Openingbank:
			if (shortcut_text_provinces.getText().length() == 0||shortcut_text_provinces.getText().toString().equals("点此选择")) {
				showToast("请选择省市");
				return;
			} else {
				Intent intent4 = new Intent(this,
						AccountAddressActivity.class);
				intent4.putExtra("City", vRight);
				intent4.putExtra("ChannelId", channelid);
				startActivityForResult(intent4, 4);
			}
			break;
		case R.id.shortcut_Binding_confirm:
			if (shortcut_text_Openingbank.getText().length()==0||shortcut_text_provinces.getText().toString().equals("点此选择")) {
				showToast("请完善您绑定的银行卡信息");
				return;
			}else {
				Intent intent2=new Intent(this, ShortcutObligateActivity.class);
				System.out.println("vLeft1----->"+vLeft);
				bundle.putString("NewProvinces", vLeft);
				bundle.putString("NewCity",vRight);
				bundle.putString("Phone", getIntent().getStringExtra("Phone"));
				bundle.putString("SDName", getIntent().getStringExtra("SDName"));
				bundle.putString("SDIdCard", getIntent().getStringExtra("SDIdCard"));
				bundle.putString("ChanneLid",channelid);
				bundle.putString("ChanneName", channelname);
				bundle.putString("PayCenterId", paycenterid);
				bundle.putString("NewDBK", shortcut_text_DBK.getText().toString());
				bundle.putString("AccountProvinces", shortcut_text_provinces.getText().toString());
				bundle.putString("AccountAddress", shortcut_text_Openingbank.getText().toString());
				bundle.putString("SDealPass", getIntent().getStringExtra("SDealPass"));
				bundle.putString("Code", getIntent().getStringExtra("Code"));
				bundle.putString("Phone", getIntent().getStringExtra("Phone"));
				intent2.putExtras(bundle);
				startActivity(intent2);
			}
			break;

		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		// 银行卡类型
		case 0:
			if (data != null) {
				Bundle bundle = data.getExtras();
				BankTypeResult res;
				res = (BankTypeResult) bundle.getSerializable("BankTypeResult");
				shortcut_text_DBK.setText(res.getChannelname());
				channelname = res.getChannelname();
				channelid = res.getChannelid();
				paycenterid = res.getPaycenterid();
			}

			break;
		
		// 省份
		case 2:
			if (data != null) {
				Bundle bundle = data.getExtras();
				shortcut_text_provinces.setText(bundle.getString("Pname"));
				shortcut_text_provinces.setText(bundle.getString("Cname"));
			}

			break;
		case 3:
			if (data != null) {
				Bundle bundle = data.getExtras();
				shortcut_text_provinces.setText(bundle.getString("Cname"));
			}
		case 4:
			if (data != null) {
				Bundle bundle = data.getExtras();
				shortcut_text_Openingbank.setText(bundle
						.getString("AccountAddress"));
			}

			break;

		default:
			break;
		}
	}
	private void showSelectDialog(Context context, String title,
			final String[] left, final String[][] right) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle(title);
		LinearLayout llContent = new LinearLayout(context);
		llContent.setOrientation(LinearLayout.HORIZONTAL);
		final WheelView wheelLeft = new WheelView(context);
		wheelLeft.setVisibleItems(5);
		wheelLeft.setCyclic(false);
		wheelLeft.setAdapter(new ArrayWheelAdapter<String>(left));
		final WheelView wheelRight = new WheelView(context);
		wheelRight.setVisibleItems(5);
		wheelRight.setCyclic(true);
		wheelRight.setAdapter(new ArrayWheelAdapter<String>(right[0]));
		LinearLayout.LayoutParams paramsLeft = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
		paramsLeft.gravity = Gravity.LEFT;

		LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				1);
		
		paramsRight.gravity = Gravity.RIGHT;
		llContent.addView(wheelLeft, paramsLeft);
		llContent.addView(wheelRight, paramsRight);
		wheelLeft.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheelRight.setAdapter(new ArrayWheelAdapter<String>(
						right[newValue]));
				wheelRight.setCurrentItem(right[newValue].length / 2);
			}
		});
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
				new DialogInterface.OnClickListener() {
					

					@Override
					public void onClick(DialogInterface dialog, int which) {
						int leftPosition = wheelLeft.getCurrentItem();
						vLeft = left[leftPosition];
						vRight = right[leftPosition][wheelRight
								.getCurrentItem()];
						shortcut_text_provinces.setText(vLeft + "-" + vRight);
						//new_Openingbank.setText(vRight);
						System.out.println("VLeft===========>"+vLeft);
						System.out.println("vRight===========>"+vRight);
						dialog.dismiss();
					}
				});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.setView(llContent);
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

}
