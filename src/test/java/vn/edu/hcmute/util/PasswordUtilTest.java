package vn.edu.hcmute.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordUtilTest {
    @Test
    public void hashesAndVerifiesPassword() {
        String firstHash = PasswordUtil.hash("secret123");
        String secondHash = PasswordUtil.hash("secret123");

        assertNotEquals(firstHash, secondHash);
        assertTrue(PasswordUtil.verify("secret123", firstHash));
        assertFalse(PasswordUtil.verify("wrong-password", firstHash));
    }
}
