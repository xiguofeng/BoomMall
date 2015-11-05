package com.plmt.boommall.network.config;

/**
 * remote result
 */
public class MsgResult {

	/**
	 * 网络请求返回标识
	 */
	public static final String RESULT_TAG = "result";

	/**
	 * 请求成功标识
	 */
	public static final String RESULT_SUCCESS = "0";

	/**
	 * 请求失败标识
	 */
	public static final String RESULT_FAIL = "-1";

	/**
	 * 请求失败：session超时
	 */
	public static final String RESULT_SESSION_TIMEOUT = "-2";

	/**
	 * msg
	 */
	public static final String RESULT_MSG_TAG = "message";

	/**
	 * data
	 */
	public static final String RESULT_DATA_TAG = "data";

	/**
	 * list
	 */
	public static final String RESULT_LIST_TAG = "list";

	/**
	 * address
	 */
	public static final String RESULT_PUSH_ADDRESS_TAG = "pushAddress";

	/**
	 * resultType
	 */
	public static final String RESULT_TYPE_TAG = "resultType";

	/**
	 * softDownloadAddress
	 */
	public static final String RESULT_SOFTDOWNLOADADDRESS_TAG = "softDownloadAddress";

	/**
	 * errorMessage
	 */
	public static final String RESULT_ERROR_MSG_TAG = "errorMessage";

	public static final String B2A_RESULT_SUCCESS = "0";

	public static final String B2A_RESULT_NOT_BIND = "1";

	public static final String B2A_RESULT_SUCCESS_TAG = "STATE";

	public static final String B2A_RESULT_DATA_TAG = "DATAS";

	public static final String ORDER_TAG = "OrderOld";

}
