/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.login;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public final class GuiUserField
extends Gui {
    private final int field_146209_f;
    private final int field_146210_g;
    private final FontRenderer field_146211_a;
    private final boolean field_146212_n;
    private boolean field_146213_o;
    private final boolean field_146215_m;
    private String field_146216_j = "";
    private final int field_146217_k;
    private final int field_146218_h;
    private final int field_146219_i;
    private int field_146223_s;
    private int field_146224_r;
    private int field_146225_q;
    private final boolean field_146226_p;

    public GuiUserField(FontRenderer p_i1032_1_, int p_i1032_2_, int p_i1032_3_, int p_i1032_4_, int p_i1032_5_) {
        this.field_146212_n = true;
        this.field_146215_m = true;
        this.field_146217_k = 32;
        this.field_146226_p = true;
        this.field_146211_a = p_i1032_1_;
        this.field_146209_f = p_i1032_2_;
        this.field_146210_g = p_i1032_3_;
        this.field_146218_h = p_i1032_4_;
        this.field_146219_i = p_i1032_5_;
    }

    public void func_146175_b(int p_146175_1_) {
        if (!this.field_146216_j.isEmpty()) {
            if (this.field_146223_s != this.field_146224_r) {
                this.func_146191_b("");
            } else {
                boolean var2 = p_146175_1_ < 0;
                int var3 = var2 ? this.field_146224_r + p_146175_1_ : this.field_146224_r;
                int var4 = var2 ? this.field_146224_r : this.field_146224_r + p_146175_1_;
                String var5 = "";
                if (var3 >= 0) {
                    var5 = this.field_146216_j.substring(0, var3);
                }
                if (var4 < this.field_146216_j.length()) {
                    var5 = var5 + this.field_146216_j.substring(var4);
                }
                this.field_146216_j = var5;
                if (var2) {
                    this.func_146182_d(p_146175_1_);
                }
            }
        }
    }

    public void func_146177_a(int p_146177_1_) {
        if (!this.field_146216_j.isEmpty()) {
            if (this.field_146223_s != this.field_146224_r) {
                this.func_146191_b("");
            } else {
                this.func_146175_b(this.func_146187_c(p_146177_1_) - this.field_146224_r);
            }
        }
    }

    public boolean func_146181_i() {
        return this.field_146215_m;
    }

    public void func_146182_d(int p_146182_1_) {
        this.func_146190_e(this.field_146223_s + p_146182_1_);
    }

    public int func_146183_a(int p_146183_1_) {
        return this.func_146197_a(p_146183_1_, this.func_146198_h(), true);
    }

    public int func_146186_n() {
        return this.field_146223_s;
    }

    public int func_146187_c(int p_146187_1_) {
        return this.func_146183_a(p_146187_1_);
    }

    public void func_146190_e(int p_146190_1_) {
        this.field_146224_r = p_146190_1_;
        int var2 = this.field_146216_j.length();
        if (this.field_146224_r < 0) {
            this.field_146224_r = 0;
        }
        if (this.field_146224_r > var2) {
            this.field_146224_r = var2;
        }
        this.func_146199_i(this.field_146224_r);
    }

    public void func_146191_b(String p_146191_1_) {
        int var7;
        String var2 = "";
        String var3 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        int var4 = Math.min(this.field_146224_r, this.field_146223_s);
        int var5 = Math.max(this.field_146224_r, this.field_146223_s);
        int var6 = this.field_146217_k - this.field_146216_j.length() - (var4 - this.field_146223_s);
        if (!this.field_146216_j.isEmpty()) {
            var2 = var2 + this.field_146216_j.substring(0, var4);
        }
        if (var6 < var3.length()) {
            var2 = var2 + var3.substring(0, var6);
            var7 = var6;
        } else {
            var2 = var2 + var3;
            var7 = var3.length();
        }
        if (!this.field_146216_j.isEmpty() && var5 < this.field_146216_j.length()) {
            var2 = var2 + this.field_146216_j.substring(var5);
        }
        this.field_146216_j = var2;
        this.func_146182_d(var4 - this.field_146223_s + var7);
    }

    public void func_146196_d() {
        this.func_146190_e(0);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
        int var4 = p_146197_2_;
        boolean var5 = p_146197_1_ < 0;
        int var6 = Math.abs(p_146197_1_);
        for (int var7 = 0; var7 < var6; ++var7) {
            if (var5) {
                while (!p_146197_3_ || --var4 <= 0 || this.field_146216_j.charAt(var4 - 1) == ' ') {
                }
                while (--var4 > 0 && this.field_146216_j.charAt(var4 - 1) != ' ') {
                }
                continue;
            }
            int var8 = this.field_146216_j.length();
            if ((var4 = this.field_146216_j.indexOf(32, var4)) == -1) {
                var4 = var8;
                continue;
            }
            while (p_146197_3_ && var4 < var8 && this.field_146216_j.charAt(var4) == ' ') {
                ++var4;
            }
        }
        return var4;
    }

    public int func_146198_h() {
        return this.field_146224_r;
    }

    public void func_146199_i(int p_146199_1_) {
        int var2 = this.field_146216_j.length();
        if (p_146199_1_ > var2) {
            p_146199_1_ = var2;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        this.field_146223_s = p_146199_1_;
        if (this.field_146211_a != null) {
            if (this.field_146225_q > var2) {
                this.field_146225_q = var2;
            }
            int var3 = this.func_146200_o();
            String var4 = this.field_146211_a.trimStringToWidth(this.field_146216_j.substring(this.field_146225_q), var3);
            int var5 = var4.length() + this.field_146225_q;
            if (p_146199_1_ == this.field_146225_q) {
                this.field_146225_q -= this.field_146211_a.trimStringToWidth(this.field_146216_j, var3, true).length();
            }
            if (p_146199_1_ > var5) {
                this.field_146225_q += p_146199_1_ - var5;
            } else if (p_146199_1_ <= this.field_146225_q) {
                this.field_146225_q -= this.field_146225_q - p_146199_1_;
            }
            if (this.field_146225_q < 0) {
                this.field_146225_q = 0;
            }
            if (this.field_146225_q > var2) {
                this.field_146225_q = var2;
            }
        }
    }

    public int func_146200_o() {
        return this.func_146181_i() ? this.field_146218_h - 8 : this.field_146218_h;
    }

    public void func_146202_e() {
        this.func_146190_e(this.field_146216_j.length());
    }

    public String func_146207_c() {
        int var1 = Math.min(this.field_146224_r, this.field_146223_s);
        int var2 = Math.max(this.field_146224_r, this.field_146223_s);
        return this.field_146216_j.substring(var1, var2);
    }

    public String getText() {
        return this.field_146216_j;
    }

    public boolean isFocused() {
        return this.field_146213_o;
    }

    public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
        boolean var4;
        boolean bl = var4 = p_146192_1_ >= this.field_146209_f && p_146192_1_ < this.field_146209_f + this.field_146218_h && p_146192_2_ >= this.field_146210_g && p_146192_2_ < this.field_146210_g + this.field_146219_i;
        if (this.field_146212_n) {
            this.field_146213_o = var4;
        }
        if (this.field_146213_o && p_146192_3_ == 0) {
            int var5 = p_146192_1_ - this.field_146209_f;
            if (this.field_146215_m) {
                var5 -= 4;
            }
            String var6 = this.field_146211_a.trimStringToWidth(this.field_146216_j.substring(this.field_146225_q), this.func_146200_o());
            this.func_146190_e(this.field_146211_a.trimStringToWidth(var6, var5).length() + this.field_146225_q);
        }
    }

    public void setFocused(boolean p_146195_1_) {
        this.field_146213_o = p_146195_1_;
    }

    public void textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
        if (!this.field_146213_o) {
            return;
        }
        switch (p_146201_1_) {
            case '\u0001': {
                this.func_146202_e();
                this.func_146199_i(0);
                return;
            }
            case '\u0003': {
                GuiScreen.setClipboardString(this.func_146207_c());
                return;
            }
            case '\u0016': {
                if (this.field_146226_p) {
                    this.func_146191_b(GuiScreen.getClipboardString());
                }
                return;
            }
            case '\u0018': {
                GuiScreen.setClipboardString(this.func_146207_c());
                if (this.field_146226_p) {
                    this.func_146191_b("");
                }
                return;
            }
        }
        switch (p_146201_2_) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.field_146226_p) {
                        this.func_146177_a(-1);
                    }
                } else if (this.field_146226_p) {
                    this.func_146175_b(-1);
                }
                return;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.func_146199_i(0);
                } else {
                    this.func_146196_d();
                }
                return;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.func_146199_i(this.func_146183_a(-1));
                    } else {
                        this.func_146199_i(this.func_146186_n() - 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.func_146190_e(this.func_146187_c(-1));
                } else {
                    this.func_146182_d(-1);
                }
                return;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.func_146199_i(this.func_146183_a(1));
                    } else {
                        this.func_146199_i(this.func_146186_n() + 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.func_146190_e(this.func_146187_c(1));
                } else {
                    this.func_146182_d(1);
                }
                return;
            }
            case 207: {
                System.out.println("88888");
                if (GuiScreen.isShiftKeyDown()) {
                    this.func_146199_i(this.field_146216_j.length());
                } else {
                    this.func_146202_e();
                }
                return;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.field_146226_p) {
                        this.func_146177_a(1);
                    }
                } else if (this.field_146226_p) {
                    this.func_146175_b(1);
                }
                return;
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_) && this.field_146226_p) {
            this.func_146191_b(Character.toString(p_146201_1_));
        }
    }
}

