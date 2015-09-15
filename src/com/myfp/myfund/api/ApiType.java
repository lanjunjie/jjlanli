package com.myfp.myfund.api;


/**
 * 
 * API 请求的类型
 * @author wqz
 *
 */
public enum ApiType {

	TEST_API(""),
	/**	获取banner图片列表	*/
	GET_BANNERS("/Service/DemoService.svc/GetBanner2"),
	/**	登陆	*/
	LOGIN("/Service/DemoService.svc/GetUserLogin"),
	/**基金查询列表-分页*/
	GET_FUNDLIST("/Service/DemoService.svc/GetFundInfor2New"),
	/**基金查询列表-分页2*/
	GET_FUNDLISTTWO("/Service/DemoService.svc/GetFundInfor1New2"),
	/**单只精品列表*/
	GET_ONEQUALITY("/Service/DemoService.svc/GetOneQuality"),
	/**基金快讯列表*/
	GET_FUNDNEWS("/Service/DemoService.svc/GetFundNews2"),
	/**研报快讯列表*/
	GET_RESEARCHNEWS("/Service/DemoService.svc/GetResearchNews"),
	/**快讯具体内容*/
	GET_CONTENTS("/Service/DemoService.svc/GetContents2"),
	/**注册*/
	GET_USERREGIST("/Service/DemoService.svc/GetUserRegist"),
	/**验证码1*/
	GET_CHECK_CODE("/javaDemo/CheckCodeServlet/sendOneMsgApp.do"),
	/**验证码2*/
	GET_CHECK_CODETWO("/data-platform/webservice/web/dctMessage"),
	/**忘记密码*/
	FORGET_PASSWORD("/Service/DemoService.svc/ForgetPassWord"),
	/**获得个人信息*/
	GET_USER_INFO("/Service/DemoService.svc/GetUserInfo"),
	/**省心组合*/
	GET_RECOM_FUND("/Service/DemoService.svc/Getrecomfund"),
	/**组合基金占比*/
	GET_RECOM_FUND_PERCENT("/Service/DemoService.svc/Getrecomfund1"),
	/**省心组合详情*/
	GET_RECOM_FUND_DETAIL("/Service/DemoService.svc/Getrecomfund3"),
	/**单只基金基本数据*/
	GET_FUND_DETAIL_INFO("/Service/DemoService.svc/GetFundDetailInfoNew"),
	/**近五日净值*/
	GET_FIVE_UNIT_EQUITY("/Service/DemoService.svc/Get5unitEquity"),
	/**历史净值*/
	GET_HISTORY_EQUITY("/Service/DemoService.svc/GetHistoryEquity"),
	/**评论列表*/
	GET_FUND_REVIEW("/Service/DemoService.svc/GetFundReview"),
	/**发表评论*/
	GET_REVIEW_UPDATE("/Service/DemoService.svc/GetFundReviewUpdate"),
	/**意见反馈*/
	GET_CUS_SUGGEST("/Service/DemoService.svc/GetCusSuggest"),
	/**使用帮助列表*/
	GET_HELP_CONTENT("/Service/DemoService.svc/GethelpContent"),
	/**使用帮助详情*/
	GET_HELP_CONTENT1("/Service/DemoService.svc/GethelpContent1"),
	/**消息中心*/
	GET_NEWS_LIST("/Service/DemoService.svc/GetNewsCenter"),
	/**消息详情*/
	GET_NEWS_CONTENT("/Service/DemoService.svc/GetNewsCenter1"),
	/**修改个人信息*/
	UPDATE_USER_INFO("/Service/DemoService.svc/GetUserInfoUpdate"),
	/**修改绑定手机*/
	UPDATE_Mobile("/Service/DemoService.svc/GetMobileUpdate"),
	/**修改绑定手机用身份证修改*/
	UPDATE_MOBILEID("/appwebnew/ws/webapp-clientspace/updateMobileno"),
	/**俱乐部权限*/
	GET_USER_LEVEL("/Service/DemoService.svc/GetCaiLiFangApply1"),
	/**申请加入俱乐部*/
	GET_CLUB_APPLY("/Service/DemoService.svc/GetCaiLiFangApply"),
	/**限时优品*/
	GET_MONTH_QUALITY1("/Service/DemoService.svc/GetMonthQuality1"),
	/**限时优品历史*/
	GET_MONTH_QUALITY2("/Service/DemoService.svc/GetMonthQuality2"),
	/**基金概况*/
	GET_FUND_PROFILE("/Service/DemoService.svc/GetFundDetailInfo3"),
	/**基金费率*/
	GET_FUND_RATE("/Service/DemoService.svc/GetFeeItems"),
	/**基金业绩*/
	GET_FUND_ACHIEVEMENTS("/Service/DemoService.svc/GetFundDetailInfo4"),
	/**风险指标*/
	GET_FUND_RISK("/Service/DemoService.svc/GetFundDetailInfo5"),
	/**资产配置*/
	GET_ASSET_ALLOCTION("/Service/DemoService.svc/GetFundDetailInfo6"),
	/**十大重仓股*/
	GET_STOCK_TOP10("/Service/DemoService.svc/GetFundDetailInfo7"),
	/**五大重仓债券*/
	GET_BOND_TOP5("/Service/DemoService.svc/GetFundDetailInfo8"),
	/**上证指数深证指数*/
	GET_INDEX("/Service/DemoService.svc/Getindex"),
	/**专家精选banner*/
	GET_BANNER("/Service/DemoService.svc/GetBanner1"),
	/**在线预约*/
	GET_ONLINE_BOOK("/Service/DemoService.svc/GetOnlineBooking"),
	/**俱乐部列表*/
	GET_CLUB_LIST("/Service/DemoService.svc/GetCaiLiFangInfor"),
	/**我的自选*/
	GET_FUND_OPTIONAL("/Service/DemoService.svc/GetFundOptionalNew"),
	/**添加自选(基金浏览为分页含自选)*/
	ADD_MY_FUND("/Service/DemoService.svc/GetFundInfor3New"),
	/**搜索*/
	SEARCH_FUND("/Service/DemoService.svc/GetFundKeywordMate1"),
	/**搜索（不含自选）*/
	SEARCH_FUND1("/Service/DemoService.svc/GetFundKeywordMate"),
	/**单位净值走势*/
	GET_UNIT_CHAR("/Service/DemoService.svc/GetFundDetailInfo1"),
	/**累计收益走势*/
	GET_TOTAL_CHAR("/Service/DemoService.svc/GetFundDetailInfo2"),
	/**省心组合走势*/
	GET_GROUP_CHAR("/Service/DemoService.svc/Getrecomfundchart"),
	/**省心组合同等收益*/
	GET_SAME_EARNINGS("/Service/DemoService.svc/Getrecomfund2"),
	/**添加自选初始列表*/
	INIT_MY_SELECTED("/Service/DemoService.svc/GetFundInfor4New"),
	/**添加自选功能*/
	GET_MY_FUND_CENTER("/Service/DemoService.svc/GetMyFundCenter"),
	/**消息标记已读*/
	MSG_IS_READ("/Service/DemoService.svc/GetMsgIsReadedFlag"),
	/**限时优品最大收益*/
	GET_MAX_EXPECTED("/Service/DemoService.svc/GetMonthMaxExpected"),
	/**版本号*/
	GET_VERSIONS("/javaDemo/AppUpdateServlet/appMyfundVersion.version"),
	/**最新版APK下载*/
	GET_APK("/javaDemo/AppUpdateServlet/appMyfundApk.version"),
	/**我的资产*/
	GET_HOLD_DETAIL("/appweb/ws/webapp-cxf/getFloat"),
	/**我的资产2*/
	GET_HOLD_DETAILTWO("/appwebnew/ws/webapp-cxf/getFloatnew"),
	/**JPUSH列表*/
	GET_JPUSH_LIST("/Service/DemoService.svc/GetJpushList"),
	/**JPUSH详情*/
	GET_JPUSH_DETAIL("/Service/DemoService.svc/GetJpushDetail"),
	/**应用推荐*/
	GET_APPSHARE("/Service/DemoService.svc/GetAppShare"),
	/**交易登陆*/
	GET_DEALLOGIN("/appweb/ws/webapp-cxf/validLogin"), 
	/**交易登陆2*/
	GET_DEALLOGINTWO("/appwebnew/ws/webapp-cxf/validLogin"), 
	/**个人交易信息*/
	GET_DEALINFOR("/appweb/ws/webapp-cxf/getFloat"),
	/**购买*/
	GET_DEALBUY("/appweb/ws/webapp-cxf/buyFund"),
	/**下单*/
	GET_ORDER("/appweb/ws/webapp-cxf/buyFundStep"),
	/**下单2*/
	GET_ORDERTWO("/appwebnew/ws/webapp-cxf/buyFundStepnew"),
	/**银行支付*/
	GET_BANKPAY("/appweb/ws/webapp-cxf/readyPayStep"),
	/**银行支付2*/
	GET_BANKPAYTWO("/appwebnew/ws/webapp-cxf/readyPayStepnew"),
	/**赎回(转换)列表*/
	GET_DEALREDEEM_LIST("/appweb/ws/webapp-cxf/getHoldFunds"),
	/**赎回(转换)列表2*/
	GET_DEALREDEEM_LISTTWO("/appwebnew/ws/webapp-cxf/getHoldFundsnew"),
	/**赎回*/
	GET_DEALREDEEM("/appweb/ws/webapp-cxf/sellFund"),
	/**赎回2*/
	GET_DEALREDEEMTWO("/appwebnew/ws/webapp-cxf/sellFundnew2"),
	/**转换目标基金*/
	GET_CHANGEFORFUND("/appweb/ws/webapp-cxf/getchangeToFund"),
	/**转换目标基金2*/
	GET_CHANGEFORFUNDTWO("/appwebnew/ws/webapp-cxf/getchangeToFundnew"),
	/**转换*/
	GET_CHANGEFUND("/appweb/ws/webapp-cxf/changeFund"),
	/**转换2*/
	GET_CHANGEFUNDTWO("/appwebnew/ws/webapp-cxf/changeFundnew2"),
	/**撤单列表*/
	GET_CANCELLATIONLIST("/appweb/ws/webapp-cxf/searchTrades"),
	/**撤单列表2*/
	GET_CANCELLATIONLISTTWO("/appwebnew/ws/webapp-cxf/searchTradesnew"),
	/**查询基金所有信息*/
	GET_FUNDTOTALINFOR("/appweb/ws/webapp-cxf/getFundTotalInfo"),
	/**查询基金所有信息2*/
	GET_FUNDTOTALINFORTWO("/appwebnew/ws/webapp-cxf/getFundTotalInfonew"),
	/**在线支付银行信息*/
	GET_ONLINEBANKINFOR("/appweb/ws/webapp-cxf/getMyActiveBankList"),
	/**在线支付银行信息2*/
	GET_ONLINEBANKINFORTWO("/appwebnew/ws/webapp-cxf/getMyActiveBankListnew"),
	/**汇款支付银行信息*/
	GET_REMITBANKINFOR("/appweb/ws/webapp-cxf/getMyHKBankList"),
	/**汇款支付银行信息2*/
	GET_REMITBANKINFORTWO("/appwebnew/ws/webapp-cxf/getMyHKBankListnew"),
	/**参考费率*/
	GET_RATEFEE("/appweb/ws/webapp-cxf/getRatefee"),
	/**撤单*/
	GET_CANCELLATION("/appweb/ws/webapp-cxf/deleteTrades"),
	/**撤单2*/
	GET_CANCELLATIONTWO("/appwebnew/ws/webapp-cxf/deleteTradesnew"),
	/**委托查询*/
	GET_ENTURSTSEARCH("/appweb/ws/webapp-cxf/searchHisApp"),
	/**委托查询2*/
	GET_ENTURSTSEARCHTWO("/appwebnew/ws/webapp-cxf/searchHisAppnew"),
	/**交易历史查询*/
	GET_HISTORYSEARCH("/appweb/ws/webapp-cxf/searchHisAck"),
	/**交易历史查询2*/
	GET_HISTORYSEARCHTWO("/appwebnew/ws/webapp-cxf/searchHisAcknew"),
	/**基金查询（交易）*/
	GET_DEALSEARCH("/appweb/ws/webapp-cxf/fundSearch"),
	/**基金查询（交易）2*/
	GET_DEALSEARCHONETWO("/appwebnew/ws/webapp-cxf/fundSearchnew"),
	/**定投查询（交易）*/
	GET_DEALSEARCHTWO("/appwebnew/ws/webapp-cxf/getFundTotalInfonew"),
	/**定投查询（交易）*/
	GET_DTSEARCH("/appweb/ws/webapp-cxf/getFundTotalInfo"),
	/**定投查询（交易）2*/
	GET_DTSEARCHTWO("/appwebnew/ws/webapp-cxf/getFundTotalInfonew"),
	/**获取定投协议*/
	GET_DTAGREEMENT("/appweb/ws/webapp-cxf/checkBeforeAutoSavePlan"),
	/**获取定投协议2*/
	GET_DTAGREEMENTTWO("/appwebnew/ws/webapp-cxf/checkBeforeAutoSavePlannew"),
	/**定投付款*/
	GET_DTPAY("/appweb/ws/webapp-cxf/signSend"),
	/**定投付款2*/
	GET_DTPAYTWO("/appwebnew/ws/webapp-cxf/signSendnew"),
	/**定投管理*/
	GET_DTMANAGE("/appweb/ws/webapp-cxf/searchSavePlanList"),
	/**定投管理2*/
	GET_DTMANAGETWO("/appwebnew/ws/webapp-cxf/searchSavePlanListnew"),
	/**定投终止*/
	GET_DTSTOP("/appweb/ws/webapp-cxf/deleteSavePlan"),
	/**定投终止*/
	GET_DTSTOPTWO("/appwebnew/ws/webapp-cxf/deleteSavePlannew"),
	/**查询账户信息*/
	GET_CHECKINFO("/appweb/ws/webapp-cxf/checkInfo"),
	/**生成随机数*/
	GET_RANDOMBUM("/appweb/ws/webapp-cxf/randomNum"),
	/**发送短信*/
	GET_SENDMSG("/appweb/ws/webapp-cxf/sendMsg"),
	/**重置密码*/
	GET_RESETPASS("/appweb/ws/webapp-cxf/resetTPassword"),
	/**账户信息*/
	GET_ACCOUNTINFOR("/appweb/ws/webapp-cxf/showMyAccountInfo"),
	/**查询小额打款*/
	GET_QRYSMALLMONEY("/appweb/ws/webapp-cxf/qrySmallMoney"),
	/**查询小额打款2*/
	GET_QRYSMALLMONEYTWO("/appwebnew/ws/webapp-cxf/qrySmallMoney"),
	/**小额打款*/
	GET_CHKSMALLMONEY("/appweb/ws/webapp-cxf/chkSmallMoney"),
	/**查询风险评价问题*/
	GET_RISKQUESTION ("/appweb/ws/webapp-cxf/getQuestion"),
	/**查询风险评价问题2*/
	GET_RISKQUESTIONTWO ("/appwebnew/ws/webapp-cxf/getQuestionnew"),
	/**风险评价提交*/
	GET_RISKSUBMIT ("/appweb/ws/webapp-cxf/updateAccountRiskLevel"),
	/**风险评价提交2*/
	GET_RISKSUBMITTWO ("/appwebnew/ws/webapp-cxf/updateAccountRiskLevelnew"),
	/**查询开户信息*/
	GET_OPENACCOUNTSTATUS ("/appweb/ws/webapp-cxf/getOpenAccountStatus"),
	/**查询所有银行*/
	GET_OPENACCOUNTBANKS ("/appweb/ws/webapp-cxf/getALLBankList"),
	/**查询开户行网点*/
	GET_OPENACCOUNTADDRESS ("/appweb/ws/webapp-cxf/querybankCode"),
	/**保存开户信息*/
	GET_OPENACCOUNT ("/appweb/ws/webapp-cxf/openAccount"),
	/**保存开户信息2*/
	GET_OPENACCOUNT2 ("/appwebnew/ws/webapp-cxf/openAccount"),
	/**进行小额打款*/
	GET_SMALLMONEY ("/appweb/ws/webapp-cxf/smallMoney"),
	/**进行小额打款2*/
	GET_SMALLMONEY2 ("/appwebnew/ws/webapp-cxf/smallMoney"),
	/**主页模块儿*/
	GET_MAINPART("/Service/DemoService.svc/GetMainPart"),
	/**活跃度统计*/
	GET_MYFUND_COUNT("/Service/DemoService.svc/myfundCountForAndroid"),
	/**公墓基金*/
	GET_CEMETEEY("/Service/CLFDemo.svc/GetHotfundList"),
	/**特色固收*/
	GET_FEATURE("/Service/CLFDemo.svc/GetSpecfixList"),
	/**私募基金*/
	GET_PLACEMENT("/Service/CLFDemo.svc/GetPrivateFund"),
	/**私募基金*/
	GET_PLACEMENTTWO("/Service/CLFDemo.svc/GetPrivateFund4APP"),
	/**私募基金2*/
	GET_PLACEMENTTWOT("/Service/CLFDemo.svc/GetPrivateFund4APPOther2"),
	/**我的资产*/
	GET_MYPROPERTY("/appweb/ws/webapp-clientspace/getFloat"),
	/**我的资产2*/
	GET_MYPROPERTYNEW("/appwebnew/ws/webapp-clientspace/getFloat"),
	/**我的资产会员*/
	GET_MYPROPERTYMEMBER("/appweb/ws/webapp-clientspace/checkIsMember"),
	/**我的资产会员判断*/
	GET_MEBERJUDGE("/appweb/ws/webapp-clientspace/checkIsMember"),
	/**我的资产会员判断NEW*/
	GET_MEBERJUDGENEW("/appwebnew/ws/webapp-clientspace/checkIsMember"),
	/**会员信息*/
	GET_MEMBERINFORMATION("/appweb/ws/webapp-clientspace/getDctVipInfo"),
	/**会员信息NEW*/
	GET_MEMBERINFORMATIONNEW("/appwebnew/ws/webapp-clientspace/getDctVipInfo"),
	/**申购详情*/
	GET_PURCHASEDETAILS("/appweb/ws/webapp-clientspace/getTradeInfoFoApp"),
	/**申购详情NEW*/
	GET_PURCHASEDETAILSNEW("/appwebnew/ws/webapp-clientspace/getTradeInfoFoApp"),
	/**提现详情*/
	GET_SHOWDETAILS("/data-platform/webservice/web/selectReduceCustPointForOne"),
	/**提现详情2*/
	GET_SHOWDETAILSTWO("/appweb/ws/webapp-clientspace/getPointBackRecordFoApp"),
	/**提现详情2NEW*/
	GET_SHOWDETAILSTWONEW("/appwebnew/ws/webapp-clientspace/getPointBackRecordFoApp"),
	/**持有固收*/
	GET_SOLIDCHARGE("/Service/MyfundDemo.svc/GetHoldfixList"),
	/**购买记录*/
	GET_PURCHASEHISTORY("/Service/MyfundDemo.svc/GetHoldfixList1"),
	/**持有的私募*/
	GET_HOLDAPRIVATE("/Service/MyfundDemo.svc/GetPrivateproductsList"),
	/**私募购买*/
	GET_PRIVATEEQUITYTOBUY("/Service/MyfundDemo.svc/GetPrivateproductsList1"),
	/**提现*/
	GET_WITHDREWDEPOSIT("/appweb/ws/webapp-clientspace/addPointRecord"),
	/**提现NEW*/
	GET_WITHDREWDEPOSITNEW("/appwebnew/ws/webapp-clientspace/addPointRecord"),
	/**判断会员提现*/
	GET_JUDGEWITHDRAWALDEGREE("/appweb/ws/webapp-clientspace/checkMemberDealTime"),
	/**判断会员提现NEW*/
	GET_JUDGEWITHDRAWALDEGREENEW("/appwebnew/ws/webapp-clientspace/checkMemberDealTime"),
	/**撤销返现记录*/
	GET_WITHDRaAWCASHBACKRECORD("/appweb/ws/webapp-clientspace/chexiaoPointBackRecord"),
	/**撤销返现记录NEW*/
	GET_WITHDRaAWCASHBACKRECORDNEW("/appwebnew/ws/webapp-clientspace/chexiaoPointBackRecord"),
	/**基金单页（基金详情） */
	GET_FUNDINFOR("/Service/CLFDemo.svc/GetPrivateFundInfo"),
	/**基金单页（历史收益率） */
	GET_HISTORYRATE("/Service/CLFDemo.svc/GetHistoryrate"),
	/**基金单页（基金净值列表） */
	GET_EQUITYCHART("/Service/CLFDemo.svc/GetEquitychart"),
	/**基金单页（基金经理信息） */
	GET_FUNDMANAGER("/Service/CLFDemo.svc/GetFundManager"),
	/**基金单页（基金当前热销产品） */
	GET_CURRENTPRODUCT("/Service/CLFDemo.svc/GetCurrentProduct"),
	/**基金单页（基金经理和公司代码） */
	GET_CODE("/Service/CLFDemo.svc/GetProductCode"),
	/**基金单页（基金公司信息） */
	GET_FUNDCOMPANY("/Service/CLFDemo.svc/GetFundCompany"),
	/**基金单页（当前热销产品） */
	GET_SELLINGPRODUCTS("/Service/CLFDemo.svc/GetSellingProducts"),
	/**基金单页（单只产品信息） */
	GET_PRODUCTDETAIL("/Service/CLFDemo.svc/GetProductDetail"),
	/**基金单页（单只产品信息） */
	GET_APPOINTMENTFUNDINFO("/Service/MyfundDemo.svc/GetAppointmentFundInfoNew"),
	/**基金代消系统银行卡信息查询*/
	GET_BANKCARDMESSAGE("/appweb/ws/webapp-clientspace/getAccountBankList"),
	/**基金代消系统银行卡信息查询NEW*/
	GET_BANKCARDMESSAGENEW("/appwebnew/ws/webapp-clientspace/getAccountBankList"),
	/**获取个人信息 */
	GET_ACCOUNTINFO("/appweb/ws/webapp-clientspace/getAccountInfoForOne"),
	/**保存验证信息 */
	GET_SAVENAMETESTINFO("/Service/CLFDemo.svc/GetRealnameauthenticationInfo"),
	
	/**基金配资*/
	FUNDSWITH_CAPITAL("/data-platform/webservice/web/getAdoptFinanceInfo"),
	/**管家通*/
	GET_BUTLERTONG("/appweb/ws/webapp-clientspace/getFund"),
	/**判断管家通会员*/
	GET_TODETERMINETHEMEBEER("/Service/CLFDemo.svc/CheckIsSteward"),
	/**管家通会员信息*/
	GET_THEHOUSEKEEPERMEMBERSHIP("/Service/MyfundDemo.svc/GetStewardInfo"),
	/**判断基金配资*/
	GET_JUDGEFNDS("/Service/CLFDemo.svc/CheckIsFinancing"),
	/**判断基金配资2*/
	GET_JUDGEFNDSTWO("/Service/MyfundDemo.svc/CheckIsFinancing2_0"),
	/**管家通预约*/
	GET_HOUSEKEEPERFLUX("/Service/CLFDemo.svc/OrderFinancing"),
	/**管家通预约2*/
	GET_HOUSEKEEPERFLUXTWO("/Service/MyfundDemo.svc/OrderFinancingNew"),
	/**基金配资预约*/
	GET_ANAPPOINTMENT("/Service/CLFDemo.svc/OrderSteward"),
	/**基金配资预约2*/
	GET_ANAPPOINTMENTTWO("/Service/MyfundDemo.svc/OrderStewardNew"),
	/**添加一条配资*/
	GET_ADDFUTURES("/data-platform/webservice/web/setFinanceInfo"),
	/**配资支付*/
	GET_ANDPAY("/yeeappay/paypre.aspx"),
	/**支付方式*/
	GET_PAYWAY("/Service/MyfundDemo.svc/UpdatePaymentMethod"),
	/**判断会员是否支付成功*/
	GET_WHETHERPAYMENT("/Service/MyfundDemo.svc/WhetherToPay"),
	
	/**修改客户信息*/
	GET_CLIENT_MESSAGE("/appweb/ws/webapp-cxf/modifyAccountInfoAndroid"),
	/**查询持有基金*/
	GET_INQUIRY_FUND("/appweb/ws/webapp-cxf/queryAssets"),
	/**查询持有基金2*/
	GET_INQUIRY_FUNDTWO("/appwebnew/ws/webapp-cxf/queryAssetsnew"),
	/**交易密码修改*/
	GET_CHANGE_PASSWORD("/appweb/ws/webapp-cxf/modifyPassword"),
	/**设置基金方式*/
	GET_SET_FUND("/appweb/ws/webapp-cxf/setDefdividendMethod"),
	/**设置基金方式2*/
	GET_SET_FUNDTWO("/appwebnew/ws/webapp-cxf/setDefdividendMethodnew"),
	/**支付接口*/
	GET_PAY("/paymobile/api/pay/request"),
	/**优惠券*/
	GET_DISCUNTCOUPON("/Service/MyfundDemo.svc/CouponsState"),
	/**抡券列表*/
	GET_LUNSTAMPS("/Service/MyfundDemo.svc/GetQuanList"),
	/**点财通支付方式*/
	GET_SOMEMONEYWAY("/Service/CLFDemo.svc/OpenDianCaiTong"),
	/**分享链接加50元**/
	GET_SHARELINKS("/Service/MyfundDemo.svc/ShareLinks"),
	/**测试banner**/
	GET_MYFUNDBANNER("/Service/CLFDemo.svc/GetzhjjBanner"),
	/**使用优惠券**/
	GET_UPDATECOUPONS("/Service/MyfundDemo.svc/UpdateCoupons"),
	/**查询手机号是否存在**/
	GET_FINDMOBILENO("/appwebnew/ws/webapp-cxf/findMobileno"),
	/**银行汇款方式*/
	GET_BANKTRANSFER("/Service/MyfundDemo.svc/OpenDianCaiTong"),
	/**银行汇款方式2*/
	GET_BANKTRANSFERTWO("/Service/MyfundDemo.svc/OpenDianCaiTong2"),
	/**银行汇款方式3*/
	GET_BANKTRANSFERTWOA("/Service/MyfundDemo.svc/OpenDianCaiTong3"),
	/**会员卡支付*/
	GET_MEMDERSHIPPAY("/Service/MyfundDemo.svc/OpenDianCaiTongbycard"),
	/**判断是否为点财通会员*/
	GET_JUDGEDOTGEIN("/Service/MyfundDemo.svc/huiyuanpanduan"),
	/**单点登录注册接口*/
	GET_POINTREGISTER("/Service/MyfundDemo.svc/GetAPPUserRegist"),
	/**单点登录注册接口2*/
	GET_POINTREGISTERTWO("/Service/MyfundDemo.svc/GetAPPUserRegist2"),
	/**验证登录*/
	GET_STEPVERIFICATION("/appwebnew/ws/webapp-cxf/userlogin"),
	/**更新CMS信息*/
	GET_UPDATEMESSAGE("/Service/MyfundDemo.svc/UpdateCMS"),
	/**身份证号查手机号*/
	GET_IDCRADAUDITUSANM("/Service/MyfundDemo.svc/IDCardToUserInfo"),

	/**推荐原生*/
	GET_HOTFUNDLIST2("/Service/MyfundDemo.svc/GetHotfundList2"),
	/**推荐列表*/
	GET_TUIJIANBANNER("/Service/MyfundDemo.svc/TuiJiangBanner"),
	/**开户更新到CRM*/
	GET_UPDATECRM("/Service/MyfundDemo.svc/UpdateCMS"),
	/**首页精选基金接口*/
	GET_INDEXFUNDLIST("/Service/MyfundDemo.svc/GetIndexFundList"),
	GET_INDEXFUNDLISTTWO("/Service/MyfundDemo.svc/GetIndexFundList"),
	/**开户卡信息验证*/
	GET_OPENCARD("/appwebnew/ws/webapp-cxf/identifySend"),
	/**短信验证码验证*/
	GET_SMSOPENCARD("/appwebnew/ws/webapp-cxf/verifySend"),
	/**客户空间判断是否注册*/
	GET_CLIENTELEJUDGE("/Service/MyfundDemo.svc/CheckCMSMobile"),
	/**特色固收列表HDL*/
	GET_FEATUREPRMHDL("/data-platform/webservice/web/TheCurrentProduct"),
	/**特色固收列表HYC*/
	GET_FEATUREPRMHYC("/data-platform/webservice/web/TheCurrentProduct"),
	/**固收产品销量*/
	GET_SALESPRODUCT("/Service/MyfundDemo.svc/GetFixedSaled"),
	/**固收线下支付*/
	GET_OFFLINERECIVE("/Service/MyfundDemo.svc/BankHuikuanPayHdlHyc"),
	;

	private String opt;
	private Class<? extends ResponseResult> clazz;
	private RequestMethod requestMethod = RequestMethod.GET;
	private int retryNumber = 1;

	/**
	 * 设置请求类型
	 * @param method
	 * @return
	 */
	public ApiType setMethod(RequestMethod method){
		requestMethod = method;
		return this;
	}
	
	private ApiType(String opt) {
		this.opt = opt;
	}

	private ApiType(String opt, RequestMethod requestMethod) {
		this.opt = opt;
		this.requestMethod = requestMethod;
	}

	private ApiType(String opt, RequestMethod requestMethod, int retryNumber) {
		this.opt = opt;
		this.requestMethod = requestMethod;
		this.retryNumber = retryNumber;
	}

	public String getOpt() {
		return opt;
	}
	
	public Class<? extends ResponseResult> getClazz(){
		return clazz;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public int getRetryNumber() {
		return retryNumber;
	}

	public enum RequestMethod {
		POST("POST"), GET("GET"),FILE("POST");
		private String requestMethodName;

		RequestMethod(String requestMethodName) {
			this.requestMethodName = requestMethodName;
		}

		public String getRequestMethodName() {
			return requestMethodName;
		}
	}
}
