/**   
 * @Title: FundListResult.java
 * @Package com.rndchina.cailifang.api.beans
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyao  
 * @date 2014-8-7 下午5:12:01
 * @version V1.0   
 */

package com.myfp.myfund.api.beans;

import java.util.List;

import com.myfp.myfund.api.ResponseResult;

/**
 * @ClassName: FundListResult
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author liangyao
 * @date 2014-8-7 下午5:12:01
 * 
 */

public class FundListResult extends ResponseResult {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 1L;
	private List<FundList> data;

	public FundListResult(List<FundList> data) {
		super();
		this.data = data;
	}

	public FundListResult() {
		super();
	}

	public List<FundList> getData() {
		return data;
	}

	public void setData(List<FundList> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "FundListResult [data=" + data + "]";
	}

	public static class FundList {
		private String FundCode;
		private String FundName;
		private String FundType;
		// 单位净值
		private String UnitEquity;
		// 累计净值
		private String TotalEquity;
		// 日涨幅
		private String DayBenefit;
		// 净值日期
		private String DealDate;
		// 周涨跌幅
		private String OneWeekRedound;
		// 月涨跌幅
		private String OneMonthRedound;
		// 季涨跌幅
		private String ThreeMonthRedound;
		// 半年涨跌幅
		private String SixMonthRedound;
		// 年涨跌幅
		private String OneyearRedound;
		// 今年涨跌幅
		private String ThisYearRedound;

		public String getFundCode() {
			return FundCode;
		}

		public void setFundCode(String fundCode) {
			FundCode = fundCode;
		}

		public String getFundName() {
			return FundName;
		}

		public void setFundName(String fundName) {
			FundName = fundName;
		}

		public String getFundType() {
			return FundType;
		}

		public void setFundType(String fundType) {
			FundType = fundType;
		}

		public String getUnitEquity() {
			return UnitEquity;
		}

		public void setUnitEquity(String unitEquity) {
			UnitEquity = unitEquity;
		}

		public String getTotalEquity() {
			return TotalEquity;
		}

		public void setTotalEquity(String totalEquity) {
			TotalEquity = totalEquity;
		}

		public String getDayBenefit() {
			return DayBenefit;
		}

		public void setDayBenefit(String dayBenefit) {
			DayBenefit = dayBenefit;
		}

		public String getDealDate() {
			return DealDate;
		}

		public void setDealDate(String dealDate) {
			DealDate = dealDate;
		}

		public String getOneWeekRedound() {
			return OneWeekRedound;
		}

		public void setOneWeekRedound(String oneWeekRedound) {
			OneWeekRedound = oneWeekRedound;
		}

		public String getOneMonthRedound() {
			return OneMonthRedound;
		}

		public void setOneMonthRedound(String oneMonthRedound) {
			OneMonthRedound = oneMonthRedound;
		}

		public String getThreeMonthRedound() {
			return ThreeMonthRedound;
		}

		public void setThreeMonthRedound(String threeMonthRedound) {
			ThreeMonthRedound = threeMonthRedound;
		}

		public String getSixMonthRedound() {
			return SixMonthRedound;
		}

		public void setSixMonthRedound(String sixMonthRedound) {
			SixMonthRedound = sixMonthRedound;
		}

		public String getOneyearRedound() {
			return OneyearRedound;
		}

		public void setOneyearRedound(String oneyearRedound) {
			OneyearRedound = oneyearRedound;
		}

		public String getThisYearRedound() {
			return ThisYearRedound;
		}

		public void setThisYearRedound(String thisYearRedound) {
			ThisYearRedound = thisYearRedound;
		}

		@Override
		public String toString() {
			return "FundList [FundCode=" + FundCode + ", FundName=" + FundName
					+ ", FundType=" + FundType + ", UnitEquity=" + UnitEquity
					+ ", TotalEquity=" + TotalEquity + ", DayBenefit="
					+ DayBenefit + ", DealDate=" + DealDate
					+ ", OneWeekRedound=" + OneWeekRedound
					+ ", OneMonthRedound=" + OneMonthRedound
					+ ", ThreeMonthRedound=" + ThreeMonthRedound
					+ ", SixMonthRedound=" + SixMonthRedound
					+ ", OneyearRedound=" + OneyearRedound
					+ ", ThisYearRedound=" + ThisYearRedound + "]";
		}

	}
}
