package sky_bai.sponge.baimusic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.plugin.Plugin;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Plugin(id = "baimusic", name = "BaiMusic", authors = { "sky_bai" })
public class BaiMusic {
	public final static Logger logger = LoggerFactory.getLogger("BaiMusic");
	static BaiMusic baiMusic;
	

	BaiMusic() {
		baiMusic = this;
	}

	@Listener
	public void onServerStart(GameInitializationEvent event) {
		try {
			setConfig();
		} catch (IOException | ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		BMNetwork.channel = Sponge.getChannelRegistrar().createRawChannel(this, BMNetwork.channel_name);
		BMNetwork.channel.addListener(BMNetwork.getRawDataListener());
		BMNetwork.channel_2 = Sponge.getChannelRegistrar().createRawChannel(this, "AudioBufferOut");
		BMNetwork.channel_2.addListener(BMNetwork.getRawDataListener());
	}
	
	String onChannelBufGetString(ChannelBuf data) {
		return new String(data.readBytes(0,data.getCapacity()));
	}

	final void setConfigPath() {
		BMConfig.BMDefaultPath = Sponge.getConfigManager().getPluginConfig(this).getDirectory().resolveSibling("BaiMusic");
		BMConfig.BMConfigPath = BMConfig.BMDefaultPath.resolve("config.conf");
	}

	final void setConfigFile() throws IOException {
		Sponge.getAssetManager().getAsset(this, "config.conf").get().copyToDirectory(BMConfig.BMDefaultPath, false, true);
	}

	final void setConfig() throws IOException, ObjectMappingException {
		setConfigPath();
		setConfigFile();
		reloadPlugin();
		Sponge.getCommandManager().register(this, new BMCommand().getBMCommandSpec(), "BaiMusic", "BM");
	}

	final void reloadPlugin() throws IOException, ObjectMappingException {
		BMConfig.config = HoconConfigurationLoader.builder().setPath(BMConfig.BMConfigPath).build().load();
		BMConfig.setMessageSet();
		logger.info("当前Mod模式为 "+BMNetwork.channel_name);
	}

}
