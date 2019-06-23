package cn.xiaokai.stockings.puss;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.xiaokai.stockings.Stockings;

/**
 * @author Winfxk
 */
public class Buttock {
	public static final String ShopName = "Shop.yml";
	/**
	 * 文字数据文件名
	 */
	public static final String MsgName = "SexualMoans.yml";
	/**
	 * 系统配置文件名
	 */
	public static final String ConfigName = "Config.yml";
	/**
	 * 玩家数据存储路径
	 */
	public static final String PlayerPath = "/Brassiere/";
	/**
	 * 要检查内容的文件名
	 */
	public static final String[] ExamineConfigNameList = { ConfigName, MsgName };
	/**
	 * 要检查创建的文件夹列表
	 */
	public static final String[] Mkdir = { PlayerPath };

	/**
	 * 初始化玩家默认配置
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, Object> getPlayerMap() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		ArrayList<String> list = new ArrayList<String>();
		String Sign = Stockings.mis.config.get("进服默认称号") == null
				|| Stockings.mis.config.get("进服默认称号").toString().isEmpty() ? null
						: Stockings.mis.config.getString("进服默认称号");
		if (Sign != null && !Sign.isEmpty())
			list.add(Sign);
		map.put("Name", "");
		map.put("Sign", Sign == null || Sign.isEmpty() ? "" : Sign);
		map.put("Signs", list);
		map.put("Ban", false);
		return map;
	}
}
