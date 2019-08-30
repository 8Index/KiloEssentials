package org.kilocraft.essentials.utils;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.text.LiteralText;
import org.kilocraft.essentials.Mod;

public class LangText {

    public static LiteralText getFormatter (boolean allowColorCodes, String key, @NotNull Object... objects) {
        String lang = Mod.lang.getProperty(key);
        LiteralText literalText = new LiteralText ("");
        String result = null;
        Object tmpObject;

        if (objects[0] != null) {
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                result = lang.replaceFirst("%s", object.toString());
            }
        }

        if (allowColorCodes) {
            result = ChatColor.translateAlternateColorCodes('&', result);
        } else {
            result = ChatColor.removeAlternateColorCodes('&', result);
        }


        literalText.append(result);
        return literalText;
    }

    public static LiteralText get(boolean allowColorCodes, String key) {
        String lang = Mod.lang.getProperty(key);
        LiteralText literalText = new LiteralText ("");
        String result = lang;

        if (allowColorCodes) {
            result = ChatColor.translateAlternateColorCodes('&', result);
        } else {
            result = ChatColor.removeAlternateColorCodes('&', result);
        }

        literalText.append(result);
        return literalText;
    }

}