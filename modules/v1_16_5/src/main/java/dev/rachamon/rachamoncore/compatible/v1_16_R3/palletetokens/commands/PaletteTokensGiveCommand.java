package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.commands;

import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.GenderProperties;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.factory.PaletteTokensFactoryImpl;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PaletteTokensGiveCommand extends AbstractCommand {

    private final PaletteTokensFactoryImpl module;

    public PaletteTokensGiveCommand(PaletteTokensFactoryImpl plugin) {
        super(CommandType.BOTH, false, "give");
        this.module = plugin;
    }

    @Override
    protected ReturnType execute(CommandSender sender, String... args) {
        if (args.length < 2) {
            return ReturnType.SYNTAX_ERROR;
        } else if (args.length < 3) {
            args = new String[]{args[0], args[1], "1"};
        } else if (args.length < 4 && !(sender instanceof Player)) {
            return ReturnType.NEEDS_PLAYER;
        }

        if (!PixelmonCommandUtils.tabCompletePokemon().contains(args[0])) {
            return ReturnType.SYNTAX_ERROR;
        }

        // validate args 3 if it's a number
        if (!args[2].matches("\\d+")) {
            return ReturnType.SYNTAX_ERROR;
        }

        String pokemon = args[0];
        String palette = args[1];
        int amount = Integer.parseInt(args[2]);

        Player target = args.length == 4 ? module.getPlugin().getServer().getPlayer(args[3]) : (Player) sender;

        if (target == null) {

            if (sender != null) {
                sender.sendMessage(this.module.getLocale().from(s -> s.getGeneralConfig().getPlayerIsNotOnlineNorInvalid()).process("player", args[3]).get());
            }

            return ReturnType.FAILURE;
        }

        if (amount < 1) {
            return ReturnType.FAILURE;
        }

        String itemName = module.getConfig().getTokenTemplate().getPokemon().replaceAll("&([0-9a-fA-Fk-oK-OrR])", "§$1");;
        String[] itemLore = module.getConfig().getTokenTemplate().getPalette();

        itemName = itemName.replace("{pokemon}", pokemon);
        for (int i = 0; i < itemLore.length; i++) {
            itemLore[i] = itemLore[i].replace("{pokemon}", pokemon);
            itemLore[i] = itemLore[i].replace("{palette}", palette);
            itemLore[i] = itemLore[i].replaceAll("&([0-9a-fA-Fk-oK-OrR])", "§$1");
        }

        Material material = Material.getMaterial(module.getConfig().getTokenTemplate().getMaterial());

        if (material == null) {
            material = Material.PAPER;
            module.getModuleLogger().error("Invalid material in config, defaulting to PAPER");
        }

        ItemStack item = new ItemStack(material, amount);

        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound pokemonNBT = new NBTTagCompound();
        pokemonNBT.setString("pokemon", pokemon);
        pokemonNBT.setString("palette", palette);
        nbt.set("rachamon-palette-token", pokemonNBT);

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);
        nmsItem.setTag(nbt);

        item = CraftItemStack.asBukkitCopy(nmsItem);

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return ReturnType.FAILURE;
        }

        meta.setDisplayName(itemName);
        meta.setLore(Arrays.asList(itemLore));
        meta.setCustomModelData(module.getConfig().getTokenTemplate().getCustomModelData());

        item.setItemMeta(meta);

        target.getInventory().addItem(item);

        if (target != sender) {
            this.module.getLocale().from(s -> s.getGeneralConfig().getSuccessfullySendTokenToPlayer()).process("pokemon", pokemon).process("palette", palette).process("player", target.getName()).send(sender);
        }

        this.module.getLocale().from(s -> s.getGeneralConfig().getSuccessfullyRetrieveToken()).process("pokemon", pokemon).process("palette", palette).send(target);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        List<String> pokemon = PixelmonCommandUtils.tabCompletePokemon();
        switch (args.length) {
            case 1:
                return pokemon;
            case 2:

                if (!pokemon.contains(args[0])) {
                    return null;
                }

                PokemonSpecification spec = PokemonSpecificationProxy.create(args[0]);
                Pokemon poke = spec.create();
                List<String> palettes = new ArrayList<>();
                for (Stats stats : poke.getSpecies().getForms()) {
                    for (GenderProperties gender : stats.getGenderProperties()) {
                        for (PaletteProperties palette : gender.getPalettes()) {
                            palettes.add(palette.getName());
                        }
                    }
                }

                return palettes;
            case 3:
                return Collections.singletonList("<amount>");
            case 4:
                return module.getPlugin().getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default:
                return null;
        }
    }

    @Override
    public String getPermissionNode() {
        return "rachamoncore.module.palettetokens.command.give";
    }

    @Override
    public String getSyntax() {
        return "palettetokens give <pokemon> <palette> <amount> [player]";
    }

    @Override
    public String getDescription() {
        return "Gives a palette token to a player";
    }
}
