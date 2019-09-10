package sky_bai.sponge.baimusic;

import sky_bai.sponge.baimusic.config.ConfigMusic;
import org.spongepowered.api.world.World;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class BMCommand {
	static String pluginName = "§l[BaiMusic]§r ";
	CommandSpec BMCommandSpec;
	CommandSpec Play;
	CommandSpec Stop;
	CommandSpec Volume;
	CommandSpec Debug;
	CommandSpec Reload;

	private CommandElement MusicCE = GenericArguments.choices(Text.of("Music"), () -> BMConfig.getConfigMusicMap().keySet(), key -> BMConfig.getConfigMusicMap().get(key), false);
	private CommandElement PlayerCE = GenericArguments.player(Text.of("Player"));
	private CommandElement WorldCE = GenericArguments.world(Text.of("World"));

	public BMCommand() {
		Reload = CommandSpec.builder().permission("BaiMusic.Reload").executor((src, args) -> {
			try {
				BaiMusic.baiMusic.reloadPlugin();
			} catch (IOException | ObjectMappingException e) {
				src.sendMessage(Text.of(pluginName + "重载失败...."));
				return CommandResult.empty();
			}
			src.sendMessage(Text.of(pluginName + "重载成功!!!"));
			return CommandResult.success();
		}).build();
		Volume = CommandSpec.builder().permission("BaiMusic.Volume").arguments(PlayerCE, GenericArguments.integer(Text.of("0-100"))).executor((src, args) -> {
			Player player = args.<Player>getOne("Player").get();
			Integer i = args.<Integer>getOne("Player").get();
			BMNetwork.setVolume(player, i);
			return CommandResult.success();
		}).build();
		Debug = CommandSpec.builder().permission("BaiMusic.Debug").arguments(PlayerCE).executor((src, args) -> {
			Player player = args.<Player>getOne("Player").get();
			BMNetwork.debug(player);
			return CommandResult.success();
		}).build();
		onPlayCommand();
		onStopCommand();
		BMCommandSpec = CommandSpec.builder().permission("BaiMusic").child(Reload, "reload").child(Play, "play").child(Stop, "stop").child(Volume, "volume").child(Debug, "debug").build();
	}

	void onPlayCommand() {
		CommandSpec play_Player = CommandSpec.builder().permission("BaiMusic.Paly.Player").arguments(PlayerCE, MusicCE).executor((src, args) -> {
			ConfigMusic music = args.<ConfigMusic>getOne("Music").get();
			Player player = args.<Player>getOne("Player").get();
			BMNetwork.playMusic(player, music);
			src.sendMessage(Text.of(pluginName + "已为玩家 [" + player.getName() + "] 播放音乐"));
			return CommandResult.success();
		}).build();
		CommandSpec play_World = CommandSpec.builder().permission("BaiMusic.Paly.World").arguments(WorldCE, MusicCE).executor((src, args) -> {
			ConfigMusic music = args.<ConfigMusic>getOne("Music").get();
			World world = args.<World>getOne("world").get();
			for (Player player : world.getPlayers()) {
				BMNetwork.playMusic(player, music);
			}
			src.sendMessage(Text.of(pluginName + "已为世界 [" + world.getName() + "] 的玩家们播放音乐"));
			return CommandResult.success();
		}).build();
		CommandSpec play_ALL = CommandSpec.builder().permission("BaiMusic.Paly.ALL").arguments(MusicCE).executor((src, args) -> {
			ConfigMusic music = args.<ConfigMusic>getOne("Music").get();
			for (Player player : Sponge.getServer().getOnlinePlayers()) {
				BMNetwork.playMusic(player, music);
			}
			src.sendMessage(Text.of(pluginName + "已为全服玩家们播放音乐"));
			return CommandResult.success();
		}).build();
		CommandSpec play_Me = CommandSpec.builder().permission("BaiMusic.Paly.Me").arguments(MusicCE).executor((src, args) -> {
			if (src instanceof Player == false) {
				src.sendMessage(Text.of(pluginName + "这个命令只有玩家可以执行"));
				return CommandResult.empty();
			}
			ConfigMusic music = args.<ConfigMusic>getOne("Music").get();
			BMNetwork.playMusic((Player) src, music);
			src.sendMessage(Text.of(pluginName + "已为您播放音乐"));
			return CommandResult.success();
		}).build();
		Play = CommandSpec.builder().permission("BaiMusic.Paly").child(play_Player, "player").child(play_World, "world").child(play_ALL, "all").child(play_Me, "me").build();
	}

	void onStopCommand() {
		CommandSpec stop_Player = CommandSpec.builder().permission("BaiMusic.Stop.Player").arguments(PlayerCE).executor((src, args) -> {
			Player player = args.<Player>getOne("Player").get();
			BMNetwork.stopMusic(player);
			src.sendMessage(Text.of(pluginName + "已关闭玩家 [" + player.getName() + "] 的音乐"));
			return CommandResult.success();
		}).build();
		CommandSpec stop_World = CommandSpec.builder().permission("BaiMusic.Stop.World").arguments(WorldCE).executor((src, args) -> {
			World world = args.<World>getOne("world").get();
			for (Player player : world.getPlayers()) {
				BMNetwork.stopMusic(player);
			}
			src.sendMessage(Text.of(pluginName + "已关闭世界 [" + world.getName() + "] 玩家们的音乐"));
			return CommandResult.success();
		}).build();
		CommandSpec stop_ALL = CommandSpec.builder().permission("BaiMusic.Stop.ALL").executor((src, args) -> {
			for (Player player : Sponge.getServer().getOnlinePlayers()) {
				BMNetwork.stopMusic(player);
			}
			src.sendMessage(Text.of(pluginName + "已关闭全服玩家的音乐"));
			return CommandResult.success();
		}).build();
		CommandSpec stop_Me = CommandSpec.builder().permission("BaiMusic.Stop.Me").executor((src, args) -> {
			if (src instanceof Player == false) {
				src.sendMessage(Text.of(pluginName + "这个命令只有玩家可以执行"));
				return CommandResult.empty();
			}
			BMNetwork.stopMusic((Player) src);
			src.sendMessage(Text.of(pluginName + "已关闭您的音乐"));
			return CommandResult.success();
		}).build();
		Stop = CommandSpec.builder().permission("BaiMusic.Stop").child(stop_Player, "player").child(stop_World, "world").child(stop_ALL, "all").child(stop_Me, "me").build();
	}

	public CommandSpec getBMCommandSpec() {
		return BMCommandSpec;
	}
	
	
}
