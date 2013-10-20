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
package net.visualillusionsent.dconomy.addon.bank;

import net.visualillusionsent.dconomy.addon.bank.data.BankProperties;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.logging.dCoLevel;
import net.visualillusionsent.utils.PropertiesFile;

import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class dBankLiteBase {
    private final float dCoVersion = 3.0F;
    private final long dCoRevision = 0;
    private final Logger logger;
    private final Timer timer;
    private final BankProperties props;
    private final PropertiesFile timer_reset;
    private final MessageTranslator translator;

    private static dBankLiteBase $;

    public dBankLiteBase(dBankLite dbanklite) {
        $ = this;
        this.logger = dbanklite.getPluginLogger();
        if (incompatibilityTest()) {
            warning("dConomy appears to be a newer version. Incompatibility could result...");
        }
        props = new BankProperties();
        translator = new MessageTranslator();
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
        if (key.matches("bank\\.(deposit|withdraw)")) {
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
        if (key.matches("bank\\.(deposit|withdraw)")) {
            user.error($.translator.translate(key, user.getUserLocale(), args));
        }
        else { // Probably a dConomy default message
            dCoBase.translateErrorMessageFor(user, key, args);
        }
    }

    public final static void info(String msg) {
        $.logger.info(msg);
    }

    public final static void info(String msg, Throwable thrown) {
        $.logger.log(Level.INFO, msg, thrown);
    }

    public final static void warning(String msg) {
        $.logger.warning(msg);
    }

    public final static void warning(String msg, Throwable thrown) {
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public final static void severe(String msg) {
        $.logger.severe(msg);
    }

    public final static void severe(String msg, Throwable thrown) {
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public final static void stacktrace(Throwable thrown) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public final static void debug(String msg) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    public final static void cleanUp() {
        if ($.timer != null) {
            $.timer.cancel();
            $.timer.purge();
        }
        dCoBase.getProperties().getPropertiesFile().save();
    }

    private final long getInitialStart() {
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

    private final long getInterestInterval() {
        return $.props.getLong("interest.pay.interval") * 60000;
    }

    final void setResetTime() {
        timer_reset.setLong("bank.timer.reset", System.currentTimeMillis() + getInterestInterval());
    }

    private final boolean incompatibilityTest() {
        if (dCoBase.getVersion() > dCoVersion) {
            return true;
        }
        else if (dCoBase.getRevision() > dCoRevision) {
            return true;
        }
        return false;
    }
}
