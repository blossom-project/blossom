package com.blossomproject.module.filemanager.digest;

import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.io.DigestOutputStream;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
public interface DigestUtil {
  String getHash(DigestInputStream digestInputStream);

  String getHash(DigestOutputStream digestOutputStream);

}
