package com.plmt.boommall.network.config;

/**
 * remote request url
 */
public class RequestUrl {

	public static final String NAMESPACE = "http://120.55.116.206";

	// http://120.55.116.206:8080/mapi
	public static final String HOST_URL = "http://120.55.116.206:8000/mapi";
	public static final String HOST_PAY_URL = "http://120.55.116.206:8000";
	//
	// public static final String HOST_URL = "http://appapi.wangpugo.com/mapi";
	//
	// public static final String HOST_PAY_URL = "http://appapi.wangpugo.com";

	public interface connect {
		/**
		 * 连接 获取推送
		 */
		public String connect = "/user/connect";

	}

	public interface search {
		/**
		 * 搜索
		 */
		public String normal = "/search/normal";

		/**
		 * 热搜
		 */
		public String getHotWords = "/search/getHotWords";

	}
	
	public interface notice {
		/**
		 * 降价通知
		 */
		public String priceReduce = "/pdp/alertPrice";

		/**
		 * 到货提醒
		 */
		public String aog = "/pdp/alertStock";
		
		/**
		 * 客户降价提醒列表接口
		 */
		public String getPriceReduce = "/user/getAlertPrice";
		
		/**
		 * 客户到货提醒列表接口
		 */
		public String getAog = "/user/getAlertStock";
		
		/**
		 * 客户关闭降价提醒接口
		 */
		public String closePriceReduce = "/user/closePrice";
		
		/**
		 * 客户关闭到货提醒列表接口
		 */
		public String closeAog = "/user/closeStock";
		

	}

	public interface promotion {

		/**
		 * 获取首页banner
		 */
		public String getBanners = "/flashSale/getBanners";

		/**
		 * 获取分类Rounds
		 */
		public String getRounds = "/flashSale/getRounds";
	}

	public interface account {

		/**
		 * 登陆
		 */
		public String login = "/account/login";

		/**
		 * 注册
		 */
		public String register = "/account/register";

		/**
		 * 忘记密码
		 */
		public String forgetPwd = "/account/forgetPWD";

		/**
		 * 获取用户信息
		 */
		public String getInfo = "/user/baseinfo";

		public String modifyPwd = "/account/forgetPWD";

		/**
		 * 验证码
		 */
		public String sendAuthCode = "/account/authCode";

		public String setReal = "/checkout/setReal";

		/**
		 * 退出
		 */
		public String logout = "/account/logout";

		/**
		 * 設置用戶頭像
		 */
		public String setUserPhoto = "/user/setUserPhoto";

	}

	public interface property {

		/**
		 * 积分查询
		 */
		public String queryIntegral = "/user/getRewardAmount";

		/**
		 * 余额查询
		 */
		public String queryRemainingMoney = "/user/getBalance";

		/**
		 * 旺卡查询
		 */
		public String queryGiftCard = "/user/giftCart";

		/**
		 * 旺卡充值到余额
		 */
		public String rechargeBalance = "/user/redeemGift";

		/**
		 * 余额支付接口
		 */
		public String balancePay = "/checkout/appsavePaymentData";

		/**
		 * 旺卡支付接口
		 */
		public String giftCardPay = "/checkout/saveGiftcard";

	}

	public interface cart {

		/**
		 * 购物车列表
		 */
		public String list = "/cart/list";

		/**
		 * 购物车添加
		 */
		public String add = "/cart/add";

		/**
		 * 购物车修改
		 */
		public String update = "/cart/update";

		/**
		 * 购物车删除
		 */
		public String del = "/cart/delete";

		/**
		 * 设置购物车选择
		 */
		public String setSelectItem = "/cart/selectItem";

	}

	public interface address {

		/**
		 * 地址数据
		 */
		public String data = "/address/getJsonArea";

		/**
		 * 地址列表
		 */
		public String list = "/address/list";

		/**
		 * 地址详细信息
		 */
		public String detail = "/address/view";

		/**
		 * 地址修改
		 */
		public String update = "/address/update";

		/**
		 * 地址删除
		 */
		public String del = "/address/delete";

		/**
		 * 设置地址
		 */
		public String setShippingAddress = "/checkout/setShippingAddress";
	}

	public interface collection {

		/**
		 * 收藏列表
		 */
		public String list = "/favorite/list";

		/**
		 * 添加收藏
		 */
		public String add = "/favorite/add";

		/**
		 * 删除收藏
		 */
		public String del = "/favorite/delete";

	}

	public interface comment {

		/**
		 * 评论列表
		 */
		public String list = "/pdp/listComment";
		
		public String add = "/pdp/postComment";
		
	}

	public interface goods {

		/**
		 * 根据商品种类查询商品
		 */
		public String queryGoodsByCategory = "/plp/getproducts";

		/**
		 * 根据商品种类查询商品
		 */
		public String queryGoodsByCategoryNew = "/plp/getProductsnew";

		/**
		 * 根据商品Id查询商品
		 */
		public String queryGoodsByID = "/pdp/getproductdetail";

		/**
		 * 查询一级分类
		 */
		public String queryTopCategory = "/category/getcategory";

		/**
		 * 一级分类获取二级分类
		 */
		public String querySubCategory = "/category/getsubCategory";

		/**
		 * 一级分类获取首页商品
		 */
		public String querySubCategoryHome = "/category/getHomeSubCategory";

		/**
		 * 获取首页分类商品
		 */
		public String queryHomeCategory = "/category/getHomeCategory";
		
		/**
		 * 获取筛选数据
		 */
		public String getFilter = "/plp/getFilter";

	}

	public interface order {

		public String queryOrderList = "/account_order/list";

		/**
		 * 提交订单
		 */
		public String submitOrder = "/checkout/submitOrder/";

		/**
		 * 获取订单支付信息
		 */
		public String getOrderPayInfo = "/appaliapy/payment/getOrder";

		/**
		 * 银联接口
		 */
		public String getOrderPayByUnionInfo = "/appunionpay/payment/getOrder";

		/**
		 * 订单信息
		 */
		public String cartDetail = "/checkout/cartDetail";

		/**
		 * 设置订单地址
		 */
		public String setShippingAddress = "/checkout/setShippingAddress";
	}

}
