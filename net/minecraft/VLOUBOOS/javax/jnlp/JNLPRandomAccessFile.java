/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.jnlp;

public interface JNLPRandomAccessFile {
    public void close();

    public long length();

    public long getFilePointer();

    public int read();

    public int read(byte[] var1, int var2, int var3);

    public int read(byte[] var1);

    public void readFully(byte[] var1);

    public void readFully(byte[] var1, int var2, int var3);

    public int skipBytes(int var1);

    public boolean readBoolean();

    public byte readByte();

    public int readUnsignedByte();

    public short readShort();

    public int readUnsignedShort();

    public char readChar();

    public int readInt();

    public long readLong();

    public float readFloat();

    public double readDouble();

    public String readLine();

    public String readUTF();

    public void seek(long var1);

    public void setLength(long var1);

    public void write(int var1);

    public void write(byte[] var1);

    public void write(byte[] var1, int var2, int var3);

    public void writeBoolean(boolean var1);

    public void writeByte(int var1);

    public void writeShort(int var1);

    public void writeChar(int var1);

    public void writeInt(int var1);

    public void writeLong(long var1);

    public void writeFloat(float var1);

    public void writeDouble(double var1);

    public void writeBytes(String var1);

    public void writeChars(String var1);

    public void writeUTF(String var1);
}

