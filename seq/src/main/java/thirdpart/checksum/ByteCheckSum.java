package thirdpart.checksum;

public class ByteCheckSum implements IChecksum {

    @Override
    public byte getChecksum(byte[] data) {
        byte sum = 0;
        for (byte b : data) {
            sum ^= b;
        }
        return sum;
    }
}
