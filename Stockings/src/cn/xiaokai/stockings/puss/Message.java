package cn.xiaokai.stockings.puss;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.utils.Config;
import cn.xiaokai.stockings.Stockings;
import cn.xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Message {
	private Stockings mis;
	private String[] Key = { "{n}", "{RandColor}", "{ServerName}", "{PluginName}", "{MoneyName}", "{Time}", "{Date}" };
	private String[] Data = {};
	public Config Message;

	public Message(Stockings mis) {
		this.mis = mis;
		Message = new Config(new File(mis.getDataFolder(), Buttock.MsgName), 2);
	}

	private void load() {
		Data = new String[] { "\n", Tool.getRandColor(), mis.getServer().getMotd(), mis.getName(), mis.getMoneyName(),
				Tool.getTime(), Tool.getDate() };
	}

	public String getSun(String t, String Son, String Sun) {
		return getSun(t, Son, Sun, new String[] {}, new String[] {});
	}

	public String getSun(String t, String Son, String Sun, String[] myKey, Object[] myData) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son) && (map.get(Son) instanceof Map)) {
				map = (HashMap<String, Object>) map.get(Son);
				if (map.containsKey(Sun))
					return getText(map.get(Sun).toString(), myKey, myData);
			}
		}
		return null;
	}

	public String getSon(String t, String Son) {
		return getSon(t, Son, new String[] {}, new String[] {});
	}

	public String getSon(String t, String Son, String[] myKey, Object[] myData) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son))
				return getText(map.get(Son).toString(), myKey, myData);
		}
		return null;
	}

	public String getMessage(String t) {
		return getMessage(t, new String[] {}, new String[] {});
	}

	public String getMessage(String t, String[] myKey, Object[] myData) {
		if (Message.exists(t))
			return getText(Message.getString(t), myKey, myData);
		return null;
	}

	public String getText(String text) {
		return getText(text, new String[] {}, new String[] {});
	}

	public String getText(String text, String[] myKey, Object[] myData) {
		load();
		for (int i = 0; i < Key.length; i++)
			if (text.contains(Key[i]))
				text = text.replace(Key[i], Data[i]);
		for (int i = 0; (i < myKey.length && i < myData.length); i++)
			if (text.contains(myKey[i]))
				text = text.replace(myKey[i], myData[i].toString());
		return text;
	}
}
