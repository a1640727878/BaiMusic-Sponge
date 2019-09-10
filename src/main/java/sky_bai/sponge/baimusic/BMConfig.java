package sky_bai.sponge.baimusic;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import sky_bai.sponge.baimusic.config.ConfigMusic;

public class BMConfig {
	static Path BMDefaultPath;
	static Path BMConfigPath;
	
	static ConfigurationNode config;
	
	private static Map<String, ConfigMusic> configMusicMap;
	private static Boolean mod;

	@SuppressWarnings("serial")
	public static void setMessageSet() throws ObjectMappingException {
		configMusicMap = new HashMap<String, ConfigMusic>();
		configMusicMap.putAll(config.getNode("Music").getValue(new TypeToken<Map<String, ConfigMusic>>() {}, Collections.emptyMap()));
		mod = config.getNode("Mod").getBoolean(true);
		if (mod) {
			BMNetwork.channel_name = "AudioBuffer";
		} else {
			BMNetwork.channel_name = "BaiMusic";
		}
	}
	
	public static Map<String, ConfigMusic> getConfigMusicMap() {
		return configMusicMap;
	}
	
	public static Boolean getMod() {
		return mod;
	}
}
