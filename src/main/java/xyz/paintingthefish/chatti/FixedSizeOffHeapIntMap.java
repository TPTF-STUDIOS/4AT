package xyz.paintingthefish.chatti;

import java.nio.ByteBuffer;

public class FixedSizeOffHeapIntMap {
    private static final int HEADER_SIZE = 1;
    private static final int KEY_SIZE = 4;

    private final ByteBuffer buffer;
    private final int valueLength;
    private final int entrySize;
    private final int capacity;

    // Status flags for slot management
    private static final byte EMPTY = 0;
    private static final byte OCCUPIED = 1;
    private static final byte DELETED = 2; // Tombstone flag for removals

    public FixedSizeOffHeapIntMap(int totalBytes, int valueLength) {
        this.valueLength = valueLength;
        this.entrySize = HEADER_SIZE + KEY_SIZE + valueLength;
        this.capacity = totalBytes / this.entrySize;
        this.buffer = ByteBuffer.allocateDirect(totalBytes);
    }

    private int hash(int key) {
        return (key & Integer.MAX_VALUE) % capacity;
    }

    public void put(int key, byte[] value) {
        if (value.length != valueLength) {
            throw new IllegalArgumentException("Value must be exactly " + valueLength + " bytes");
        }

        int slot = hash(key);
        int attempts = 0;
        int firstDeletedSlot = -1;

        while (attempts < capacity) {
            int offset = slot * entrySize;
            byte status = buffer.get(offset);

            // Track the first tombstone we encounter to reuse it if the key isn't already
            // in the map
            if (status == DELETED && firstDeletedSlot == -1) {
                firstDeletedSlot = slot;
            }

            // If empty, we can stop searching for duplicates and insert
            if (status == EMPTY) {
                int targetSlot = (firstDeletedSlot != -1) ? firstDeletedSlot : slot;
                writeToSlot(targetSlot, key, value);
                return;
            }

            // If updating an existing key
            if (status == OCCUPIED && buffer.getInt(offset + HEADER_SIZE) == key) {
                writeToSlot(slot, key, value);
                return;
            }

            slot = (slot + 1) % capacity;
            attempts++;
        }

        // If the map was full of OCCUPIED/DELETED slots but we found a tombstone, reuse
        // it
        if (firstDeletedSlot != -1) {
            writeToSlot(firstDeletedSlot, key, value);
            return;
        }

        throw new IllegalStateException("Map is completely full!");
    }

    public byte[] get(int key) {
        int slot = hash(key);
        int attempts = 0;

        while (attempts < capacity) {
            int offset = slot * entrySize;
            byte status = buffer.get(offset);

            if (status == EMPTY) {
                return null; // Probe chain broken natively by empty slot
            }

            if (status == OCCUPIED && buffer.getInt(offset + HEADER_SIZE) == key) {
                byte[] result = new byte[valueLength];
                // Thread-safe bulk get using absolute offset index
                buffer.get(offset + HEADER_SIZE + KEY_SIZE, result, 0, valueLength);
                return result;
            }

            // If status == DELETED, we keep probing!
            slot = (slot + 1) % capacity;
            attempts++;
        }
        return null;
    }

    /**
     * Removes the key and its value, freeing the slot for future reuse.
     * 
     * @return true if the key was found and removed, false otherwise.
     */
    public boolean remove(int key) {
        int slot = hash(key);
        int attempts = 0;

        while (attempts < capacity) {
            int offset = slot * entrySize;
            byte status = buffer.get(offset);

            if (status == EMPTY) {
                return false; // Key definitely does not exist
            }

            if (status == OCCUPIED && buffer.getInt(offset + HEADER_SIZE) == key) {
                // Set status to DELETED (Tombstone) via absolute offset
                buffer.put(offset, DELETED);

                // Privacy Wipe: Clear the data payload cleanly using absolute offsets
                buffer.putInt(offset + HEADER_SIZE, 0);
                byte[] zeroPayload = new byte[valueLength];
                buffer.put(offset + HEADER_SIZE + KEY_SIZE, zeroPayload, 0, valueLength);

                return true;
            }

            slot = (slot + 1) % capacity;
            attempts++;
        }
        return false;
    }

    private void writeToSlot(int slot, int key, byte[] value) {
        int offset = slot * entrySize;
        // Replaced sequential position operations with thread-safe absolute index
        // insertions
        buffer.put(offset, OCCUPIED);
        buffer.putInt(offset + HEADER_SIZE, key);
        buffer.put(offset + HEADER_SIZE + KEY_SIZE, value, 0, valueLength);
    }
}
