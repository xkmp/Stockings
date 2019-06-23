package cn.xiaokai.stockings.py;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.puss.Buttock;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class PY {
	public static String getPlayerSign(Player player) {
		return getPlayerSign(player.getName());
	}

	public static String getPlayerSign(String player) {
		player = player.toLowerCase();
		if (!isPlayerConfig(player))
			return null;
		Config config = getPlayerConfig(player);
		return config.getString("Sign");
	}

	public static boolean isPlayerConfig(Player player) {
		return isPlayerConfig(player.getName());
	}

	public static boolean isPlayerConfig(String player) {
		player = player.toLowerCase();
		File file = new File(Stockings.mis.getDataFolder() + Buttock.PlayerPath, player + ".yml");
		return file.exists();
	}

	public static List<String> getPlayerSigns(Player player) {
		return getPlayerSigns(player.getName());
	}

	public static List<String> getPlayerSigns(String player) {
		if (!isPlayerConfig(player))
			return new ArrayList<String>();
		Config config = getPlayerConfig(player);
		return config.getList("Signs");
	}

	public static Config getPlayerConfig(Player player) {
		return getPlayerConfig(player.getName());
	}

	public static Config getPlayerConfig(String player) {
		if (!isPlayerConfig(player))
			return null;
		return new Config(new File(Stockings.mis.getDataFolder() + Buttock.PlayerPath, player.toLowerCase() + ".yml"),
				Config.YAML);
	}

	public static boolean setPlayerSign(Player player, String Sign) {
		return setPlayerSign(player.getName(), Sign);
	}

	public static boolean setPlayerSign(String player, String Sign) {
		Config config = getPlayerConfig(player);
		if (config == null)
			return false;
		config.set("Sign", Sign);
		return config.save();
	}

	public static boolean addPlayerSign(Player player, String Sign) {
		return addPlayerSign(player.getName(), Sign);
	}

	public static boolean addPlayerSign(String player, String Sign) {
		Config config = getPlayerConfig(player);
		if (config == null)
			return false;
		List<String> list = config.getList("Signs");
		if (list.contains(Sign))
			return false;
		list.add(Sign);
		config.set("Signs", list);
		return config.save();
	}

	public static boolean addAndSetPlayerSign(Player player, String Sign) {
		return addAndSetPlayerSign(player.getName(), Sign);
	}

	public static boolean addAndSetPlayerSign(String player, String Sign) {
		boolean addSign = addPlayerSign(player, Sign);
		boolean setSign = setPlayerSign(player, Sign);
		return (addSign && setSign);
	}

	public static boolean PlayerHaveSign(Player player, String Sign) {
		return PlayerHaveSign(player.getName(), Sign);
	}

	public static boolean PlayerHaveSign(String player, String Sign) {
		return getPlayerSigns(player).contains(Sign);
	}
}
