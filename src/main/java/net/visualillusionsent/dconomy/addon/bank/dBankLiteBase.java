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
package net.visualillusionsent.dconomy.addon.bank;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankAPIListener;
import net.visualillusionsent.dconomy.addon.bank.data.BankProperties;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.logging.dCoLevel;
import net.visualillusionsent.minecraft.plugin.PluginInitializationException;
import net.visualillusionsent.utils.PropertiesFile;

import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jason (darkdiplomat)
 */
public final class dBankLiteBase {
    private final Logger logger;
    private final Timer timer;
    private final BankProperties props;
    private final PropertiesFile timer_reset;
    private final dBankLiteTranslator translator;
    private final BankHandler bank_handler;
    private static dBankLiteBase $;
    private static final Matcher bankDepWith = Pattern.compile("bank\\.(deposit|withdraw)").matcher("");

    public dBankLiteBase(dBankLite dbanklite) {
        $ = this;
        this.logger = dbanklite.getPluginLogger();
        if (!dCoBase.isNewerThan(3.0F, 0, true)) {
            throw new PluginInitializationException("dBankLite requires dConomy 3.0.1 or newer to function...");
        }
        if (dCoBase.isNewerThan(3.0F, 0xDEAD, false)) {
            warning("dConomy appears to be a newer version. Incompatibility could result...");
        }
        props = new BankProperties();
        bank_handler = new BankHandler(dCoBase.getDataHandler().getDataSourceType());
        BankAPIListener.setBankHandler(bank_handler);
        translator = new dBankLiteTranslator(dbanklite);
        timer_reset = new PropertiesFile("config/dBankLite/.reset.dbl");
        if (getInterestInterval() > 0) { // interest enabled?
            timer = new Timer();
            timer.scheduleAtFixedRate(new InterestPayer(this), getInitialStart(), getInterestInterval());
        }
        else {
            timer = null;
        }
    }

    /**
     * Sends a dBankLite Translated message to a user
     *
     * @param user
     *         the {@link dConomyUser} to send the message to
     * @param key
     *         the key to look up
     * @param args
     *         the arguments to format the message with
     */
    public static void translateMessageFor(dConomyUser user, String key, Object... args) {
        if (bankDepWith.reset(key).matches()) {
            user.message($.translator.translate(key, user.getUserLocale(), args));
        }
        else { // Probably a dConomy default message
            dCoBase.translateMessageFor(user, key, args);
        }
    }

    /**
     * Sends a dBankLite Translated error message to a user
     *
     * @param user
     *         the {@link dConomyUser} to send the message to
     * @param key
     *         the key to look up
     * @param args
     *         the arguments to format the message with
     */
    public static void translateErrorMessageFor(dConomyUser user, String key, Object... args) {
        if (bankDepWith.reset(key).matches()) {
            user.error($.translator.translate(key, user.getUserLocale(), args));
        }
        else { // Probably a dConomy default message
            dCoBase.translateErrorMessageFor(user, key, args);
        }
    }

    public static void info(String msg) {
        $.logger.info(msg);
    }

    public static void info(String msg, Throwable thrown) {
        $.logger.log(Level.INFO, msg, thrown);
    }

    public static void warning(String msg) {
        $.logger.warning(msg);
    }

    public static void warning(String msg, Throwable thrown) {
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public static void severe(String msg) {
        $.logger.severe(msg);
    }

    public static void severe(String msg, Throwable thrown) {
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public static void stacktrace(Throwable thrown) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public static void debug(String msg) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    public final void cleanUp() {
        if ($.timer != null) {
            $.timer.cancel();
            $.timer.purge();
        }
        if ($.bank_handler != null) {
            $.bank_handler.cleanUp();
        }
    }

    private long getInitialStart() {
        if (timer_reset.containsKey("bank.timer.reset")) {
            long reset = timer_reset.getLong("bank.timer.reset") - System.currentTimeMillis();
            if (reset < 0) {
                return 60000;
            }
            else {
                timer_reset.setLong("bank.timer.reset", System.currentTimeMillis() + reset);
                return reset;
            }
        }
        else {
            setResetTime();
            return getInterestInterval();
        }
    }

    private long getInterestInterval() {
        return $.props.getLong("interest.pay.interval") * 60000;
    }

    final void setResetTime() {
        timer_reset.setLong("bank.timer.reset", System.currentTimeMillis() + getInterestInterval());
    }

    public final BankHandler getHandlerInstance() {
        return bank_handler;
    }
}
