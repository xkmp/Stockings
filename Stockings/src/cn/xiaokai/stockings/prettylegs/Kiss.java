package cn.xiaokai.stockings.prettylegs;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.form.MakeForm;
import cn.xiaokai.stockings.form.Shop;
import cn.xiaokai.stockings.puss.Briefs;
import cn.xiaokai.stockings.py.PY;
import cn.xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class Kiss {
	private Player player;
	private Stockings mis;

	public Kiss(Player player) {
		this.mis = Stockings.mis;
		this.player = player;
	}

	public boolean Setting(FormResponseCustom data) {
		mis.config.set("检测更新", data.getToggleResponse(0));
		mis.config.set("货币单位", data.getInputResponse(1));
		mis.config.set("潜行开关", data.getToggleResponse(2));
		mis.config.set("潜行概率",
				Tool.isInteger(data.getInputResponse(3)) ? Float.valueOf(data.getInputResponse(3)).intValue() : 10);
		mis.config.set("死亡提示", data.getToggleResponse(4));
		mis.config.set("进服默认称号", data.getInputResponse(5));
		mis.config.set("聊天管控", data.getToggleResponse(6));
		mis.config.set("监控违禁词", data.getToggleResponse(7));
		return MakeForm.Tip(player, "§6设置成功");
	}

	public boolean SettingMain(FormResponseSimple data) {
		if (!player.isOp())
			return MakeForm.Tip(player, mis.Msg.getMessage("权限不足"));
		switch (data.getClickedButtonId()) {
		case 2:
			(new MakeForm(mis)).Config(player);
			break;
		case 1:
			(new Shop(player)).DelSign();
			break;
		case 0:
		default:
			(new Shop(player)).AddSign();
			break;
		}
		return true;
	}

	public boolean MySign(FormResponseSimple data) {
		List<String> list = PY.getPlayerSigns(player);
		String Sign = list
				.get(data.getClickedButtonId() > list.size() - 1 ? list.size() - 1 : data.getClickedButtonId());
		if (PY.setPlayerSign(player, Sign))
			return MakeForm.Tip(player, mis.Msg.getSun("界面", "个人称号", "成功设置称号提示", new String[] { "{Sign}", "{Player}" },
					new Object[] { Sign, player.getName() }), true);
		return MakeForm.Tip(player, mis.Msg.getSun("界面", "个人称号", "设置失败提示", new String[] { "{Sign}", "{Player}" },
				new Object[] { Sign, player.getName() }));
	}

	public boolean Main(FormResponseSimple data) {
		Briefs briefs = mis.PlayerOfBriefs.get(player.getName().toLowerCase());
		List<String> list = briefs.FormListKey;
		switch (list.get(data.getClickedButtonId())) {
		case "P":
			if (!player.isOp())
				return MakeForm.Tip(player, mis.Msg.getMessage("权限不足"));
			(new MakeForm(mis)).Setting(player);
			return true;
		case "M":
			(new MakeForm(mis)).MySign(player);
			return true;
		case "G":
		default:
			(new MakeForm(mis)).Shop(player);
			return true;
		}
	}
}
