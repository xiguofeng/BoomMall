package com.plmt.boommall.network.config;

/**
 * remote request url
 */
public class RequestUrl {

	public static final String NAMESPACE = "http://139.196.15.154";

	public static final String HOST_URL = "http://120.55.116.206:8080/mapi/plp";
	
	

	public interface connect {
		/**
		 * 连接 获取推送
		 */
		public String connect = "/user/connect";

	}

	public interface account {

		/**
		 * 登陆
		 */
		public String login = "loginDelivery";

		public String modifyPwd = "changePwd";
		
		/**
		 * 验证码
		 */
		public String sendAuthCode = "sendAuthCode";


	}

	public interface goods {

		/**
		 * 查询全部商品
		 */
		public String queryAllGoods = "queryAllProduct";

		/**
		 * 根据商品种类查询商品
		 */
		public String queryGoodsByCategory = "/getproducts";
		
		/**
		 * 根据商品种类查询商品
		 */
		public String queryGoodsByCategoryNew = "/getproductsnew";


		public String queryGoodsCategory = "queryProductCategory";

		/**
		 * 查询商品（关键字）
		 */
		public String queryGoodsByKey = "/goods/queryGoodsByKeyword";

	}

	public interface order {

		public String queryOrderForGrab = "queryOrderForGrab";

		public String grabOrder = "grab";

		/**
		 * 获取抢单历史(已抢订单)
		 */
		public String queryOrderOfDelivery = "queryOrderOfDelivery";

		/**
		 * 订单确认
		 */
		public String recieveConfirm = "recieveConfirm";

	}

}
