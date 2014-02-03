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
package net.visualillusionsent.dconomy.addon.bank;

import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.ChatFormat;
import net.visualillusionsent.minecraft.plugin.MessageTranslator;
import net.visualillusionsent.minecraft.plugin.VisualIllusionsPlugin;

public final class dBankLiteTranslator extends MessageTranslator {

    dBankLiteTranslator(dBankLite dBankLite) {
        super((VisualIllusionsPlugin) dBankLite, dCoBase.getServerLocale(), true);
    }

    public final String translate(String key, String locale, Object... args) {
        return ChatFormat.formatString(localeTranslate(key, locale, args), "~").replace("$m", dCoBase.getMoneyName());
    }
}
