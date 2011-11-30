package com.sonyericsson.cs.usbflashnative.impl;

import java.io.IOException;

public class USBFlashNativeImpl
  implements USBFlashNativeInterface
{
  public static int a;
  public static int b;

  public native int getMaxAllowedBufferSize();

  public native boolean close(int paramInt);

  public native boolean writeBytes(int paramInt, byte[] paramArrayOfByte)
    throws IOException;

  public native byte[] readBytes(int paramInt1, int paramInt2)
    throws IOException;

  public native byte[] readBytes(int paramInt1, int paramInt2, int paramInt3)
    throws IOException;

  public native int rxByteCount(int paramInt)
    throws IOException;

  public native boolean switchToDFU(int paramInt)
    throws IOException;

  public native boolean clearStatusDFU(int paramInt)
    throws IOException;

  public native byte[] getStatusDFU(int paramInt)
    throws IOException;

  public native boolean downloadDFU(int paramInt, byte[] paramArrayOfByte)
    throws IOException;

  public native int openChannel(String paramString, boolean paramBoolean)
    throws IOException;
}