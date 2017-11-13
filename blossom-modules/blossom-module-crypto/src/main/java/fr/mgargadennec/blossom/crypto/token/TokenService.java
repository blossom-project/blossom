package fr.mgargadennec.blossom.crypto.token;

public interface TokenService {

    /**
     * Crypt String data into a token
     *
     * @param data A string to turn into a token
     * @return A reversible token that contains the data
     * @throws NullPointerException if data is null
     */
    public String crypt(String data);

    /**
     * Crypt a byte array into a token
     *
     * @param data A byte array to turn into a token
     * @return A reversible token that contains the data
     */
    public String crypt(byte[] data);

    /**
     * Recovers the data from a token, as a String
     *
     * @param token A token obtained from this TokenService
     * @return The original data as String
     */
    public String decrypt(String token);

    /**
     * Recovers the data from a token, as a byte array
     *
     * @param token A token obtained from this TokenService
     * @return The original data as a byte array
     */
    public byte[] decryptBytes(String token);
}
