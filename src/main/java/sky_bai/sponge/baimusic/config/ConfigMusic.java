package sky_bai.sponge.baimusic.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigMusic {
	@Setting(value = "URL")
	String URL;
	@Setting(value = "Hash")
	String Hash;

	public String getURL() {
		return URL;
	}

	public String getHash() {
		return Hash;
	}

	public String toString() {
		return "[URL = \"" + URL + "\" , Hash = \"" + Hash + "\"]";
	}
}
