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
package net.visualillusionsent.dconomy.addon.bank;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.visualillusionsent.dconomy.MessageTranslator;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.io.logging.dCoLevel;
import net.visualillusionsent.utils.ProgramStatus;
import net.visualillusionsent.utils.PropertiesFile;
import net.visualillusionsent.utils.UtilityException;
import net.visualillusionsent.utils.VersionChecker;

public final class dBankLiteBase{

    private final float dCoVersion = 3.0F;
    private final Logger logger;
    private final VersionChecker vc;
    private final Timer timer;
    private ProgramStatus status;
    private float version;
    private short build;
    private String buildTime;

    private static dBankLiteBase $;

    public dBankLiteBase(IdBankLite dbanklite){
        $ = this;
        this.logger = dbanklite.getPluginLogger();
        if (dCoBase.getRawVersion() > dCoVersion) {
            warning("dConomy appears to be a newer version. Incompatibility could result.");
        }
        testdBankLiteProps();
        readManifest();
        checkStatus();
        vc = new VersionChecker("dBankLite", String.valueOf(version), String.valueOf(build), "http://visualillusionsent.net/minecraft/plugins/", status, true);
        checkVersion();
        installBankMessages();
        if (getInterestInterval() > 0) { // interest enabled?
            timer = new Timer();
            timer.scheduleAtFixedRate(new InterestPayer(this), getInitialStart(), getInterestInterval());
        }
        else {
            timer = null;
        }
    }

    private final void testdBankLiteProps(){
        PropertiesFile dCoProps = dCoBase.getProperties().getPropertiesFile();
        if (!dCoProps.containsKey("interest.rate")) {
            dCoProps.setFloat("interest.rate", 2.0F, "dBankLite: Interest Rate (in percentage) (Default: 2%)");
        }
        if (!dCoProps.containsKey("interest.pay.interval")) {
            dCoProps.setInt("interest.pay.interval", 360, "dBankLite: Interest Pay Interval (in minutes) (Default: 360 [6 Hours]) Set to 0 or less to disable");
        }
        if (!dCoProps.containsKey("interest.max.payout")) {
            dCoProps.setInt("interest.max.payout", 10000, "dBankLite: Max Interest Payout (Default: 10000)");
        }
        if (!dCoProps.containsKey("sql.bank.table")) {
            dCoProps.setString("sql.bank.table", "dBankLite", "dBankLite: SQL Bank table");
        }
    }

    public final static String getVersion(){
        return $.version + "." + $.build;
    }

    public final static float getRawVersion(){
        return $.version;
    }

    public static short getBuildNumber(){
        return $.build;
    }

    public static String getBuildTime(){
        return $.buildTime;
    }

    public final static ProgramStatus getProgramStatus(){
        return $.status;
    }

    private final void readManifest(){
        try {
            Manifest manifest = getManifest();
            Attributes mainAttribs = manifest.getMainAttributes();
            version = Float.parseFloat(mainAttribs.getValue("Version").replace("-SNAPSHOT", ""));
            build = Short.parseShort(mainAttribs.getValue("Build"));
            buildTime = mainAttribs.getValue("Build-Time");
            try {
                status = ProgramStatus.valueOf(mainAttribs.getValue("ProgramStatus"));
            }
            catch (IllegalArgumentException iaex) {
                status = ProgramStatus.UNKNOWN;
            }
        }
        catch (Exception ex) {
            version = -1.0F;
            build = -1;
            buildTime = "19700101-0000";
        }
    }

    private final Manifest getManifest() throws Exception{
        Manifest toRet = null;
        Exception ex = null;
        JarFile jar = null;
        try {
            jar = new JarFile(getJarPath());
            toRet = jar.getManifest();
        }
        catch (Exception e) {
            ex = e;
        }
        finally {
            if (jar != null) {
                try {
                    jar.close();
                }
                catch (IOException e) {}
            }
            if (ex != null) {
                throw ex;
            }
        }
        return toRet;
    }

    private final String getJarPath(){ // For when the jar isn't dConomy3.jar
        try {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            return codeSource.getLocation().toURI().getPath();
        }
        catch (URISyntaxException ex) {}
        return "plugins/dConomy3.jar";
    }

    private final void checkStatus(){
        if (status == ProgramStatus.UNKNOWN) {
            severe("dBankLite has declared itself as an 'UNKNOWN STATUS' build. Use is not advised and could cause damage to your system!");
        }
        else if (status == ProgramStatus.ALPHA) {
            warning("dBankLite has declared itself as a 'ALPHA' build. Production use is not advised!");
        }
        else if (status == ProgramStatus.BETA) {
            warning("dBankLite has declared itself as a 'BETA' build. Production use is not advised!");
        }
        else if (status == ProgramStatus.RELEASE_CANDIDATE) {
            info("dBankLite has declared itself as a 'Release Candidate' build. Expect some bugs.");
        }
    }

    public final static VersionChecker getVersionChecker(){
        return $.vc;
    }

    private final void checkVersion(){
        Boolean islatest = vc.isLatest();
        if (islatest == null) {
            warning("VersionCheckerError: " + vc.getErrorMessage());
        }
        else if (!vc.isLatest()) {
            warning(vc.getUpdateAvailibleMessage());
            warning("You can view update info @ http://wiki.visualillusionsent.net/dBankLite#ChangeLog");
        }
    }

    public final static void info(String msg){
        $.logger.info(msg);
    }

    public final static void info(String msg, Throwable thrown){
        $.logger.log(Level.INFO, msg, thrown);
    }

    public final static void warning(String msg){
        $.logger.warning(msg);
    }

    public final static void warning(String msg, Throwable thrown){
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public final static void severe(String msg){
        $.logger.severe(msg);
    }

    public final static void severe(String msg, Throwable thrown){
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public final static void stacktrace(Throwable thrown){
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public final static void debug(String msg){
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    public final static void cleanUp(){
        if ($.timer != null) {
            $.timer.cancel();
            $.timer.purge();
        }
        dCoBase.getProperties().getPropertiesFile().save();
    }

    private final long getInitialStart(){
        if (dCoBase.getProperties().getPropertiesFile().containsKey("bank.timer.reset")) {
            long reset = dCoBase.getProperties().getPropertiesFile().getLong("bank.timer.reset") - System.currentTimeMillis();
            if (reset < 0) {
                return 0;
            }
            else {
                dCoBase.getProperties().getPropertiesFile().setLong("bank.timer.reset", System.currentTimeMillis() + reset);
                return reset;
            }
        }
        else {
            setResetTime();
            return getInterestInterval();
        }
    }

    private final long getInterestInterval(){
        return dCoBase.getProperties().getPropertiesFile().getLong("interest.pay.interval") * 60000;
    }

    final void setResetTime(){
        dCoBase.getProperties().getPropertiesFile().setLong("bank.timer.reset", System.currentTimeMillis() + getInterestInterval());
    }

    private final void installBankMessages(){
        try {
            PropertiesFile lang = new PropertiesFile("config/dConomy3/lang/en_US.lang");
            if (!lang.containsKey("bank.deposit")) {
                lang.setString("bank.deposit", "$cAYou have deposited $cE{0, number, 0.00} $m$cA into your $c3Bank Account$cA.", ";dBankLite Message");
            }
            if (!lang.containsKey("bank.withdraw")) {
                lang.setString("bank.withdraw", "$cAYou have withdrawn $cE{0, number, 0.00} $m$cA from your $c3Bank Account$cA.", ";dBankLite Message");
            }
            lang.save();
            MessageTranslator.reloadMessages();
        }
        catch (UtilityException uex) {
            warning("Failed to install dBankLite messages into dConomy English file (en_US.lang)");
        }
    }
}
