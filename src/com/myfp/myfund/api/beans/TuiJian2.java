package com.myfp.myfund.api.beans;

public class TuiJian2 {
		private String FundCode,FundName,FundType,ThisYearRedound,RecommendReason;

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

	

		public String getThisYearRedound() {
			return ThisYearRedound;
		}

		public void setThisYearRedound(String thisYearRedound) {
			ThisYearRedound = thisYearRedound;
		}

		public String getRecommendReason() {
			return RecommendReason;
		}

		public void setRecommendReason(String recommendReason) {
			RecommendReason = recommendReason;
		}

		@Override
		public String toString() {
			return "TuiJian2 [FundCode=" + FundCode + ", FundName=" + FundName
					+ ", FundType=" + FundType + ", OneYearRedound="
					+ ThisYearRedound + ", RecommendReason=" + RecommendReason
					+ "]";
		}
		
}
