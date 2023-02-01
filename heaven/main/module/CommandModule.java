/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module;

import heaven.main.command.Command;
import heaven.main.module.Module;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.math.MathUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;

public class CommandModule
extends Command {
    private final Module m;

    CommandModule(Module module, String name, String[] alias) {
        super(name, alias);
        this.m = module;
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2) {
            Value option = null;
            Numbers fuck = null;
            Mode xd = null;
            for (Value v : this.m.values) {
                if (!(v instanceof Option) || !v.getName().equalsIgnoreCase(args[0])) continue;
                option = (Option)v;
            }
            if (option != null) {
                switch (args.length) {
                    case 2: {
                        option.setValue((Boolean)option.getValue() == false);
                        Helper.sendMessage(String.format("%s> %s has been set to %s", this.getName(), option.getName(), option.getValue()));
                        break;
                    }
                    case 3: {
                        if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                            option.setValue(Boolean.parseBoolean(args[2]));
                            Helper.sendMessage(String.format("%s> %s has been set to %s", this.getName(), option.getName(), option.getValue()));
                            break;
                        }
                        Helper.sendMessage(String.format("> %s is not a boolean value!", args[2]));
                    }
                }
            } else {
                for (Value v : this.m.values) {
                    if (!(v instanceof Numbers) || !v.getName().equalsIgnoreCase(args[0])) continue;
                    fuck = (Numbers)v;
                }
                if (fuck != null) {
                    if (MathUtil.parsable(args[1], (byte)4)) {
                        double v1 = MathUtil.round(Double.parseDouble(args[1]), 1);
                        fuck.setValue(v1 > (Double)fuck.getMaximum() ? (Double)fuck.getMaximum() : Double.valueOf(v1));
                        Helper.sendMessage(String.format("> %s has been set to %s", fuck.getName(), fuck.getValue()));
                    } else {
                        Helper.sendMessage("> " + args[1] + " is not a number!");
                    }
                }
                for (Value v : this.m.getValues()) {
                    if (!args[0].equalsIgnoreCase(v.getName()) || !(v instanceof Mode)) continue;
                    xd = (Mode)v;
                }
                if (xd != null) {
                    if (xd.isValid(args[1])) {
                        xd.setMode(args[1]);
                        Helper.sendMessage(String.format("> %s set to %s", xd.getName(), xd.getModeAsString()));
                    } else {
                        Helper.sendMessage("> " + args[1] + " is an invalid mode");
                    }
                }
            }
            if (fuck == null && option == null && xd == null) {
                Command.syntaxError();
            }
        } else if (args.length >= 1) {
            Value option = null;
            for (Value fuck1 : this.m.getValues()) {
                if (!(fuck1 instanceof Option) || !fuck1.getName().equalsIgnoreCase(args[0])) continue;
                option = (Option)fuck1;
            }
            if (option != null) {
                option.setValue((Boolean)option.getValue() == false);
                String fuck2 = option.getName().substring(1);
                String xd2 = option.getName().substring(0, 1).toUpperCase();
                if (((Boolean)option.getValue()).booleanValue()) {
                    Helper.sendMessage(String.format("> %s has been set to \u00a7a%s", xd2 + fuck2, option.getValue()));
                } else {
                    Helper.sendMessage(String.format("> %s has been set to \u00a7c%s", xd2 + fuck2, option.getValue()));
                }
            } else {
                Command.syntaxError();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Value v : this.m.getValues()) {
                stringBuilder.append(v.getName()).append(", ");
            }
            Helper.sendMessage(String.format("%s Values: \n %s", this.getName().substring(0, 1).toUpperCase() + this.getName().substring(1).toLowerCase(), stringBuilder.substring(0, stringBuilder.toString().length() - 2)));
        }
    }
}

