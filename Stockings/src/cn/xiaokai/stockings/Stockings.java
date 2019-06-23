package cn.xiaokai.stockings;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.xiaokai.stockings.prettylegs.Belle;
import cn.xiaokai.stockings.prettylegs.BiBi;
import cn.xiaokai.stockings.prettylegs.Nipple;
import cn.xiaokai.stockings.puss.Briefs;
import cn.xiaokai.stockings.puss.Buttock;
import cn.xiaokai.stockings.puss.CheckLegality;
import cn.xiaokai.stockings.puss.Message;
import cn.xiaokai.tool.Tool;
import cn.xiaokai.tool.up.Update;

/**
 * @author Winfxk
 */
public class Stockings extends PluginBase {
	private Instant loadTime = Instant.now();
	public Config config;
	public static Stockings mis;
	public Message Msg;
	public LinkedHashMap<String, Briefs> PlayerOfBriefs = new LinkedHashMap<String, Briefs>();
	private MyCmd cmd;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "sadmk":
			return cmd.AdminCommand(sender, label, args);
		case "sk":
		default:
			return cmd.PlayerCommand(sender, label, args);
		}
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BiBi(this), this);
		pm.registerEvents(new Nipple(this), this);
		pm.registerEvents(new Belle(), this);
		if (config.getBoolean("检测更新"))
			(new Update(this)).start();
		super.onEnable();
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "启动！") + TextFormat.GREEN + "耗时：" + TextFormat.BLUE
						+ ((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000) + TextFormat.GREEN
						+ "s");
	}

	@Override
	public void onLoad() {
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "正在加载..."));
		mis = this;
		(new CheckLegality(this)).start();
		config = new Config(new File(getDataFolder(), Buttock.ConfigName), Config.YAML);
		Msg = new Message(this);
		new Config(new File(getDataFolder(), Buttock.ShopName), Config.YAML);
		cmd = new MyCmd(this);
		super.onLoad();
	}

	/**
	 * ????这都看不懂？？这是插件关闭事件
	 */
	@Override
	public void onDisable() {
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "关闭！") + TextFormat.GREEN + "本次运行时长" + TextFormat.BLUE
						+ Tool.getTimeBy(((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000))
						+ TextFormat.GREEN + "s");
		super.onDisable();
	}

	/**
	 * 返回货币的名称，如“金币”
	 * 
	 * @return
	 */
	public String getMoneyName() {
		return config.getString("货币单位");
	}
}
