/* 
 * Copyright 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dBankLite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dBankLite
 */
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModUser;
import net.visualillusionsent.utils.BooleanUtils;

public final class BankLockCommand extends dConomyCommand{

    public BankLockCommand(){
        super(1);
    }

    protected final void execute(ModUser user, String[] args){
        ModUser theUser = args[1].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[1]);
        if (theUser == null && !args[1].toUpperCase().equals("SERVER")) {
            user.error("error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !BankHandler.verifyAccount(theUser.getName())) {
            user.error("error.404.account", theUser.getName(), "BANK ACCOUNT");
            return;
        }
        boolean locked = BooleanUtils.parseBoolean(args[0]);
        BankHandler.getBankAccountByName(theUser == null ? "SERVER" : theUser.getName()).setLockOut(locked);
        if (locked) {
            user.error("admin.account.locked", theUser == null ? "SERVER" : theUser.getName(), "BANK ACCOUNT");
        }
        else {
            user.error("admin.account.unlocked", theUser == null ? "SERVER" : theUser.getName(), "BANK ACCOUNT");
        }
    }
}
