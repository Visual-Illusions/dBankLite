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
package net.visualillusionsent.dconomy.addon.bank.data;

import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.File;
import java.io.IOException;

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
            try {
                FileUtils.cloneFileFromJar(JarUtils.getJarPath(getClass()), "resources/default_config.cfg", configDir.concat("settings.cfg"));
            }
            catch (IOException e) {
                // Doesn't matter
            }
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
