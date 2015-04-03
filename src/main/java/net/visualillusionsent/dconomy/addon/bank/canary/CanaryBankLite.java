/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2015 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankTransactionHook;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

import java.util.UUID;

/**
 * dBankLite Main Canary Plugin
 *
 * @author Jason (darkdiplomat)
 */
public final class CanaryBankLite extends VisualIllusionsCanaryPlugin implements dBankLite {
    private dBankLiteBase base;

    @Override
    public final boolean enable() {
        super.enable();
        try {
            base = new dBankLiteBase(this);
            new CanaryBankLiteCommandListener(this);
            new CanaryBankLiteAPIListener(this);
            new CanaryBankLiteMOTDListener(this);
            dCoBase.getServer().registerTransactionHandler(BankTransactionHook.class, BankTransaction.class);

            return true;
        }
        catch (Exception ex) {
            String reason = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            if (debug) { // Only stack trace if debugging
                getLogman().error("dBankLite failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogman().error("dBankLite failed to start. Reason: ".concat(reason));
            }
        }
        return false;
    }

    @Override
    public final void disable() {
        if (base != null) {
            dCoBase.getServer().unregisterTransactionHandler(BankTransactionHook.class);
            base.cleanUp();
        }
    }

    @Override
    public UUID getUUID() {
        return UUID.nameUUIDFromBytes("dBankLite-AddOn-dConomy".getBytes());
    }

    @Override
    public UUID getOfflineUUID() {
        return getUUID();
    }

    @Override
    public void error(String message) {
        getLogman().warn(message);
    }

    @Override
    public void message(String message) {
        getLogman().info(message);
    }

    @Override
    public boolean hasPermission(String message) {
        return true;
    }

    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    final dBankLiteBase getBaseInstance() {
        return base;
    }
}
