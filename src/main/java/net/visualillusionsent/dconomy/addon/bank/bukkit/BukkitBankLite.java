/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.bukkit;

import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankTransactionEvent;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/** @author Jason (darkdiplomat) */
public class BukkitBankLite extends VisualIllusionsBukkitPlugin implements dBankLite {
    private dBankLiteBase base;

    @Override
    public void onEnable() {
        super.onEnable();

        try {
            base = new dBankLiteBase(this);
            new BukkitBankLiteAPIListener(this);
            new BukkitBankLiteCommandExecutor(this);

            dCoBase.getServer().registerTransactionHandler(BankTransactionEvent.class, BankTransaction.class);
        }
        catch (Exception ex) {
            String reason = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            if (debug) { // Only stack trace if debugging
                getLogger().log(Level.SEVERE, "dBankLite failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogger().severe("dBankLite failed to start. Reason: ".concat(reason));
            }
            die();
        }
    }

    @Override
    public void onDisable() {
        if (base != null) {
            dCoBase.getServer().unregisterTransactionHandler(BankTransactionEvent.class);
            base.cleanUp();
        }
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

    final dBankLiteBase getBaseInstance() {
        return base;
    }
}
