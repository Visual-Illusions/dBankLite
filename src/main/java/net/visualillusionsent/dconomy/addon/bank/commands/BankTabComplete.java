/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.CommandTabCompleteUtil;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.visualillusionsent.dconomy.addon.bank.commands.BankPermissions.BANK;
import static net.visualillusionsent.dconomy.addon.bank.commands.BankPermissions.BANK$ADMIN;

/**
 * @author Jason (darkdiplomat)
 */
public class BankTabComplete extends CommandTabCompleteUtil {
    private static final String[] bankCMD = new String[]{"deposit", "withdraw", "add", "remove", "set", "reload", "reset", "lock"};
    public static final Matcher //
            matchP = Pattern.compile("(deposit|withdraw)").matcher(""),
            matchA = Pattern.compile("(add|remove|set|reload|reset|lock)").matcher(""),
            subUser1 = Pattern.compile("(reload|reset)").matcher(""),
            subUser2 = Pattern.compile("(add|remove|set|lock)").matcher("");

    public static List<String> match(dConomyUser user, String[] args) {
        switch (args.length) {
            case 1:
                List<String> preRet = matchTo(args, bankCMD);
                Iterator<String> preRetItr = preRet.iterator();
                while (preRetItr.hasNext()) {
                    String ret = preRetItr.next();
                    if (matchP.reset(ret).matches() && !user.hasPermission(BANK.concat(".").concat(ret))) {
                        preRetItr.remove();
                    }
                    else if (matchA.reset(ret).matches() && !user.hasPermission(BANK$ADMIN.concat(".").concat(ret))) {
                        preRetItr.remove();
                    }
                }
                return preRet;
            case 2:
                if ((subUser1.reset(args[0]).matches() && user.hasPermission(BANK$ADMIN.concat(".").concat(args[0])))) {
                    return matchTo(args, dCoBase.getServer().getUserNames());
                }
                break;
            case 3:
                if (subUser2.reset(args[0]).matches() && user.hasPermission(BANK$ADMIN.concat(".").concat(args[0]))) {
                    return matchTo(args, dCoBase.getServer().getUserNames());
                }
                break;
        }
        return null;
    }
}
