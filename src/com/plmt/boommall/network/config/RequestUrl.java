package com.plmt.boommall.network.config;

/**
 * remote request url
 */
public class RequestUrl {

	public static final String NAMESPACE = "http://139.196.15.154";

	// http://120.55.116.206:8080/mapi
	public static final String HOST_URL = "http://120.55.116.206:8080/mapi";

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
		public String login = "/account/login";

		/**
		 * 获取用户信息
		 */
		public String getInfo = "/user/baseinfo";

		public String modifyPwd = "changePwd";

		/**
		 * 验证码
		 */
		public String sendAuthCode = "sendAuthCode";

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

	}

	public interface address {

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

	}
	
	

	public interface goods {

		/**
		 * 根据商品种类查询商品
		 */
		public String queryGoodsByCategory = "/plp/getproducts";

		/**
		 * 根据商品种类查询商品
		 */
		public String queryGoodsByCategoryNew = "/plp/getproductsnew";

		/**
		 * 根据商品Id查询商品
		 */
		public String queryGoodsByID = "/pdp/getproductdetail";

		/**
		 * 查询一级分类
		 */
		public String queryTopCategory = "/category/getcategory";

		/**
		 * 一级分类获取二级分类和一级分类的banner接口
		 */
		public String querySubCategory = "/category/getsubCategory";

	}

	public interface order {

		public String queryOrderList = "/account_order/list";

	}

}
