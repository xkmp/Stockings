package cn.xiaokai.stockings.prettylegs;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerToggleSneakEvent;
import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.puss.Briefs;
import cn.xiaokai.stockings.puss.Buttock;
import cn.xiaokai.stockings.puss.CheckLegality;
import cn.xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class Nipple implements Listener {
	private Stockings mis;

	public Nipple(Stockings mis) {
		this.mis = mis;
	}

	@EventHandler
	public void eatTooMuch(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		File file = new File(mis.getDataFolder() + Buttock.PlayerPath, player.getName().toLowerCase() + ".yml");
		Config config;
		if (!file.exists()) {
			mis.getLogger().info("§6未找到玩家§4" + player.getName().toLowerCase() + "§6的数据，正在创建....");
			config = new Config(file, Config.YAML);
			LinkedHashMap<String, Object> map = Buttock.getPlayerMap();
			map.put("Name", player.getName());
			map.put("Sign", config.getString("进服默认称号"));
			ArrayList<String> list = new ArrayList<String>();
			if (config.getString("进服默认称号") != null && !config.getString("进服默认称号").isEmpty())
				list.add(config.getString("进服默认称号"));
			map.put("Signs", list);
			config.setAll(Buttock.getPlayerMap());
			config.save();
		}
		config = new Config(file, Config.YAML);
		config.set("Name", player.getName());
		CheckLegality legality = new CheckLegality(mis);
		mis.getLogger().info("§6玩家§4" + player.getName().toLowerCase() + "§6加入服务器....正在检查数据合法性....");
		legality.isMap(Buttock.getPlayerMap(), config.getAll(), config);
		mis.PlayerOfBriefs.put(player.getName().toLowerCase(), new Briefs(player, config));
	}

	@EventHandler
	public void Brainless(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (mis.PlayerOfBriefs.containsKey(player.getName()))
			mis.PlayerOfBriefs.remove(player.getName());
	}

	@EventHandler
	public void onSB(PlayerDeathEvent e) {
		if (!mis.config.getBoolean("死亡提示") || !e.getEntity().isPlayer())
			return;
		List<String> list = mis.Msg.Message.getStringList("死亡");
		e.setDeathMessage(mis.Msg.getText(list.get(Tool.getRand(0, list.size() - 1)), new String[] { "{Player}" },
				new Object[] { e.getEntity().getName() }));
	}

	@EventHandler
	public void onPy(PlayerToggleSneakEvent e) {
		if (!mis.config.getBoolean("潜行开关") || Tool.getRand(0,
				Tool.isInteger(String.valueOf(mis.config.get("潜行概率")))
						? Float.valueOf(String.valueOf(mis.config.get("潜行概率"))).intValue()
						: 10) != 0)
			return;
		List<String> list = mis.Msg.Message.getStringList("潜行");
		if (list.size() < 1)
			return;
		mis.getServer().broadcastMessage(mis.Msg.getText(list.get(Tool.getRand(0, list.size() - 1)),
				new String[] { "{Player}" }, new Object[] { e.getPlayer().getName() }));
	}
}
