package cn.xiaokai.stockings.puss;
/**
 * @author Winfxk
 */

import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

public class Briefs {
	public Player player;
	public Config config;
	public List<String> FormListKey;
	public List<Map<String, Object>> ItemList;

	public Briefs(Player player, Config config) {
		this.player = player;
		this.config = config;
	}
}
