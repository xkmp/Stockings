package cn.xiaokai.stockings.prettylegs;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.py.PY;
import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
public class BiBi implements Listener {
	private Stockings mis;

	public BiBi(Stockings stockings) {
		mis = stockings;
	}

	@EventHandler
	public void onBiBi(PlayerChatEvent e) {
		if (!mis.config.getBoolean("聊天管控"))
			return;
		Player player = e.getPlayer();
		Config config;
		try {
			config = mis.PlayerOfBriefs.get(player.getName().toLowerCase()).config;
		} catch (Exception e2) {
			mis.getLogger()
					.error("§4无法从系统获取玩家§6" + player.getName() + "§4的数据！可能为插件BUG！如果条件允许请将当时发生的事情描述给我！我将竭尽全力避免BUG的发生");
			config = PY.getPlayerConfig(player);
			if (config == null) {
				mis.getLogger().error("§4无法以已知方法获取玩家§6" + player.getName() + "§4的数据！跳过本次处理");
				return;
			}
		}
		if (config.getBoolean("Ban")) {
			player.sendMessage(mis.Msg.getMessage("被禁言提示"));
			e.setCancelled();
			return;
		} /**
			 * #{World} 玩家所在的世界名、 {Sign}玩家正在佩戴的称号、 {Player}玩家名、 {Msg}玩家说的话、 {Money}忘记的余额、
			 * {Level}玩家的mc经验等级、 {Exp}玩家的经验数、 {Food}玩家的饱食度、 {OP}玩家是否是OP，如果是，将显示管理员，否则显示玩家
			 */
		String BiBi = e.getMessage();
		if (mis.config.getBoolean("监控违禁词")) {
			String SBBiBi = mis.Msg.getMessage("违禁替换");
			List<String> list = mis.Msg.Message.getStringList("违禁词");
			for (String SBs : list)
				if (BiBi.contains(SBs))
					BiBi = BiBi.replace(SBs, SBBiBi);
		}
		Plugin economy = mis.getServer().getPluginManager().getPlugin("EconomyAPI");
		int Money = 0;
		if (economy != null && economy.isEnabled())
			Money = (int) EconomyAPI.getInstance().myMoney(player);
		else
			mis.getLogger().info("§4警告！插件依赖的EconomyAPI插件可能未安装或未启用！将无法获取玩家金币数量！");
		String msg = mis.Msg.getMessage("玩家消息",
				new String[] { "{World}", "{Sign}", "{Player}", "{Msg}", "{Money}", "{Level}", "{Exp}", "{Food}",
						"{OP}", "{X}", "{Y}", "{Z}", "{Health}" },
				new Object[] { player.getLevel().getFolderName(),
						PY.getPlayerSign(player) == null ? "无称号" : PY.getPlayerSign(player), player.getName(), BiBi,
						Money, player.getExperienceLevel(), player.getExperience(), player.getFoodData().getLevel(),
						player.isOp() ? "OP" : "玩家", (int) player.getX(), (int) player.getY(), (int) player.getZ(),
						(int) player.getHealth() });
		e.setCancelled();
		Server.getInstance().broadcastMessage(msg);
	}
}
