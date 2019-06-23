package cn.xiaokai.stockings.puss;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.Utils;
import cn.xiaokai.stockings.Stockings;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class CheckLegality {
	private Stockings mis;

	public CheckLegality(Stockings mis) {
		this.mis = mis;
	}

	public void start() {
		for (String dir : Buttock.Mkdir) {
			File file = new File(mis.getDataFolder() + dir);
			if (!file.exists())
				file.mkdirs();
		}
		Config config;
		Map<String, Object> map;
		Map<String, Object> cg;
		for (String FileNae : Buttock.ExamineConfigNameList) {
			try {
				mis.getLogger().info("§6正在检查文件" + FileNae);
				String content = Utils.readFile(this.getClass().getResourceAsStream("/resources/" + FileNae));
				DumperOptions dumperOptions = new DumperOptions();
				dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
				Yaml yaml = new Yaml(dumperOptions);
				map = new ConfigSection(yaml.loadAs(content, LinkedHashMap.class));
				config = new Config(new File(mis.getDataFolder(), FileNae), Config.YAML);
				cg = config.getAll();
				isMap(map, cg, config);
			} catch (IOException e) {
				mis.getLogger().info("§4在检查数据中遇到错误！请尝试删除该文件§9[§d" + FileNae + "§9]\n§f" + e.getMessage());
			}
		}
	}

	public void isMap(Map<String, Object> map, Map<String, Object> cg, Config config) {
		for (String ike : map.keySet()) {
			if (!cg.containsKey(ike)) {
				cg.put(ike, map.get(ike));
				mis.getLogger().info("§6" + ike + "§4所属的数据错误！已回复默认");
				continue;
			}
			if (((cg.get(ike) instanceof Map) || (map.get(ike) instanceof Map))
					|| ((cg.get(ike) instanceof List) && (map.get(ike) instanceof List)
							|| ((cg.get(ike) instanceof String) && (map.get(ike) instanceof String)))
					|| ((map.get(ike) instanceof Integer) && (cg.get(ike) instanceof Integer))
					|| ((map.get(ike) instanceof Boolean) && (cg.get(ike) instanceof Boolean))
					|| ((map.get(ike) instanceof Float) && (cg.get(ike) instanceof Float))) {
			} else {
				cg.put(ike, map.get(ike));
				mis.getLogger().info("§6" + ike + "§4属性不匹配！已回复默认");
				continue;
			}
			if (map.get(ike) instanceof Map)
				cg.put(ike, icMap((Map<String, Object>) map.get(ike), (Map<String, Object>) cg.get(ike)));
		}
		config.setAll((LinkedHashMap<String, Object>) cg);
		config.save();
	}

	public Map<String, Object> icMap(Map<String, Object> map, Map<String, Object> cg) {
		for (String ike : map.keySet()) {
			if (!cg.containsKey(ike)) {
				cg.put(ike, map.get(ike));
				mis.getLogger().info("§6" + ike + "§4所属的数据错误！已回复默认");
				continue;
			}
			if (((cg.get(ike) instanceof Map) && (map.get(ike) instanceof Map))
					|| ((cg.get(ike) instanceof List) && (map.get(ike) instanceof List)
							|| ((cg.get(ike) instanceof String) && (map.get(ike) instanceof String)))) {
			} else {
				cg.put(ike, map.get(ike));
				mis.getLogger().info("§6" + ike + "§4属性不匹配！已回复默认");
				continue;
			}
			if (map.get(ike) instanceof Map)
				cg.put(ike, icMap((Map<String, Object>) map.get(ike), (Map<String, Object>) cg.get(ike)));
		}
		return cg;
	}
}
