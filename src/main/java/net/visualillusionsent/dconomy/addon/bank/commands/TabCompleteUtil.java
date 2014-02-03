package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.CommandTabCompleteUtil;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jason (darkdiplomat)
 */
public class TabCompleteUtil extends CommandTabCompleteUtil {
    private static final String[] bankCMD = new String[]{ "deposit", "withdraw", "add", "remove", "set", "reload", "reset", "lock" };
    public static final Matcher matchP = Pattern.compile("(deposit|withdraw)").matcher(""),
            matchA = Pattern.compile("(add|remove|set|reload|reset|lock)").matcher(""),
            subUser1 = Pattern.compile("(reload|reset)").matcher(""),
            subUser2 = Pattern.compile("(add|remove|set|lock)").matcher("");

    public static List<String> match(dConomyUser user, String[] args) {
        if (args.length == 1) {
            List<String> preRet = matchTo(args, bankCMD);
            Iterator<String> preRetItr = preRet.iterator();
            while (preRetItr.hasNext()) {
                String ret = preRetItr.next();
                if (matchP.reset(ret).matches() && !user.hasPermission("dconomy.bank.".concat(ret))) {
                    preRetItr.remove();
                }
                else if (matchA.reset(ret).matches() && !user.hasPermission("dconomy.admin.bank.".concat(ret))) {
                    preRetItr.remove();
                }
            }
            return preRet;
        }
        else if ((args.length == 2 && subUser1.reset(args[0]).matches())
                || (args.length == 3 && subUser2.reset(args[0]).matches())) {
            return matchTo(args, dCoBase.getServer().getUserNames());
        }
        return null;
    }
}
