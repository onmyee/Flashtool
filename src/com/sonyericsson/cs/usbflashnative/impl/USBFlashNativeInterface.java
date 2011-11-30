package com.sonyericsson.cs.usbflashnative.impl;

import java.io.IOException;

public abstract interface USBFlashNativeInterface
{
  public abstract int getMaxAllowedBufferSize();

  public abstract boolean close(int paramInt);

  public abstract boolean writeBytes(int paramInt, byte[] paramArrayOfByte)
    throws IOException;

  public abstract byte[] readBytes(int paramInt1, int paramInt2)
    throws IOException;

  public abstract byte[] readBytes(int paramInt1, int paramInt2, int paramInt3)
    throws IOException;

  public abstract int rxByteCount(int paramInt)
    throws IOException;

  public abstract boolean switchToDFU(int paramInt)
    throws IOException;

  public abstract boolean clearStatusDFU(int paramInt)
    throws IOException;

  public abstract byte[] getStatusDFU(int paramInt)
    throws IOException;

  public abstract boolean downloadDFU(int paramInt, byte[] paramArrayOfByte)
    throws IOException;

  public abstract int openChannel(String paramString, boolean paramBoolean)
    throws IOException;
}