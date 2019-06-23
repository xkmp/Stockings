package cn.xiaokai.stockings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.form.MakeForm;
import cn.xiaokai.stockings.puss.Buttock;
import cn.xiaokai.stockings.puss.Message;
import cn.xiaokai.stockings.py.PY;
import cn.xiaokai.tool.Tool;
import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MyCmd {
	private Stockings mis;
	private Message Msg;

	public MyCmd(Stockings mis) {
		this.mis = mis;
		this.Msg = mis.Msg;
	}

	public boolean AdminCommand(CommandSender player, String label, String[] k) {
		if (k.length < 1) {
			if (player.isPlayer())
				return (new MakeForm(mis)).Setting((Player) player);
			return false;
		}
		Map<UUID, Player> uMap;
		String Name;
		switch (k[0].toLowerCase()) {
		case "sa":
		case "addandset":
		case "设置并给与":
			if (k.length < 3) {
				player.sendMessage(Msg.getSun("命令", "设置称号", "无参数"));
				return false;
			}
			Name = k[1].toLowerCase();
			if (!PY.isPlayerConfig(Name)) {
				player.sendMessage("§4该玩家可能还未加入过服务器！");
				return true;
			}
			if (PY.addAndSetPlayerSign(Name, k[2])) {
				player.sendMessage("§6设置成功！");
			} else
				player.sendMessage("§5设置成功！但可能会遇到问题");
			uMap = Server.getInstance().getOnlinePlayers();
			for (UUID ike : uMap.keySet()) {
				Player player1 = uMap.get(ike);
				if (player1.getName().toLowerCase().equals(Name)) {
					player1.sendMessage(Msg.getSun("命令", "设置称号", "管理员给与并设置玩家称号", new String[] { "{Admin}", "{Sign}" },
							new Object[] { player.getName(), k[2] }));
					break;
				}
			}
			return true;
		case "add":
		case "a":
		case "添加":
			if (k.length < 3) {
				player.sendMessage(Msg.getSun("命令", "设置称号", "无参数"));
				return false;
			}
			Name = k[1].toLowerCase();
			if (!PY.isPlayerConfig(Name)) {
				player.sendMessage("§4该玩家可能还未加入过服务器！");
				return true;
			}
			if (PY.addPlayerSign(Name, k[2])) {
				player.sendMessage("§6设置成功！");
			} else
				player.sendMessage("§5设置成功！但可能会遇到问题");
			uMap = Server.getInstance().getOnlinePlayers();
			for (UUID ike : uMap.keySet()) {
				Player player1 = uMap.get(ike);
				if (player1.getName().toLowerCase().equals(Name)) {
					player1.sendMessage(Msg.getSun("命令", "设置称号", "管理员给与玩家称号", new String[] { "{Admin}", "{Sign}" },
							new Object[] { player.getName(), k[2] }));
					break;
				}
			}
			return true;
		case "set":
		case "setting":
		case "设置":
			if (k.length < 3) {
				player.sendMessage(Msg.getSun("命令", "设置称号", "无参数"));
				return false;
			}
			Name = k[1].toLowerCase();
			if (!PY.isPlayerConfig(Name)) {
				player.sendMessage("§4该玩家可能还未加入过服务器！");
				return true;
			}
			if (PY.setPlayerSign(Name, k[2])) {
				player.sendMessage("§6设置成功！");
			} else
				player.sendMessage("§5设置成功！但可能会遇到问题");
			uMap = Server.getInstance().getOnlinePlayers();
			for (UUID ike : uMap.keySet()) {
				Player player1 = uMap.get(ike);
				if (player1.getName().toLowerCase().equals(Name)) {
					player1.sendMessage(Msg.getSun("命令", "设置称号", "管理员设置玩家称号", new String[] { "{Admin}", "{Sign}" },
							new Object[] { player.getName(), k[2] }));
					break;
				}
			}
			return true;
		case "help":
		case "h":
		case "帮助":
			player.sendMessage(
					"/sadmk help:查看命令帮助\n/sadmk set <玩家名> <称号>: 设置玩家的称号，但不给与玩家\n/sadmk add <玩家名> <称号>: 给玩家一个称号，但不设置\n/sadmk sa <玩家名> <称号>: 给玩家一个称号并且设置为正在使用的称号\n§4/sk help: 查看玩家命令帮助");
			return true;
		default:
			return false;
		}
	}

	public boolean PlayerCommand(CommandSender player, String label, String[] k) {
		List<String> list;
		String Sign;
		Config config;
		if (k.length < 1) {
			if (player.isPlayer())
				return (new MakeForm(mis)).Main((Player) player);
			return false;
		}
		switch (k[0].toLowerCase()) {
		case "购买":
		case "by":
		case "buy":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return false;
			}
			if (k.length < 2) {
				player.sendMessage(Msg.getSun("命令", "购买称号", "无参数"));
				return false;
			}
			config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
			if (config.getAll().size() < 1) {
				player.sendMessage(Msg.getSun("界面", "称号商店", "商店为空"));
				return true;
			}
			ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
			for (String ike : config.getAll().keySet())
				items.add((HashMap<String, Object>) config.get(ike));
			if (!Tool.isInteger(k[1])) {
				player.sendMessage(Msg.getSun("命令", "购买称号", "未知序号"));
				return true;
			}
			int ins = Float.valueOf(k[1]).intValue();
			if (ins < 0 || ins >= items.size()) {
				player.sendMessage(Msg.getSun("命令", "购买称号", "未知序号"));
				return true;
			}
			int Money = Float.valueOf(items.get(ins).get("Money").toString()).intValue();
			if (Money > EconomyAPI.getInstance().myMoney(player.getName())) {
				player.sendMessage(Msg.getSun("命令", "购买称号", "金币不足"));
				return true;
			}
			Sign = items.get(ins).get("Sign").toString();
			if (PY.getPlayerSigns(player.getName()).contains(Sign)) {
				player.sendMessage(Msg.getSun("命令", "购买称号", "已购买的称号"));
				return true;
			}
			EconomyAPI.getInstance().reduceMoney(player.getName(), Money);
			if (PY.addAndSetPlayerSign(player.getName(), Sign)) {
				player.sendMessage(Msg.getSun("命令", "购买称号", "购买成功"));
			} else
				player.sendMessage(Msg.getSun("命令", "购买称号", "购买异常"));
			return true;
		case "shop":
		case "商店":
			config = new Config(new File(mis.getDataFolder(), Buttock.ShopName), Config.YAML);
			if (config.getAll().size() < 1) {
				player.sendMessage(Msg.getSun("界面", "称号商店", "商店为空"));
				return true;
			}
			int in = 0;
			player.sendMessage(Tool.getColorFont("===========[商店列表]==========="));
			for (String ike : config.getAll().keySet()) {
				HashMap<String, Object> map = (HashMap<String, Object>) config.get(ike);
				player.sendMessage("§6" + in + " :§f" + map.get("Sign") + " §9售价:" + map.get("Money"));
				in++;
			}
			if (player.isPlayer())
				player.sendMessage("§6您可以使用‘/sk by <称号序号>’来购买称号！");
			return true;
		case "set":
		case "setting":
		case "设置":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return false;
			}
			if (k.length < 2) {
				player.sendMessage(Msg.getSun("命令", "设置称号", "无参数"));
				return false;
			}
			list = PY.getPlayerSigns(player.getName());
			if (list.contains(k[1])) {
				Sign = k[1];
			} else if (Tool.isInteger(k[1])) {
				int i = Float.valueOf(k[1]).intValue();
				if (i >= list.size()) {
					player.sendMessage(Msg.getSun("界面", "个人称号", "序号过大"));
					return true;
				}
				if (i < 0) {
					player.sendMessage(Msg.getSun("界面", "个人称号", "未知序号"));
					return true;
				}
				Sign = list.get(i);
			} else {
				player.sendMessage(Msg.getSun("界面", "个人称号", "未知序号"));
				return true;
			}
			if (PY.setPlayerSign(player.getName(), Sign)) {
				player.sendMessage(
						Msg.getSun("界面", "个人称号", "称号设置成功", new String[] { "{Sign}" }, new Object[] { Sign }));
			} else
				player.sendMessage(Msg.getSun("界面", "个人称号", "设置异常", new String[] { "{Sign}" }, new Object[] { Sign }));
			return true;
		case "mysign":
		case "ms":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return false;
			}
			list = PY.getPlayerSigns(player.getName());
			if (list.size() < 1) {
				player.sendMessage(Msg.getSun("界面", "个人称号", "无称号"));
				return true;
			}
			player.sendMessage(Tool.getColorFont("===========[您的称号]==========="));
			for (int i = 0; i < list.size(); i++)
				player.sendMessage("§6" + i + ": §f" + list.get(i));
			player.sendMessage("§6若想更改称号请使用‘/sk set <序号> ’来设置您的称号");
			return true;
		case "help":
		case "h":
		case "帮助":
			player.sendMessage(
					"§6/sk §dhelp§f:§e查看命令帮助\n§6/sk§d ms§f:§e查看自己已拥有的称号列表\n§6/sk §dset §a<§b称号序号§a>§f:§e更改称号为已拥有的称号\n§6/sk §dshop§f:§e查看称号商店列表\n§6/sk §dby§a <§b称号序号§a>§f:§9购买称号商店内的称号\n§4/sadmk 管理员命令");
			return true;
		default:
			return false;
		}
	}
}
