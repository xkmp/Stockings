package cn.xiaokai.stockings.prettylegs;
/**
 * @author Winfxk
 */

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.stockings.form.MakeForm;
import cn.xiaokai.stockings.form.Shop;
import cn.xiaokai.stockings.puss.FormID;

public class Belle implements Listener {

	public Belle() {
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onPlayerForm(PlayerFormRespondedEvent e) {
		Player player = e.getPlayer();
		if (player == null || e.wasClosed() || e.getResponse() == null
				|| (!(e.getResponse() instanceof FormResponseCustom) && !(e.getResponse() instanceof FormResponseSimple)
						&& !(e.getResponse() instanceof FormResponseModal)))
			return;
		FormResponse data = e.getResponse();
		switch (e.getFormID()) {
		case FormID.Setting:
			(new Kiss(player)).Setting((FormResponseCustom) data);
			break;
		case FormID.SignDelItem:
			(new Shop(player)).DelSign((FormResponseSimple) data);
			break;
		case FormID.SignAddItem:
			(new Shop(player)).AddSign((FormResponseCustom) data);
			break;
		case FormID.SettingMain:
			(new Kiss(player)).SettingMain((FormResponseSimple) data);
			break;
		case FormID.MySignList:
			(new Kiss(player)).MySign((FormResponseSimple) data);
			break;
		case FormID.ShopMain:
			(new MakeForm(Stockings.mis)).Shop(player, (FormResponseSimple) data);
			break;
		case FormID.MainID:
			(new Kiss(player)).Main((FormResponseSimple) data);
			break;
		default:
			break;
		}
	}
}
