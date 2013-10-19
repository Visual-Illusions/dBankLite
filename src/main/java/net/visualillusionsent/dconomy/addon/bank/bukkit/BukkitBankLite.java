/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dBankLite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dBankLite.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.bukkit;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankTransactionEvent;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPlugin;

import java.util.logging.Logger;

/** @author Jason (darkdiplomat) */
public class BukkitBankLite extends VisualIllusionsBukkitPlugin implements dBankLite {

    @Override
    public void onEnable() {
        super.onEnable();

        new dBankLiteBase(this);
        BankHandler.initialize();
        dCoBase.getServer().registerTransactionHandler(BankTransactionEvent.class, BankTransaction.class);
        new BukkitBankLiteAPIListener(this);
        new BukkitBankLiteCommandExecutor(this);
    }

    @Override
    public void onDisable() {
        dCoBase.getServer().unregisterTransactionHandler(BankTransactionEvent.class);
        dBankLiteBase.cleanUp();
    }

    @Override
    public final Logger getPluginLogger() {
        return this.getLogger();
    }

    @Override
    public void error(String message) {
        getLogger().warning(message);
    }

    @Override
    public void message(String message) {
        getLogger().info(message);
    }

    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
