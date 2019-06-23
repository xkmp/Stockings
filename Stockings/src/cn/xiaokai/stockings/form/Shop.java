package cn.xiaokai.stockings.form;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.puss.Briefs;
import cn.xiaokai.stockings.puss.Buttock;
import cn.xiaokai.stockings.puss.FormID;
import cn.xiaokai.tool.Tool;
import cn.xiaokai.ui.CustomForm;
import cn.xiaokai.ui.SimpleForm;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Shop {
	private Player player;
	private Stockings mis;

	public Shop(Player player) {
		this.player = player;
		mis = Stockings.mis;
	}

	public boolean AddSign(FormResponseCustom data) {
		String Sign = (String) data.getResponse(0);
		if (Sign == null || Sign.isEmpty())
			return MakeForm.Tip(player, "§4请输入你想要出售的称号！");
		String moneyString = (String) data.getResponse(1);
		if (moneyString == null || moneyString.isEmpty())
			return MakeForm.Tip(player, "§4请输入你想要设置的称号价格！");
		if (!Tool.isInteger(moneyString))
			return MakeForm.Tip(player, "§4称号价格只能为大于零的纯整数");
		int Money = Float.valueOf(moneyString).intValue();
		if (Money < 1)
			return MakeForm.Tip(player, "§4称号价格只能为大于零的纯整数");
		String Key = getKey(1);
		Config config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Money", Money);
		map.put("Sign", Sign);
		map.put("Data", Tool.getDate() + " " + Tool.getTime());
		map.put("Player", player.getName());
		config.set(Key, map);
		config.save();
		return MakeForm.Tip(player, "§6上架成功！Key为：" + Key, true);
	}

	public boolean DelSign(FormResponseSimple data) {
		Briefs briefs = mis.PlayerOfBriefs.get(player.getName().toLowerCase());
		List<String> list = briefs.FormListKey;
		String Key = list
				.get(data.getClickedButtonId() > list.size() - 1 ? list.size() - 1 : data.getClickedButtonId());
		Config config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
		config.remove(Key);
		config.save();
		MakeForm.Tip(player, "§6删除成功！");
		return true;
	}

	public boolean DelSign() {
		List<String> list = new ArrayList<String>();
		Config config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
		if (config.getAll().size() < 1)
			return MakeForm.Tip(player, mis.Msg.getSun("界面", "称号商店", "商店为空"));
		SimpleForm form = new SimpleForm(FormID.SignDelItem, Tool.getColorFont(mis.getName() + "-下架称号"));
		for (String ike : config.getKeys()) {
			HashMap<String, Object> map = (HashMap<String, Object>) config.get(ike);
			list.add(ike);
			form.addButton(map.get("Sign") + "§f|§e" + map.get("Money"));
		}
		Briefs briefs = mis.PlayerOfBriefs.get(player.getName().toLowerCase());
		briefs.FormListKey = list;
		mis.PlayerOfBriefs.put(player.getName().toLowerCase(), briefs);
		form.sendPlayer(player);
		return true;
	}

	public boolean AddSign() {
		CustomForm form = new CustomForm(FormID.SignAddItem, Tool.getColorFont(mis.getName() + "-上架称号"));
		form.addInput("请输入想要上架的称号");
		form.addInput("请输入称号的价格");
		form.sendPlayer(player);
		return true;
	}

	public String getKey(int JJLength) {
		String Key = "";
		for (int JJSize = 0; JJLength > JJSize; JJSize++)
			Key += Tool.getRandString();
		Config config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
		if (config.exists(Key))
			return getKey(JJLength++);
		return Key;
	}
}
