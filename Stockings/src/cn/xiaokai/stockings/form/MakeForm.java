package cn.xiaokai.stockings.form;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.puss.Briefs;
import cn.xiaokai.stockings.puss.Buttock;
import cn.xiaokai.stockings.puss.FormID;
import cn.xiaokai.stockings.puss.Message;
import cn.xiaokai.stockings.py.PY;
import cn.xiaokai.tool.Tool;
import cn.xiaokai.ui.CustomForm;
import cn.xiaokai.ui.SimpleForm;
import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MakeForm {
	private Stockings mis;
	private Message Msg;

	public MakeForm(Stockings mis) {
		this.mis = mis;
		Msg = mis.Msg;
	}

	public boolean Shop(Player player, FormResponseSimple data) {
		Briefs briefs = mis.PlayerOfBriefs.get(player.getName().toLowerCase());
		List<Map<String, Object>> list = briefs.ItemList;
		String Sign = list.get(data.getClickedButtonId()).get("Sign").toString();
		int Money = Float.valueOf(list.get(data.getClickedButtonId()).get("Money").toString()).intValue();
		if (Money > EconomyAPI.getInstance().myMoney(player))
			return Tip(player, Msg.getSun("界面", "称号商店", "内容提示", new String[] { "{Player}", "{Sign}" },
					new Object[] { player.getName(), Sign }));
		if (PY.PlayerHaveSign(player, Sign))
			return Tip(player, Msg.getSun("界面", "称号商店", "已购买的称号", new String[] { "{Player}", "{Sign}" },
					new Object[] { player.getName(), Sign }));
		EconomyAPI.getInstance().reduceMoney(player, Money);
		if (PY.addAndSetPlayerSign(player, Sign))
			return Tip(player, Msg.getSun("界面", "称号商店", "购买成功", new String[] { "{Player}", "{Sign}", "{Money}" },
					new Object[] { player.getName(), Sign, Money }), true);
		return Tip(player, Msg.getSun("界面", "称号商店", "购买异常", new String[] { "{Player}", "{Sign}", "{Money}" },
				new Object[] { player.getName(), Sign, Money }), true);
	}

	public boolean MySign(Player player) {
		List<String> list = PY.getPlayerSigns(player);
		if (list.size() < 1)
			return MakeForm.Tip(player, Msg.getSun("界面", "个人称号", "无称号"));
		SimpleForm form = new SimpleForm(FormID.MySignList, Msg.getSun("界面", "个人称号", "标题"),
				Msg.getSun("界面", "个人称号", "内容提示"));
		for (String string : list)
			form.addButton(string);
		form.sendPlayer(player);
		return true;
	}

	public boolean Shop(Player player) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Config config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
		if (config.getAll().size() < 1)
			return MakeForm.Tip(player, Msg.getSun("界面", "称号商店", "商店为空"));
		SimpleForm form = new SimpleForm(FormID.ShopMain, Msg.getSun("界面", "称号商店", "标题"),
				Msg.getSun("界面", "称号商店", "内容提示"));
		for (String ike : config.getAll().keySet()) {
			HashMap<String, Object> map = (HashMap<String, Object>) config.get(ike);
			list.add(map);
			form.addButton(map.get("Sign") + "§f|§e" + map.get("Money"));
		}
		Briefs briefs = mis.PlayerOfBriefs.get(player.getName().toLowerCase());
		briefs.ItemList = list;
		mis.PlayerOfBriefs.put(player.getName().toLowerCase(), briefs);
		form.sendPlayer(player);
		return true;
	}

	public boolean Main(Player player) {
		List<String> list = new ArrayList<String>();
		SimpleForm form = new SimpleForm(FormID.MainID, Msg.getSun("界面", "主页", "标题"), Msg.getSun("界面", "主页", "内容提示"));
		form.addButton(Msg.getSun("界面", "主页", "购买称号"));
		list.add("G");
		form.addButton(Msg.getSun("界面", "主页", "我的称号"));
		list.add("M");
		if (player.isOp()) {
			form.addButton("配置管理");
			list.add("P");
		}
		Briefs briefs = mis.PlayerOfBriefs.get(player.getName().toLowerCase());
		briefs.FormListKey = list;
		mis.PlayerOfBriefs.put(player.getName().toLowerCase(), briefs);
		form.sendPlayer(player);
		return true;
	}

	public static boolean Tip(Player player, String Content, boolean callback) {
		return Tip(player, Stockings.mis.getName(), Content, "确定", callback);
	}

	public static boolean Tip(Player player, String Content) {
		return Tip(player, Stockings.mis.getName(), Content, "确定", false);
	}

	public static boolean Tip(Player player, String Title, String Content, String Button, boolean callback) {
		SimpleForm form = new SimpleForm(Tool.getRand(-222222222, 22222222), Title, Content);
		form.addButton(Button);
		form.sendPlayer(player);
		return callback;
	}

	public boolean Setting(Player player) {
		SimpleForm form = new SimpleForm(FormID.SettingMain, Tool.getColorFont(mis.getName() + "-Setting"));
		form.addButton(Tool.getColorFont("上架称号到商店"));
		form.addButton(Tool.getColorFont("下架称号"));
		form.addButton(Tool.getColorFont("系统设置"));
		form.sendPlayer(player);
		return true;
	}

	public void Config(Player player) {
		CustomForm form = new CustomForm(FormID.Setting, Tool.getColorFont(mis.getName() + "-Setting"));
		form.addToggle("检测更新", mis.config.getBoolean("检测更新"));
		form.addInput("货币单位", mis.config.getString("货币单位"));
		form.addToggle("潜行开关", mis.config.getBoolean("潜行开关"));
		form.addInput("潜行概率\n值越大触发概率越低，为零时百分百触发", mis.config.getString("潜行概率"));
		form.addToggle("死亡提示", mis.config.getBoolean("死亡提示"));
		form.addInput("进服默认称号", mis.config.getString("进服默认称号"));
		form.addToggle("聊天管控", mis.config.getBoolean("聊天管控"));
		form.addToggle("监控违禁词", mis.config.getBoolean("监控违禁词"));
		form.sendPlayer(player);
	}
}
