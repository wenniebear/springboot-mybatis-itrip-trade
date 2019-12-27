package com.boot.util;
/**
 * SystemConfig
 * @author hanlu
 *
 */
public class SystemConfig {

	/**
	 * 文件上传路径，通过properties文件进行配置
	 */
	private String fileUploadPathString;
	/**
	 * 上传文件访问URL，通过properties文件进行配置
	 */
	private String visitImgUrlString;
	/**
	 * 生成订单的机器码，通过properties文件进行配置
	 */
	public static String machineCode="D1000001";

	public static String orderProcessOK=" {\"1\":\"\\u8BA2\\u5355\\u63D0\\u4EA4\",\"2\":\"\\u8BA2\\u5355\\u652F\\u4ED8\",\"3\":\"\\u652F\\u4ED8\\u6210\\u529F\",\"4\":\"\\u5165\\u4F4F\",\"5\":\"\\u8BA2\\u5355\\u70B9\\u8BC4\",\"6\":\"\\u8BA2\\u5355\\u5B8C\\u6210}";

    public static String orderProcessCancel="{\"1\":\"\\u8BA2\\u5355\\u63D0\\u4EA4\",\"2\":\"\\u8BA2\\u5355\\u652F\\u4ED8\",\"3\":\"\\u8BA2\\u5355\\u53D6\\u6D88\"}";


	
	/**
	 * 在线支付交易完成通知后续处理接口的地址
	 */
	private String tradeEndsUrl;	

	/**
	 * 支付模块发送Get请求是否使用代理
	 */
	private Boolean tradeUseProxy;
	/**
	 * 代理主机
	 */
	private String tradeProxyHost;
	/**
	 * 代理端口
	 */
	private Integer tradeProxyPort;


	
	
}
