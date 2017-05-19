package fr.mgargadennec.blossom.module.filemanager.store;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.io.DigestOutputStream;
import org.bouncycastle.util.encoders.Hex;

public class DigestUtilImpl implements DigestUtil {

  @Override
  public String getHash(DigestInputStream digestInputStream) {
    return digestToString(digestInputStream.getDigest());
  }

  @Override
  public String getHash(DigestOutputStream digestOutputStream) {
    return digestToString(digestOutputStream.getDigest());
  }

  private String digestToString(Digest digest) {
    byte[] bytes = new byte[digest.getDigestSize()];
    digest.doFinal(bytes, 0);
    return new String(Hex.encode(bytes));
  }
}
