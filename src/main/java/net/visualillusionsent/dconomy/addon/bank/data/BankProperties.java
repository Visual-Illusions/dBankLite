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
package net.visualillusionsent.dconomy.addon.bank.data;

import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.File;

/**
 * dBankLite Properties
 *
 * @author Jason (darkdiplomat)
 */
public class BankProperties {
    private final PropertiesFile propsFile;
    private final String configDir;

    public BankProperties() {
        configDir = "config/dBankLite/";

        File dir = new File(configDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File real = new File(configDir.concat("settings.cfg"));
        if (!real.exists()) {
            FileUtils.cloneFileFromJar(JarUtils.getJarPath(getClass()), "resources/default_config.cfg", configDir.concat("settings.cfg"));
        }
        propsFile = new PropertiesFile(configDir.concat("settings.cfg"));
        testProperties();
    }

    private void testProperties() {
        propsFile.getDouble("interest.rate", 2.0F);
        propsFile.setComments("interest.rate", "Interest Rate (in percentage) (Default: 2%)");

        propsFile.getDouble("interest.pay.interval", 360);
        propsFile.setComments("interest.pay.interval", "Interest Pay Interval (in minutes) (Default: 360 [6 Hours]) Set to 0 or less to disable");

        propsFile.getDouble("interest.max.payout", 10000.0D);
        propsFile.setComments("interest.max.payout", "Max Interest Payout (Default: 10000)");
        if (propsFile.getDouble("interest.max.payout") > 999999999999999999.0D) {
            propsFile.setDouble("interest.max.payout", 999999999999999999.0D);
        }

        propsFile.getString("sql.bank.table", "dBankLite");
        propsFile.setComments("sql.bank.table", "SQL Bank table (All other SQL settings retrieved from dConomy)");

        propsFile.save();
    }

    public final String getString(String key) {
        return propsFile.getString(key);
    }

    public final double getDouble(String key) {
        return propsFile.getDouble(key);
    }

    public final long getLong(String key) {
        return propsFile.getLong(key);
    }
}
