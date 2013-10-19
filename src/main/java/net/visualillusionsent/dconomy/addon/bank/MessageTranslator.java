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

import net.visualillusionsent.dconomy.api.MineChatForm;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.LocaleHelper;

import java.io.File;

public final class MessageTranslator extends LocaleHelper {

    static {
        boolean mvLangtxt = false, mven_US = false;
        if (!new File(dBankLiteBase.lang_dir).exists()) {
            new File(dBankLiteBase.lang_dir).mkdirs();
            mvLangtxt = true;
            mven_US = true;
        }
        else {
            if (!new File(dBankLiteBase.lang_dir.concat("languages.txt")).exists()) {
                mvLangtxt = true;
            }
            if (!new File(dBankLiteBase.lang_dir.concat("en_US.lang")).exists()) {
                mven_US = true;
            }
        }
        if (mvLangtxt) {
            FileUtils.cloneFileFromJar(JarUtils.getJarPath(dBankLiteBase.class), "resources/lang/languages.txt", dBankLiteBase.lang_dir.concat("languages.txt"));
        }
        if (mven_US) {
            FileUtils.cloneFileFromJar(JarUtils.getJarPath(dBankLiteBase.class), "resources/lang/en_US.lang", dBankLiteBase.lang_dir.concat("en_US.lang"));
        }
    }

    MessageTranslator() {
        super(true, dBankLiteBase.lang_dir, dCoBase.getServerLocale());
        reloadLangFiles();
    }

    public final String translate(String key, String locale, Object... args) {
        String toRet = args != null ? colorForm(localeTranslate(key, locale, args)) : colorForm(localeTranslate(key, locale));
        if (toRet.contains("$m")) {
            toRet = toRet.replace("$m", dCoBase.getProperties().getString("money.name"));
        }
        return toRet;
    }

    private String colorForm(String msg) {
        return msg.replace("$c", MineChatForm.MARKER.stringValue());
    }
}
