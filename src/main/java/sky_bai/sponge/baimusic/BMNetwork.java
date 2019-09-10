package sky_bai.sponge.baimusic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.spongepowered.api.Platform.Type;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;

import sky_bai.sponge.baimusic.config.ConfigMusic;

public class BMNetwork {
	static ChannelBinding.RawDataChannel channel;
	static ChannelBinding.RawDataChannel channel_2;

	static String channel_name;
	private static RawDataListener rawDataListener;

	static RawDataListener getRawDataListener() {
		if (rawDataListener == null) {
			rawDataListener = new RawDataListener() {
				@Override
				public void handlePayload(ChannelBuf data, RemoteConnection connection, Type side) {
					BaiMusic.logger.info("服务器与客户端通讯正常");
				}
			};
		}
		return rawDataListener;
	}

	static void playMusic(Player player, ConfigMusic configMusic) {
		if (BMConfig.getMod()) {
			playMusicAB(player, configMusic);
		} else {
			playMusicBM(player, configMusic);
		}
	}

	static void playMusicAB(Player player, ConfigMusic configMusic) {
		byte[] dataByte = ("[Net]" + configMusic.getURL()).getBytes();
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}

	static void playMusicBM(Player player, ConfigMusic configMusic) {
		Object[] data = { "Play", configMusic.getURL(), configMusic.getHash() };
		byte[] dataByte = getBytes(data);
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}

	static void stopMusic(Player player) {
		if (BMConfig.getMod()) {
			stopMusicAB(player);
		} else {
			stopMusicBM(player);
		}
	}

	static void stopMusicAB(Player player) {
		byte[] dataByte = "[Stop]".getBytes();
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}

	static void stopMusicBM(Player player) {
		Object[] data = { "Stop" };
		byte[] dataByte = getBytes(data);
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}

	static void setVolume(Player player, Integer i) {
		if (BMConfig.getMod()) {
			setVolumeAB(player, i);
		} else {
			setVolumeBM(player, i);
		}
	}

	static void setVolumeAB(Player player, Integer i) {
		Float f = Float.valueOf(i) / 100f;
		byte[] dataByte = ("[Volume]" + f).getBytes();
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}

	static void setVolumeBM(Player player, Integer i) {
		Object[] data = { "Volume", i };
		byte[] dataByte = getBytes(data);
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}

	static void debug(Player player) {
		byte[] dataByte = ("[Has]10086*10086.mp3").getBytes();
		channel.sendTo(player, channelBuf -> channelBuf.writeBytes(dataByte));
	}
	
	static byte[] getBytes(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	static Object getObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}
}
